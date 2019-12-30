package com.company;

public class SynchronizedThread implements Runnable{
    int num = 10;

    public static void main(String[] args) {
            for (int i = 0; i < 5; i++) {
                new Thread(new SynchronizedThread()).start();
            }

    }

    /**给实现的Runable方法加锁（父接口的方法也是可以加锁的！）
     * synchronized是对象锁，被其修饰的代码块或者方法都是一个个原子。原子是程序执行的最小单位，不可拆分，必须等原子执行完毕才能够继续执行，否则等待
     */
    @Override
    public synchronized void run() {
        for (int j=0;j<5;j++) {
            num--;

            System.out.println(num);
        }
    }

}



