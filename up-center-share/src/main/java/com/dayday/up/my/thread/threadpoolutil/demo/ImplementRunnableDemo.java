package com.dayday.up.my.thread.threadpoolutil.demo;

import lombok.SneakyThrows;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-09-25
 * Time: 14:51
 * 实现资源共享 但是存在多卖超卖
 *  * 线程同步方法
 *  * 1  wait方法
 *  * 2  notify方法和notifyAll方法
 *  * 3  synchronized关键字
 *  * 4 atomic action（原子操作）
 *
 * 使用实现Runnable接口的方式创建的线程可以处理同一资源，从而实现资源的共享
 * 不同线程实现了线程资源共享(实现同一个runnable接口, 资源在实现里面)
 *
 *  结果:
 *  售票口一卖票： 5
 * 售票口三卖票： 4
 * 售票口三卖票： 3
 * 售票口三卖票： 2
 * 售票口三卖票： 1
 */
public class ImplementRunnableDemo implements Runnable {

    private int ticket = 5;

    @SneakyThrows
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            sale();
        }
    }

    /**使用同步方法
     *
     * @throws InterruptedException
     */
    public synchronized void  sale() throws InterruptedException {
        if (this.ticket > 0) {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName()
                    + "卖票： " + this.ticket-- +" 线程id:"+Thread.currentThread().getId());
        }
    }

    public static void main(String[] args) {
        ImplementRunnableDemo mt = new ImplementRunnableDemo();
        Thread thread1 = new Thread(mt, "售票口一");
        Thread thread2 = new Thread(mt, "售票口二");
        Thread thread3 = new Thread(mt, "售票口三");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}