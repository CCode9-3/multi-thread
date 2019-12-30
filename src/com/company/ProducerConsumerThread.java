/*面试题：写一个固定容量的同步容器，拥有put和get方法，以及getCount方法，能够支持2
*个生产者线程的阻塞调用
*
* ①使用wait和notifyAll来实现
*
* ②使用Lock和Condition来实现
* 对比两种方式，condition的方式可以更加精确的指定哪些线程被唤醒
*/

package com.company;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerThread<T> {
    private int count = 0;
    private final int MAX = 10;

    public final LinkedList<String> lists = new LinkedList<>();

    private Lock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    public void put(String t){
        try {
            lock.lock();
            //调用await，如果只是if的话，改线程被唤醒之后就会直接执行下面的语句，这时lists.size()可能已经为MAX了。为了避免这种情况，使用await/wait一定要和
            //while同时使用
            while (lists.size() ==MAX){ //await/wait的方法一定要和while一起使用！如果用if的话会出问题
                producer.await();
            }
            lists.add(t);
            ++count;
            consumer.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       finally {
            lock.unlock();
        }
    }
    public String get(){
        String t = null;
        try{
            lock.lock();
            while(lists.size()==0){
                consumer.await();
            }
            t=lists.removeFirst();
            --count;
            producer.signalAll(); //使用await/wait之后，一定要用signalAll或者notifyAll，effectiveJava有介绍！
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return t;
    }

    public static void main(String[] args){
        ProducerConsumerThread<String> c = new ProducerConsumerThread();
        for (int i=0;i<10;i++){
            new Thread(()-> {
                for (int j = 0; j < 10; j++) {
                    System.out.println(c.get());
                }
            }
                ,"c"+i).start();

        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i=0;i<2;i++){
            int finalI = i;
            new Thread(()->{
                for (int j=0;j<25;j++) {
                    c.put(Thread.currentThread().getName() + " " + finalI);
                }
            },"p").start();
        }

    }
}
