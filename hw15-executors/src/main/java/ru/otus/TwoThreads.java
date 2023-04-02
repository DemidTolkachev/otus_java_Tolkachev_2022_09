package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwoThreads {
    private static final Logger logger = LoggerFactory.getLogger(TwoThreads.class);
    private String lastThread = "Thread2";

    private synchronized void action() {
        int count = 0;
        int increment = 1;
        String threadName;

        while (!Thread.currentThread().isInterrupted()) {
            try {
                threadName = Thread.currentThread().getName();

                while (lastThread.equals(threadName)) {
                    this.wait();
                }
                count += increment;
                System.out.printf("%s: %d\n", threadName, count);

                if (count == 1) {
                    increment = 1;
                } else if (count == 10) {
                    increment = -1;
                }
                lastThread = threadName;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        TwoThreads twoThreads = new TwoThreads();
        Thread thread1 = new Thread(() -> twoThreads.action());
        thread1.setName("Thread1");
        Thread thread2 = new Thread(() -> twoThreads.action());
        thread2.setName("Thread2");
        thread1.start();
        thread2.start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
