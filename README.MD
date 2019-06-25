## 模拟spring容器，连接池实现，事务实现  
[<博客主页](https://jeremieastray.github.io)  
  

transaction-demo项目链接:https://github.com/JeremieAstray/transaction-demo

## 模拟spring容器实现

基本数据结构

```
//单例容器（代理对象与非代理对象）
private static Map<String, Object> beanContainer = new HashMap<>();
private static Map<String, Object> dynamicBeanContainer = new HashMap<>();
```

beanContainer放置未经过代理的bean（这里是使用@Component、@Repository、@Service等注解的类）。

dynamicBeanContainer则是放置经过代理的bean（这里实现是使用@Transaction注解的类）

IOC逻辑如下：

1、扫描包，获取出要加载到spring容器的类

2、初始化所有的类，如果一个类需要处理为代理对象的，在初始化时同时处理

3、处理类的注入

扫描包的逻辑：

```
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
```

初始化类与注入的逻辑：

```
private static void IOC(List<Class> clazzList) throws IllegalAccessException, InstantiationException, ClassNotDeclearException {
    for (Class clazz : clazzList) {
        if (canIOC(clazz)) {
            //初始化对象
            Object instance = clazz.newInstance();
            beanContainer.put(clazz.getName(), instance);
            //处理代理对象
            if (clazz.isAnnotationPresent(Transaction.class)) {
                Transaction transaction = (Transaction) clazz.getAnnotation(Transaction.class);
                MethodInterceptor methodInterceptor = (MethodInterceptor) transaction.transactionDynamicClass().newInstance();
                Field[] fields = methodInterceptor.getClass().getDeclaredFields();
                for (Field f : fields) {
                    if (f.getName().equals("dynamicObject")) {
                        boolean accessible = f.isAccessible();
                        if (!accessible) {
                            f.setAccessible(true);
                        }
                        f.set(methodInterceptor, instance);
                        f.setAccessible(accessible);
                        break;
                    }
                }
                //创建代理类
                dynamicBeanContainer.put(clazz.getName(), Enhancer.create(instance.getClass(), methodInterceptor));
            }
        }
    }

    //处理注入
    for (Map.Entry<String, Object> entry : beanContainer.entrySet()) {
        Object o = entry.getValue();
        Field[] fieldList = entry.getValue().getClass().getDeclaredFields();
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
```

## 模拟连接池实现

连接池是创建和管理一个连接的缓冲池的技术，这些连接准备好被任何需要它们的线程使用。

基本数据结构

```
private Deque<T> poolBeanLinkedListPool = new ConcurrentLinkedDeque<>();
private Map<Integer, T> integerTMap = new HashMap<>();
```

Map是用于存储所有的连接对象，以id->对象的方式存储。

队列则是用户连接池获取连接和释放连接，确保每条连接被获取后不能重复获取。

初始化连接池，调用对象的工厂方法初始化连接:

```
private void newPoolBeans(int size) {
    this.size.addAndGet(size);
    for (int i = 0; i < size; i++) {
        T connection = this.poolBeanFactory.init();
        this.poolBeanLinkedListPool.add(connection);
        this.integerTMap.put(connection.getId(), connection);
    }
}
```

获取连接的方法（每次只能有一条线程调用），从队列中获取:

```
public synchronized T getConnection() {
    if (this.poolBeanLinkedListPool.isEmpty()) {
        if (this.size.get() < MAX_SIZE) {
            increasePoolSize();
        } else {
            while (this.poolBeanLinkedListPool.isEmpty()) {
                //暂时设定等待一会
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //取出连接池中一个连接
    T result = this.poolBeanLinkedListPool.removeFirst(); // 删除第一个连接返回
    return result;
}
```

使用完连接后，释放连接，放回到队列中：

```
//将连接放回连接池
public void releaseConnection(int id) {
    if (integerTMap.get(id) != null) {
        this.poolBeanLinkedListPool.add(integerTMap.get(id));
    }
}
```

## 模拟事务实现

事务是逻辑处理原子性的保证手段，通过使用事务控制，可以极大的避免出现逻辑处理失败导致的脏数据等问题。

这里使用代理服务类来处理事务，使用注解的方式@Transaction来确认要代理的类，然后在调用该服务类的方法前后加上事务处理的代码。开启，提交和回滚事务。

难点：

1、每条线程确保用的是同一个连接，这样子开启事务、提交事务和回滚事务都是在同一个连接进行的。

这里我使用了ThreadLocal来确定这个线程用的是同一个连接

2、事务传播处理，如果服务类的方法之间存在互相调用或者递归调用，事务就需要进行传递处理。

实现：使用一个Stack，存放调用的方法链，直到方法调用之后这个Stack为空时，即可以提交或者回滚事务。

ps:同样，这个也是使用ThreadLocal确定一条线程有唯一一个方法栈

spring的事务传播如下，这个demo只实现了PROPAGATION_REQUIRED.

```
1）PROPAGATION_REQUIRED ，默认的spring事务传播级别，使用该级别的特点是，如果上下文中已经存在事务，那么就加入到事务中执行，如果当前上下文中不存在事务，则新建事务执行。
2）PROPAGATION_SUPPORTS ，从字面意思就知道，supports，支持，该传播级别的特点是，如果上下文存在事务，则支持事务加入事务，如果没有事务，则使用非事务的方式执行。
3）PROPAGATION_MANDATORY ， 该级别的事务要求上下文中必须要存在事务，否则就会抛出异常！配置该方式的传播级别是有效的控制上下文调用代码遗漏添加事务控制的保证手段。
4）PROPAGATION_REQUIRES_NEW ，从字面即可知道，new，每次都要一个新事务，该传播级别的特点是，每次都会新建一个事务，并且同时将上下文中的事务挂起，执行当前新建事务完成以后，上下文事务恢复再执行。
5）PROPAGATION_NOT_SUPPORTED ，这个也可以从字面得知，not supported ，不支持，当前级别的特点就是上下文中存在事务，则挂起事务，执行当前逻辑，结束后恢复上下文的事务。
6）PROPAGATION_NEVER ，该事务更严格，上面一个事务传播级别只是不支持而已，有事务就挂起，而PROPAGATION_NEVER传播级别要求上下文中不能存在事务，一旦有事务，就抛出runtime异常，强制停止执行！
7）PROPAGATION_NESTED ，字面也可知道，nested，嵌套级别事务。该传播级别特征是，如果上下文中存在事务，则嵌套事务执行，如果不存在事务，则新建事务。
```

代理service类TransactionDynamicHandler

```
public class DynamicService extends BaseDynamicService implements MethodInterceptor {

    private Object dynamicObject;

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        this.before(method);
        Object result;
        try {
            result = method.invoke(this.dynamicObject, objects);
            this.after();
            return result;
        } catch (Exception e) {
            this.exception(e);
            throw e;
        }
    }
}
```

基本的事务操作

```
public interface TransactionHandler {

    void before(Method method);

    void after();

    void exception(Exception e) throws Exception;
}

```

PROPAGATION_REQUIRED实现
```
public class PropagationRequiredTransactionDynamicHandler extends TransactionDynamicHandler {

    private static ThreadLocal<Stack<Method>> methodStack = ThreadLocal.withInitial(Stack::new);


    //PROPAGATION_REQUIRED事务处理方式简单实现
    public void before(Method method) {
        if (methodStack.get().isEmpty()) {
            ApplicationContext.connectionThreadLocal.get().startTransaction();
        }
        methodStack.get().push(method);
    }

    public void after() {
        methodStack.get().pop();
        if (methodStack.get().empty()) {
            ApplicationContext.connectionThreadLocal.get().commit();
            ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
            ApplicationContext.connectionThreadLocal.remove();
        }
    }

    public void exception(Exception e) throws Exception {
        e.printStackTrace();
        methodStack.get().pop();
        if (methodStack.get().empty()) {
            ApplicationContext.connectionThreadLocal.get().rollBack();
            ApplicationContext.connectionPool.releaseConnection(ApplicationContext.connectionThreadLocal.get().getId());
            ApplicationContext.connectionThreadLocal.remove();
        }
    }
}
```



## 总结

完成这一系列的编码以后，发现spring的context不是那么简单。这个模拟只能模拟到spring的一些皮毛，只是初尝ioc的写法，并不足以应用于生产环境。

不过这些让我熟悉了spring的ioc，事务的处理，对以后程序编码能有很大帮助。

transaction-demo项目链接:https://github.com/JeremieAstray/transaction-demo
