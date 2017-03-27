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
 * @author guanhong 2017/2/22.
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

    private static List<Class> annotationClazz = Arrays.asList(Component.class, Repository.class, Service.class);
    private static List<Class> annotationField = Arrays.asList(Autowried.class, Resource.class);

    private static boolean canIOC(Class clazz) {
        return annotationClazz.stream().anyMatch(clazz::isAnnotationPresent);
    }

    private static boolean canIOC(Field field) {
        return annotationField.stream().anyMatch(field::isAnnotationPresent);
    }

    public static void init() {
        try {
            List<Class> clazzList = getClasssFromPackages(true);
            IOC(clazzList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
                        if (file.isFile()) {
                            if (file.getName().endsWith(".class")) {
                                clazzList.add(Thread.currentThread().getContextClassLoader().loadClass(packageStr + '.' + file.getName().substring(0, file.getName().length() - 6)));
                            }
                        }
                    }
                }
            } else {
                throw new PackageNotFoundException("package name must exist! package path: " + packPath);
            }
        }
        return clazzList;
    }


    private static void IOC(List<Class> clazzList) throws IllegalAccessException, InstantiationException, ClassNotDeclearException {
        for (Class clazz : clazzList) {
            if (canIOC(clazz)) {
                //初始化对象
                Object instance = clazz.newInstance();
                beanContainer.put(clazz.getName(), instance);
                //处理代理对象
                if (clazz.isAnnotationPresent(Transaction.class)) {
                    Transaction transaction = (Transaction) clazz.getAnnotation(Transaction.class);
                    MethodInterceptor dynamicBean = (MethodInterceptor) transaction.transactionDynamicClass().newInstance();
                    Field[] fields = dynamicBean.getClass().getDeclaredFields();
                    for (Field f : fields) {
                        if (f.getName().equals("dynamicObject")) {
                            if (!f.isAccessible()) {
                                f.setAccessible(true);
                            }
                            f.set(dynamicBean, instance);
                            break;
                        }
                    }
                    dynamicBeanContainer.put(clazz.getName(), Enhancer.create(instance.getClass(), dynamicBean));
                }
            }
        }

        //处理注入
        for (Map.Entry<String, Object> entry : beanContainer.entrySet()) {
            Object o = entry.getValue();
            Field[] fieldList = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fieldList) {
                if (!field.isAccessible()) {
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
