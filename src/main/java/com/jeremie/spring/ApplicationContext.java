package com.jeremie.spring;

import com.jeremie.bean.factory.annotation.Autowried;
import com.jeremie.bean.factory.annotation.Resource;
import com.jeremie.bean.factory.annotation.Transaction;
import com.jeremie.connection.Connection;
import com.jeremie.connection.MyPool;
import com.jeremie.exception.ClassNotDeclearException;
import com.jeremie.exception.PackageNotFoundException;
import com.jeremie.stereotype.Component;
import com.jeremie.stereotype.Repository;
import com.jeremie.stereotype.Service;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class ApplicationContext {

    public static MyPool<Connection> connectionPool = new MyPool<>(Connection::new);
    public static ThreadLocal<Connection> connectionThreadLocal = ThreadLocal.withInitial(ApplicationContext.connectionPool::getConnection);

    private static List<String> packageList = new ArrayList<>();

    public static void setScanPackages(String... packages) {
        packageList.addAll(Arrays.asList(packages));
    }

    public static void setScanPackages(List<String> packages) {
        packageList.addAll(packages);
    }

    //单例容器（代理对象与非代理对象）
    private static Map<String, Object> beanContainer = new HashMap<>();
    private static Map<String, Object> dynamicBeanContainer = new HashMap<>();

    //spring的注解
    private static List<Class> annotationClazz = Arrays.asList(Component.class, Repository.class, Service.class);
    private static List<Class> annotationField = Arrays.asList(Autowried.class, Resource.class);

    private static boolean canIOC(Class clazz) {
        return annotationClazz.stream().anyMatch(clazz::isAnnotationPresent);
    }

    private static boolean canIOC(Field field) {
        return annotationField.stream().anyMatch(field::isAnnotationPresent);
    }

    /**
     * 初始化
     */
    public static void init() throws Exception {
        try {
            List<Class> clazzList = getClasssFromPackages(true);
            IOC(clazzList);
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 扫描当前项目里面的包
     *
     * @param recursive recursive
     * @return List
     * @throws Exception
     */
    private static List<Class> getClasssFromPackages(final boolean recursive) throws Exception {
        List<Class> clazzList = new ArrayList<>();
        Set<String> packageSet = new HashSet<>();
        if (recursive) {
            //先取出所有的包
            Queue<String> packageQueue = new LinkedBlockingQueue<>();
            packageQueue.addAll(packageList);
            while (!packageQueue.isEmpty()) {
                String pack = packageQueue.poll();
                packageSet.add(pack);
                String packPath = pack.replace('.', File.separatorChar);
                URL packDir = Thread.currentThread().getContextClassLoader().getResource(packPath);
                if (packDir != null) {
                    String filePathStr = packDir.getPath();
                    File filePath = new File(filePathStr);
                    File[] fileList = filePath.listFiles();
                    if (fileList != null) {
                        for (File file : fileList) {
                            if (file.isDirectory()) {
                                packageQueue.add(pack + '.' + file.getName());
                            }
                        }
                    }
                } else {
                    throw new PackageNotFoundException("package name must exist! package path: " + packPath);
                }
            }
        } else {
            packageSet.addAll(packageList);
        }
        //遍历包里面文件读取类
        for (String packageStr : packageSet) {
            String packPath = packageStr.replace('.', File.separatorChar);
            URL packDir = Thread.currentThread().getContextClassLoader().getResource(packPath);
            if (packDir != null) {
                File packageFile = new File(packDir.getPath());
                File[] subFiles = packageFile.listFiles();
                if (subFiles != null) {
                    for (File file : subFiles) {
                        if (!file.isFile() || !file.getName().endsWith(".class")) {
                            continue;
                        }
                        clazzList.add(Thread.currentThread().getContextClassLoader().loadClass(packageStr + '.' + file.getName().substring(0, file.getName().length() - 6)));
                    }
                }
            } else {
                throw new PackageNotFoundException("package name must exist! package path: " + packPath);
            }
        }
        return clazzList;
    }


    /**
     * 注入处理
     *
     * @param clazzList 类列表
     * @throws IllegalAccessException   IllegalAccessException
     * @throws InstantiationException   InstantiationException
     * @throws ClassNotDeclearException ClassNotDeclearException
     */
    private static void IOC(List<Class> clazzList) throws IllegalAccessException, InstantiationException, ClassNotDeclearException {
        //实例化bean
        for (Class clazz : clazzList) {
            //判断类是否需要注入
            if (!canIOC(clazz)) {
                continue;
            }
            //实例化bean
            Object instance = clazz.newInstance();
            beanContainer.put(clazz.getName(), instance);
            //处理代理对象
            if (clazz.isAnnotationPresent(Transaction.class)) {
                Transaction transaction = (Transaction) clazz.getAnnotation(Transaction.class);
                //事务的代理类
                MethodInterceptor methodInterceptor = (MethodInterceptor) transaction.transactionDynamicClass().newInstance();
                //创建并实例化代理类
                dynamicBeanContainer.put(clazz.getName(), Enhancer.create(instance.getClass(), methodInterceptor));
            }
        }
        //注入没有代理的bean
        dealIOC(beanContainer);
        //注入代理的bean
        dealIOC(dynamicBeanContainer);
    }

    /**
     * 处理注入
     *
     * @param container container
     */
    private static void dealIOC(Map<String, Object> container) throws IllegalAccessException, ClassNotDeclearException {
        //处理注入
        for (Map.Entry<String, Object> entry : container.entrySet()) {
            Object o = entry.getValue();

            List<Field> fieldList = new ArrayList<>();
            Class curClazz = o.getClass();
            //便利参数列表
            while (curClazz != null) {
                fieldList.addAll(Arrays.asList(curClazz.getDeclaredFields()));
                curClazz = curClazz.getSuperclass();
            }
            for (Field field : fieldList) {
                boolean accessible = field.isAccessible();
                if (!accessible) {
                    field.setAccessible(true);
                }
                if (canIOC(field)) {
                    Object iocObject = getBean(field.getType().getName());
                    if (iocObject != null) {
                        field.set(o, iocObject);
                    } else {
                        throw new ClassNotDeclearException(field.getType().getName());
                    }
                }
                field.setAccessible(accessible);
            }
        }
    }

    //从容器中获取bean
    public static Object getBean(String beanName) {
        return dynamicBeanContainer.getOrDefault(beanName,
                beanContainer.getOrDefault(beanName,
                        null));
    }
}
