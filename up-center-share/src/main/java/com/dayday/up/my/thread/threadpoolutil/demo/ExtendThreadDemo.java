package com.dayday.up.my.thread.threadpoolutil.demo;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-09-25
 * Time: 14:47
 * 这边可以看出每个线程都有5张票。并没有资源共享。
 * 不同线程实现了  不能共享线程资源, 不同的线程id
 */
public class ExtendThreadDemo extends Thread {
    private int ticket = 5;

    public ExtendThreadDemo(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            sale();
        }
    }

    /**使用同步方法
     *
     */
    public  void sale() {
        if (this.ticket > 0) {
            System.out.println(Thread.currentThread().getName()
                    + "卖票：1张 " + this.ticket--+" 线程id:"+Thread.currentThread().getId());
        }
    }

    public static void main(String[] args) {
//        MyThread mt = new MyThread();
        System.out.println("主线程ID:"+Thread.currentThread().getId());
        Thread thread1 = new ExtendThreadDemo("售票口一");
        Thread thread2 = new ExtendThreadDemo("售票口二");
        Thread thread3 = new ExtendThreadDemo("售票口三");
        //thread1.run();和主线程一致 没有启动新线程
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
