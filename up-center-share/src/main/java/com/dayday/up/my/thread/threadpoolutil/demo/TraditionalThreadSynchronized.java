package com.dayday.up.my.thread.threadpoolutil.demo;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-09-29
 * Time: 17:32
 *
 * 线程不安全 修改同一线程之外的对象资源
 *
 */
public class TraditionalThreadSynchronized {

    /**
     * @param args
     */
    public static void main(String[] args) {
        new TraditionalThreadSynchronized().init();
    }

    private void init() {
        //此方法同时启动两个线程，去调用同一个方法的打印名字
        final Outputer outputer = new Outputer();
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    try {
                        System.out.println("666 init --- new线程ID:"+Thread.currentThread().getId());
                        outputer.setName("666");
                        Thread.sleep(1000);
                        outputer.output();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //outputer.output("zhangxiaoxiang new线程ID:"+Thread.currentThread().getId());
                }
            }
        }).start();

        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    try {
                       System.out.println("888 ---init new线程ID:"+Thread.currentThread().getId());
                        outputer.setName("888");
                        Thread.sleep(1000);
                        outputer.output();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //outputer.output("lihuoming "+"new线程ID:"+Thread.currentThread().getId());
                }

            }
        }).start();

    }

    static class Outputer{
       private String  name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void output(){
//			synchronized (Outputer.class)
//			{
            System.out.println("name:"+this.name+"   线程ID:"+Thread.currentThread().getId());
//			}
        }

    }
}
