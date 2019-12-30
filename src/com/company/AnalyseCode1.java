package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AnalyseCode1 {
    List list = new ArrayList();

    public void add(Object o){
        list.add(o);
    }
    public int size(){
        return list.size();
    }

    public static void main(String[] args){
        AnalyseCode1 c = new AnalyseCode1();

        final Object lock = new Object();

        new Thread(()->{
            synchronized (lock){
                System.out.println("t2启动");
                if (c.size() !=5){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("t2结束");
                //通知t1继续执行
                lock.notify();
            }
        },"t2").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            System.out.println("t1启动");
            synchronized (lock){
                for (int i=0;i<10;i++){
                    c.add(new Object());
                    System.out.println("add"+i);
                    if (c.size() ==5){

                            lock.notify(); //唤醒线程t2，但是此时t2是没有lock这把锁的，t2还是无法运行，所以需要线程t1释放这把锁
                        try {
                            lock.wait(); //释放锁
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"t1").start();
    }
}
