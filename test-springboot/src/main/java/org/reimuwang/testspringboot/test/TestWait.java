package org.reimuwang.testspringboot.test;

import java.util.concurrent.locks.ReentrantLock;

public class TestWait {

    public static void main(String[] args) {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();
        Runnable r1 = () -> {
            lock1.lock();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.lock();
            lock2.unlock();
            lock1.unlock();
        };
        Runnable r2 = () -> {
            lock2.lock();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock1.lock();
            lock1.unlock();
            lock2.unlock();
        };
        new Thread(r1, "r1").start();
        new Thread(r2, "r2").start();
    }
}
