package PLC.EE2;
//Arthur Romaguera Lima

import java.util.LinkedList;

public class BlockingQueue<T> {
    private LinkedList<T> queue;
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        queue = new LinkedList<>();
    }

    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            // Se a fila estiver cheia, aguarde até que um elemento seja removido
            wait();
        }
        // Adicionar elemento à fila
        queue.add(item);
        // Notificar threads em espera que agora há elementos na fila
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            // Se a fila estiver vazia, aguarde até que um elemento seja adicionado
            wait();
        }
        // Remover e retornar o elemento da fila
        T item = queue.remove();
        // Notificar threads em espera que agora há espaço na fila
        notifyAll();
        return item;
    }
}
