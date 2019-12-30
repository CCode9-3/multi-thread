package com.company;

public class TestVolatile {

    volatile public static int count = 0;

    public static void inc() {

        //这里延迟1毫秒，使得结果明显
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }

        count++;
    }

    public static void main(String[] args) {

        //同时启动1000个线程，去进行i++计算，看看实际结果

        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TestVolatile.inc();
                }
            }).start();
        }

        //这里我们休眠10秒钟，使得我们输出语句执行的时候所有子线程均已经执行完成
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //这里每次运行的值都有可能不同,可能为1000
        System.out.println("运行结果:Counter.count=" + TestVolatile.count);
    }
}
