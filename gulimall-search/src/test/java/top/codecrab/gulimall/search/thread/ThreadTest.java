package top.codecrab.gulimall.search.thread;

import java.util.concurrent.*;

/**
 * @author codecrab
 * @since 2021年06月17日 17:26
 */
public class ThreadTest {

    /**
     * 开启线程的4种方式：
     * <ol>
     *     <li>继承Thread类</li>
     *     <ul>
     *         <li>业务逻辑：重写run方法</li>
     *         <li>运行：new Thread01().start();</li>
     *     </ul>
     *     <li>实现Runnable接口的run方法</li>
     *     <ul>
     *         <li>业务逻辑：实现run方法</li>
     *         <li>运行：new Thread(new Runnable01()).start();</li>
     *     </ul>
     *     <li>实现Callable接口，配合FutureTask完成。可以处理异常，接收返回值</li>
     *     <ul>
     *         <li>业务逻辑：实现call方法</li>
     *         <li>运行：
     *             <div>
     *                 FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
     *                 new Thread(futureTask).start();
     *                 Integer integer = futureTask.get();
     *                 System.out.println("futureTask.get()方法为阻塞式，必须等获得Callable01的返回值，下面代码才会运行。返回值：" + integer);
     *             </div>
     *         </li>
     *     </ul>
     *     <b style="color:red">以上三种均不推荐使用，因为当有大并发时，频繁的new Thread会导致内存耗尽，推荐使用线程池</b>
     *     <li style="color:green">推荐：使用线程池创建。最简单用法：Executors.newFixedThreadPool(5).execute(new Runnable01());</li>
     *     <p></p>
     *     <b style="color:rgb(187, 54, 131)">
     *         总结：
     *         <ul>
     *             <li>1、2不能接收返回值，3可以接收返回值</li>
     *             <li>1、2、3都不能达到资源控制</li>
     *             <li>线程池可以控制资源，性能稳定，先压力测试，测试出最高支持多少线程，就控制为多少</li>
     *         </ul>
     *     </b>
     * </ol>
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main start....");

        //创建5个线程的线程池，核心线程5个，最大5个
        Executors.newFixedThreadPool(5).execute(new Runnable01());

        System.out.println("main end....");
    }

    /**
     * 原生创建线程池的七大参数：
     * <ol>
     *     <li>
     *         <b>corePoolSize</b>：指定核心线程数，就算空闲也不会被回收。一旦创建，就准备就绪
     *     </li>
     *     <li>
     *          <b>maximumPoolSize</b>：线程池最大容量，控制资源
     *     </li>
     *     <li>
     *          <b>keepAliveTime</b>：只要线程空闲之后keepAliveTime时间，线程数大于corePoolSize就会被回收。回收的最大空闲线程数为 (maximumPoolSize - corePoolSize)
     *     </li>
     *     <li>
     *          <b>unit</b>：时间单位
     *     </li>
     *     <li>
     *          <b>BlockingQueue<Runnable> workQueue</b>：阻塞队列，如果需要执行的线程有很多，那就先把他们放入阻塞队列，一旦有空闲线程，就立即取出队列中的任务执行
     *     </li>
     *     <li>
     *          <b>threadFactory</b>：指定线程池创建线程使用的工厂。一般使用默认：Executors.defaultThreadFactory()，除非有特殊需求（指定线程名）
     *     </li>
     *     <li>
     *          <b>RejectedExecutionHandler handler</b>：线程阻塞队列满了，就按照指定的策略执行拒绝。可以丢弃新来的线程/最老的/同步执行...
     *     </li>
     * </ol>
     * <p></p>
     * <ul>
     *     注意：
     *     <li>new LinkedBlockingQueue<>()需要在构造函数里传入一个默认容量，否则会使用默认值 Integer.MAX_VALUE</li>
     *     <li>new ThreadPoolExecutor.CallerRunsPolicy()为同步执行的拒绝策略，超出阻塞队列的将同步执行</li>
     * </ul>
     * <ol>
     *     执行流程：
     *     <li>线程池创建，立即初始化10个线程。准备执行任务</li>
     *     <li>corePoolSize满了，就把再进来的任务放入阻塞队列，一旦corePoolSize空闲，就从阻塞队列取任务执行</li>
     *     <li>阻塞队列满了，就直接开新线程执行，最多开到maximumPoolSize指定的数量</li>
     *     <li>maximumPoolSize满了，就按照RejectedExecutionHandler拒绝策略执行</li>
     *     <li>maximumPoolSize执行完毕，空闲的线程等待keepAliveTime时间后被回收（core线程除外）</li>
     * </ol>
     *
     * <p>
     *     <b>问：如果有10个core，max为30，queue为50，进来了200线程，是如何分配的。</b>
     * </p>
     * <p>
     *     <b>答：其中10个立即被执行，50个放入队列，再开20个线程进行执行，剩下的拒绝执行，如果还想被执行，可以使用ThreadPoolExecutor.CallerRunsPolicy同步调用</b>
     * </p>
     */
    public void ThreadPool() {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                10,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        //创建Integer.MAX_VALUE大小的最大数量的线程，core是0，所有的线程都可以被回收，灵活
        //Executors.newCachedThreadPool()
        //创建指定大小的线程池，core==max，都不可以被回收
        //Executors.newFixedThreadPool()
        //可以指定核心线程数的线程池，主要用于定时任务
        //Executors.newScheduledThreadPool()
        //单线程的线程池，后台从队列里边获取任务，挨个执行
        //Executors.newSingleThreadExecutor()
    }

    public static class Thread01 extends Thread {

        @Override
        public void run() {
            System.out.println("当前线程ID：" + Thread.currentThread().getId());
            int i = 10 / 5;
            System.out.println(i);
        }
    }

    public static class Runnable01 implements Runnable {

        @Override
        public void run() {
            System.out.println("当前线程ID：" + Thread.currentThread().getId());
            int i = 10 / 5;
            System.out.println(i);
        }
    }

    public static class Callable01 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程ID：" + Thread.currentThread().getId());
            int i = 10 / 5;
            System.out.println(i);
            return i;
        }
    }
}
