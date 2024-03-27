package PLC.EE2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class myProdutorConsumidor {
    //declarar os atributos da classe
    private int atributo1 = 0;
    private int atributo2 = 1;
    //recurso compartilhado
    private BlockingQueue<Integer> queue;

    public myProdutorConsumidor() {
        queue = new LinkedBlockingQueue<>(atributo2);
    }

    public void startProgram() {
        Thread produtorThread = new Thread(new Produtor());
        Thread consumerThread = new Thread(new Consumer());
        produtorThread.start();
        consumerThread.start();
    }

    class Produtor implements Runnable {
        @Override
        public void run() {
            try {
                int batchNumber = 1;
                while (true) {
                    for (int i = 1; i <= atributo1; i++) {
                        queue.put(batchNumber);
                        System.out.println("Batch " + batchNumber + " placed in queue");
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
                    for (int i = 1; i <= atributo1; i++) {
                        Integer batchNumber = queue.take();
                        System.out.println("Batch " + batchNumber + " removed from queue");
                        // Simulate baking time
                        Thread.sleep(300);
                    }
                    System.out.println("Queue emptied. Waiting for replenishment.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    
}
