package com.jeremie.spring;

import com.jeremie.connection.Connection;
import com.jeremie.connection.MyPool;
import com.jeremie.demo.MyDao;
import com.jeremie.demo.MyService;
import net.sf.cglib.proxy.Enhancer;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author guanhong 2017/2/22.
 */
public class ApplicationContext {

    public static MyPool<Connection> connectionPool = new MyPool<>(Connection::new);
    public static ThreadLocal<Connection> connectionThreadLocal = ThreadLocal.withInitial(ApplicationContext.connectionPool::getConnection);

    //单例
    Map<String, Object> singleImplement = new HashMap<>();
    Map<Class, Object> singleClazzImplement = new HashMap<>();

    public static MyDao myDao = new MyDao();
    public static MyService myService = (MyService) Enhancer.create(MyService.class, new DynamicService(new MyService()));


    public static List<String> packageList = new ArrayList<>();

    public static void setScanPackages(String... packages) {
        packageList.addAll(Arrays.asList(packages));
    }

    public static void init() {
        for (String packageName : packageList) {

        }
    }


    public static List<Class> getClasssFromPackages() throws Exception {
        List<Class> clazzs = new ArrayList<>();
        for (String pack : packageList) {
            URL packDir = Thread.currentThread().getContextClassLoader().getResource(pack);
            if (packDir != null) {
                String filePath = packDir.getPath();

            } else {
                throw new Exception("package name must exist! package: " + pack);
            }

        }
        return clazzs;
    }

    public static List<Class> getClasssFromPackage(String pack) {
        List<Class> clazzs = new ArrayList<>();

        // 是否循环搜索子包
        boolean recursive = true;

        // 包名字
        String packageName = pack;
        // 包名对应的路径名称
        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();

                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    System.out.println("file类型的扫描");
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassInPackageByFile(packageName, filePath, recursive, clazzs);
                } else if ("jar".equals(protocol)) {
                    System.out.println("jar类型的扫描");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clazzs;
    }

    /**
     * 在package对应的路径下找到所有的class
     *
     * @param packageName package名称
     * @param filePath    package对应的路径
     * @param recursive   是否查找子package
     * @param clazzs      找到class以后存放的集合
     */
    public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive, List<Class> clazzs) {
        File dir = new File(filePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 在给定的目录下找到所有的文件，并且进行条件过滤
        File[] dirFiles = dir.listFiles(file -> {
            boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
            boolean acceptClass = file.getName().endsWith("class");// 接受class文件
            return acceptDir || acceptClass;
        });
        if (dirFiles != null) {
            for (File file : dirFiles) {
                if (file.isDirectory()) {
                    findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
                } else {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    try {
                        clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
