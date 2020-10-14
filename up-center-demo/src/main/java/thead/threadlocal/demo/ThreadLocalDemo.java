package thead.threadlocal.demo;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-09-07
 * Time: 11:26
 *
 */

import com.dayday.up.my.thread.threadpoolutil.ThreadPoolsUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class ThreadLocalDemo {

    private static final int CONCURRENT = 1000;

    private static  ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println(this.toString() + "is gc !");
        }
    };

    private static  CountDownLatch latch = new CountDownLatch(CONCURRENT);

    public static class ParseDate implements Runnable {
        int i ;

        public ParseDate(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
                if (threadLocal.get() == null) {
                    threadLocal.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") {
                        @Override
                        protected void finalize() throws Throwable {
                            super.finalize();
                            System.out.println(this.toString() + " is gc !");
                        }
                    });
                    System.out.println(Thread.currentThread().getId() + " created SimpleDateFormat ! ");
                }
                Date parseDate = threadLocal.get().parse("2018-07-25 23:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executorService = ThreadPoolsUtil.executorServiceWithFactory;
        for (int i = 0; i < 1000; i++) {
            executorService.execute(new ParseDate(i));
        }
        latch.await();
        System.out.println(" mission complete ！");
        threadLocal = null;
        System.gc();
        System.out.println(" first gc is complete ! ");

        threadLocal = new ThreadLocal<SimpleDateFormat>();
        latch = new CountDownLatch(CONCURRENT);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(new ParseDate(i));
        }
        latch.await();
        Thread.sleep(1000);
        System.gc();
        System.out.println(" second gc is complete ！ ");
    }

}