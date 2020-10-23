

### 1.并发优缺点
* 优点：多核CPU的计算能力和业务拆分
* 缺点：引起并发问题，安全性问题、活跃性问题（死锁、饥饿、活锁）、性能问题（上下文切换）

并发三要素：原子性、可见性（volatile、synchronized）、有序性。

进程是操作系统资源分配的基本单位，而线程是处理器任务调度和执行的基本单位

### 2.死锁
死锁是指两个或两个以上的进程（线程）在执行过程中，由于竞争资源或者由于彼此通信而造成的一种阻塞的现象，
若无外力作用，它们都将无法推进下去。此时称系统处于死锁状态或系统产生了死锁，这些永远在互相等待的进程
（线程）称为死锁进程（线程）。

死锁条件：
* 互斥资源
* 请求与保持，阻塞对获得资源保持不放
* 不剥夺，只有自己使用完毕才会释放资源
* 循环等待，当死锁发生，所等待线程形成环路，造成永久阻塞

### 3.创建线程方式（6种）
#### 1.继承`Thread`类，重写`run()`方法为线程的业务逻辑，`start()`启动线程
```Java
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " run()方法正在执行...");
    }
}
public class TheadTest {

    public static void main(String[] args) {
        MyThread myThread = new MyThread(); 	
        myThread.start();
        System.out.println(Thread.currentThread().getName() + " main()方法执行结束");
    }

}
```
#### 2.实现`Runnable`接口
```Java
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " run()方法执行中...");
    }
}
```
#### 3.匿名内部类
`new Thread(()->print("11"));`
#### 4.实现`Callable<V>`接口带返回值
* 创建实现Callable接口的类myCallable
* 以myCallable为参数创建FutureTask对象
* 将FutureTask作为参数创建Thread对象
* 调用线程对象的start()方法
```Java
public class MyCallable implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println(Thread.currentThread().getName() + " call()方法执行中...");
        return 1;
    }

}
public class CallableTest {

    public static void main(String[] args) {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new MyCallable());
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            Thread.sleep(1000);
            System.out.println("返回结果 " + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " main()方法执行完成");
    }

}
```
#### 5.定时器`Timer`
`Timer timer = new Timer(); timer.schedule(new TimerTask(), ,);`
#### 6.线程池`Executors`工具类
`newFixedThreadPool, newCachedThreadPool, newSingleThreadExecutor, newScheduledThreadPool`四种线程池，都实现
了`ExecutorService`接口。
```Java
public class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " run()方法执行中...");
    }

}

public class SingleThreadExecutorTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        MyRunnable runnableTest = new MyRunnable();
        for (int i = 0; i < 5; i++) {
            executorService.execute(runnableTest);
        }

        System.out.println("线程任务开始执行");
        executorService.shutdown();
    }

}
```
`Callable, Runnable`接口的区别：
* 相同点：都是接口，都可编写多线程，都采用Thread.run()启动
* 不同点：`Callable`接口`call()`有返回值，是个泛型，和`Future, FutureTask`配合获取执行结果；
可以获取异常信息。

为什么我们调用`start()`方法时会执行`run()`方法，为什么我们不能直接调用`run()`方法？

`run()`方法只是线程体中一个普通方法，当线程运行时才会执行次业务方法，`start()`方法启动线程，
使得线程由创建进入就绪状态，竞争CPU资源，当获取时间片时就可以开始执行进入运行状态。

### 4.线程状态
新建`new`、就绪(start())、运行(获取时间片cpu资源)、超时等待(sleep(),join()自动唤醒)、等待(wait()需要notify()唤醒)、阻塞(synchronized, blockIO获取锁资源)、结束	
采用线程调度（分时或者抢占式）。

### 5.方法
* `wait()`：线程阻塞，释放所持有对象锁
* `sleep()`：睡眠，进入超时等待状态，不释放锁
* `notify()`：唤醒一个等待线程，随机
* `notifyAll()`：唤醒所有等待线程，进行竞争
* Thread.join()方法：在主线程中调用线程A的join方法进行同步，等待A线程执行完毕才会进行主线程

`sleep(),wait()`区别：前者Thread方法后者Object方法，前者不释放锁，wait常用于线程通信，而sleep用于暂停执行，进入状态不同。

万物皆对象，对象皆为锁
### 6.线程安全性问题的条件
* 多线程环境
* 多个线程共享一个资源，竞争
* 对资源进行非原子操作

### 7.synchronized（当前对象锁）重量级锁
修饰静态方法锁.class文件--monitor,monitorexit
```Java
//双重校验锁实现对象单例（线程安全）
//在多个线程试图在同一时间创建对象时，会通过加锁来保证只有一个线程能创建对象。
//在对象创建好之后，执行getInstance()将不需要获取锁，直接返回已创建好的对象。
public class Singleton {

    private volatile static Singleton uniqueInstance; //volatile禁止指令重排序

    private Singleton() {
    }

    public static Singleton getUniqueInstance() {
       //先判断对象是否已经实例过，没有实例化过才进入加锁代码
        if (uniqueInstance == null) {
            //类对象加锁
            synchronized (Singleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }
}
```
#### 可重入
重入锁是指一个线程(Thread.currentThread() == pThread)获取到该锁之后，该线程可以继续获得该锁。底层原理维护一个计
数器，当线程获取该锁时，计数器加一，再次获得该锁时继续加一，释放锁时，计数器
减一，当计数器值为0时，表明该锁未被任何线程所持有，其它线程可以竞争获取锁。
#### 自旋锁
很多 synchronized 里面的代码只是一些很简单的代码，执行时间非常快，此时等待的线
程都加锁可能是一种不太值得的操作，因为线程阻塞涉及到用户态和内核态切换的问题。
既然 synchronized 里面的代码执行得非常快，不妨让等待锁的线程不要被阻塞，而是在 synchronized 的边界做忙循环，这就是自旋。如果做了多次循环发现还没有获得锁，再阻塞，这样可能是一种更好的策略。
```Java
public class SpinLock {
    private AtomicReference<Thread> owner =new AtomicReference<>();
    public void lock(){
        Thread current = Thread.currentThread();
        while(!owner.compareAndSet(null, current)){ //自旋操作
        }
    }
    public void unlock (){
        Thread current = Thread.currentThread();
        owner.compareAndSet(current, null);
    }
}
```
### 8.volatile
#### 可见性（线程A知道线程B修改了变量）
volatile,synchronized,wait/notify（通信）,while轮询
#### synchronized、volatile、CAS 比较
* synchronized 是悲观锁，属于抢占式，会引起其他线程阻塞。
* volatile 提供多线程共享变量可见性和禁止指令重排序优化。
* CAS 是基于冲突检测的乐观锁（非阻塞）