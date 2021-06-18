package top.codecrab.gulimall.search.thread;

import java.util.concurrent.*;

/**
 * @author codecrab
 * @since 2021年06月17日 17:26
 */
public class ThreadTest {

    public static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);

    /**
     * CompletableFuture异步任务编排，和js的Promise类似
     * <ol>
     *     <li>使用CompletableFuture传入一个线程池执行</li>
     *     <p>
     *         //使用supplyAsync方法可以获得异步执行返回值，阻塞式
     *         CompletableFuture.runAsync(() -> {
     *             System.out.println("当前线程ID：" + Thread.currentThread().getId());
     *             int i = 10 / 5;
     *             System.out.println(i);
     *         }, THREAD_POOL);
     *     </p>
     * </ol>
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main start....");

        /*
        //任务返回值和异常情况处理返回值分开操作
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程ID：" + Thread.currentThread().getId());
            int i = 10 / 0;
            System.out.println(i);
            return i;
        }, THREAD_POOL).whenComplete((result, exception) -> {
            //这种方法虽然可以感知异常，但是无法修改返回值，阻塞式
            System.out.println("异步任务执行完毕。结果：" + result + "\t异常：" + exception);
        }).exceptionally((throwable -> {
            //同样可以感知异常，这个方法可以修改返回值，无异常不会进来
            System.out.println("感知到异常：" + throwable);
            return 10;
        }));
        Integer i = future.get();
        */
        /*
        //同时操作任务返回值和异常返回值
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程ID：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("任务运行结果：" + i);
            return i;
        }).handle((res, exe) -> {
            System.out.println("上一步执行结果：" + res + "\t异常为：" + exe);
            return res * 2;
        });
        Integer i = future.get();
        */

        /*
        线程串行化，执行完一个紧接着执行第二个
        第一种：
        .thenRunAsync(() -> {
            System.out.println("第二个任务执行了。线程ID：" + Thread.currentThread().getId());
        }, THREAD_POOL);

        第二种：
        .thenAcceptAsync((res) -> {
            System.out.println("第二个任务执行了。第一步任务结果：" + res + "。线程ID：" + Thread.currentThread().getId());
        }, THREAD_POOL);

        第三种：
        .thenApplyAsync(res -> {
            System.out.println("第三个任务执行了。第二步任务结果：" + res + "。线程ID：" + Thread.currentThread().getId());
            return "上一步结果修改：" + res;
        }, THREAD_POOL)
         */
        /*
        CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程ID：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("任务运行结果：" + i);
            return i;
        }, THREAD_POOL).thenApplyAsync(res -> {
            System.out.println("第二个任务执行了。第一步任务结果：" + res + "。线程ID：" + Thread.currentThread().getId());
            return res * 2;
        }, THREAD_POOL).thenApplyAsync(res -> {
            System.out.println("第三个任务执行了。第二步任务结果：" + res + "。线程ID：" + Thread.currentThread().getId());
            return "上一步结果修改：" + res;
        }, THREAD_POOL).thenAcceptAsync(res -> {
            System.out.println("最终效果：" + res);
        }, THREAD_POOL);
        */
        /*
        //两任务组合都要完成
        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1当前线程ID：" + Thread.currentThread().getId());
            int i = 10 / 2;
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务1结束。。。");
            return i;
        }, THREAD_POOL);

        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2当前线程ID：" + Thread.currentThread().getId());
            System.out.println("任务2结束。。。");
            return "Hello";
        }, THREAD_POOL);
        */
        /*
        //等前两个组合的任务完成，执行第三个任务，无法接收上几步的返回值
        future01.runAfterBothAsync(future02, () -> {
            System.out.println("任务三开始了");
        }, THREAD_POOL);
        */
        /*
        future01.thenAcceptBothAsync(future02, (f1, f2) -> {
            System.out.println("任务三开始了。第一步返回值：" + f1 + "\t第二步返回值：" + f2);
        }, THREAD_POOL);
        */
        /*
        CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
            System.out.println("任务三开始了。第一步返回值：" + f1 + "\t第二步返回值：" + f2);
            return "第三步处理了返回值：" + f1 + ":" + f2;
        }, THREAD_POOL);
        System.out.println(future.get());
        */
        /*
        //两个任务组合，只要有一个完成就执行第三个任务，任务返回参数最好有共同的接口或父类，否则程序因为不确定是哪一个任务先执行完毕，无法确定返回值是什么而编译期报错
        future01.runAfterEitherAsync(future02, () -> {
            System.out.println("第三个任务执行");
        }, THREAD_POOL);
        */
        /*
        future01.acceptEitherAsync(future02, res -> {
            System.out.println("第三个任务执行，上一步结果：" + res);
        }, THREAD_POOL);
        */
        /*
        //R apply(T t);
        CompletableFuture<String> future = future01.applyToEitherAsync(future02, res -> {
            System.out.println("第三个任务执行，上一步结果：" + res);
            return "第三个任务拼接返回值：" + res;
        }, THREAD_POOL);
        System.out.println(future.get());
        */

        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("查询商品属性完成");
            return "8GB+256GB";
        }, THREAD_POOL);

        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("查询商品图片完成");
            return "xiaomi.jpg";
        }, THREAD_POOL);

        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("查询商品摘要完成");
            return "小米的手机";
        }, THREAD_POOL);

        //CompletableFuture<Void> allOf = CompletableFuture.allOf(futureAttr, futureImg, futureDesc);
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureAttr, futureImg, futureDesc);
        long l = System.currentTimeMillis();
        //阻塞，等待包含的线程全部执行完毕
        //allOf.get();
        //阻塞，其中有一个线程完成任务就开始下一个
        Object o = anyOf.get();
        System.out.println("执行耗时：" + (System.currentTimeMillis() - l) + "ms");
        //此时直接调用其他任务的get方法就不会被阻塞，因为都已经完成执行拿到值了
        //System.out.println("结果：" + futureAttr.get() + "=>" + futureImg.get() + "=>" + futureDesc.get());
        System.out.println("结果：" + o);

        System.out.println("main end....");
    }

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
     *     <li style="color:green">推荐：创建一个全局的线程池，每个项目一两个就够了</li>
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
    public void thread() {
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
