package com.dayday.up.my.thread.threadpoolutil.demo;


/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-09-22
 * Time: 12:27
 */
public class ThreadJoinDemo {

    public static void main(String[] args) throws InterruptedException {
        Worker runnableJob1 = new Worker(null);

        Thread t1 = new Thread(runnableJob1, "老二");
        //Worker runnableJob2 = new Worker(t1);
        Thread t2 = new Thread(runnableJob1, "老大");
        //Worker runnableJob3 = new Worker(t2);
        Thread t3 = new Thread(runnableJob1, "老爸");
        t1.start();
        //
        t2.start();
        //t2.join();
        t3.start();
        t1.join();
        System.out.println("等待t1执行完毕----");
        t2.join();
        System.out.println("等待t2执行完毕----");
        t3.join();
        System.out.println("等待t3执行完毕----");
        System.out.println("主线程执行完毕----");
    }

}

class Worker implements Runnable{
    public Worker(Thread threadPre) {
        this.threadPre = threadPre;
    }

    public void setThreadPre(Thread threadPre) {
        this.threadPre = threadPre;
    }

    public Thread getThreadPre() {
        return threadPre;
    }

    public Thread threadPre=null;

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        try {
            System.out.println(thread.getName()+"kaishi吃了");

            Thread.sleep(5000);
            if(threadPre !=null){
               // threadPre.join();
            }
            System.out.println(thread.getName()+"吃完了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
