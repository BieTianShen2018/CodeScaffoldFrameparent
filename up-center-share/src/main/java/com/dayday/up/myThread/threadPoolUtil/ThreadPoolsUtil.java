package com.dayday.up.myThread.threadPoolUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-09-07
 * Time: 11:38
 */
public class ThreadPoolsUtil {
    /** 线程池维护的线程数量，即使其中有闲置线程 **/
    public  static final int CORE_POOL_SIZE=10;
    /**  线程池能容纳的最大线程数量 **/
    public  static final int MAX_POOL_SIZE=15;
    /** 当前线程数量超出CORE_POOL_SIZE时，过量线程在开始任务前的等待时间，超时将被关闭 **/
    public  static final int KEEP_ALIVE_TIME=5000;
    /**  KEEP_ALIVE_TIME的单位 **/
    public  static final int QUEUE_CAPACITY=10;

    /**使用阿里巴巴推荐的创建线程池的方式 通过ThreadPoolExecutor构造函数自定义参数创建 **/
    public static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                /** 持有被execute/submit提交的Runnable任务，QUEUE_CAPACITY为队列长度 **/
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                /** // new ThreadPoolExecutor.CallerRunsPolicy()是一个RejectedExecutorHandler
                // RejectedExecutionHandler：当线程池不能执行提交的线程任务时使用的策略
                //  -DiscardOldestPolicy：丢弃最先提交到线程池的任务
                //  -AbortPolicy： 中断此次提交，并抛出异常
                //  -CallerRunsPolicy： 主线程自己执行此次任务
                //  -DiscardPolicy： 直接丢弃此次任务，不抛出异常
                 // 当执行被阻塞时要使用的处理程序,因为达到了线程界限和队列容量 **/
                new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        List<Future<String>> futureList = new ArrayList<>();
        Callable<String> callable = new MyCallable();
        for (int i = 0; i < 10; i++) {
            //提交任务到线程池
            Future<String> future = executor.submit(callable); // Callable任务要使用submit提交，会返回一个Future对象
            //将返回值 future 添加到 list，我们可以通过 future 获得 执行 Callable 得到的返回值
            futureList.add(future);
        }
        for (Future<String> fut : futureList) {
            try {
                System.out.println(new Date() + "::" + fut.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        //关闭线程池
        executor.shutdown();
    }
}
class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        return Thread.currentThread().getName();
    }
}

