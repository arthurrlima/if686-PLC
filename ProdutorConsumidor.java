package PLC.EE2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProdutorConsumidor {
    private final int ovenCapacity = 50;
    private final int replenishAmount = 10;
    private BlockingQueue<Integer> oven;

    public ProdutorConsumidor() {
        oven = new LinkedBlockingQueue<>(ovenCapacity);
    }

    public void startBaking() {
        Thread bakerThread = new Thread(new Baker());
        Thread consumerThread = new Thread(new Consumer());
        bakerThread.start();
        consumerThread.start();
    }

    class Baker implements Runnable {
        @Override
        public void run() {
            try {
                int batchNumber = 1;
                while (true) {
                    for (int i = 1; i <= replenishAmount; i++) {
                        oven.put(batchNumber);
                        System.out.println("Batch " + batchNumber + " placed in oven");
                    }
                    batchNumber++;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    for (int i = 1; i <= replenishAmount; i++) {
                        Integer batchNumber = oven.take();
                        System.out.println("Batch " + batchNumber + " removed from oven");
                        // Simulate baking time
                        Thread.sleep(300);
                    }
                    System.out.println("Oven emptied. Waiting for replenishment.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        ProdutorConsumidor bakery = new ProdutorConsumidor();
        bakery.startBaking();
    }
}
