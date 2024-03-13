import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<T> {
    private Queue<T> queue;
    private int capacity;
    private Lock lock = new ReentrantLock(); // Lock para garantir a sincronização entre threads
    private Condition notFull = lock.newCondition(); // Condição para aguardar a fila não estar cheia
    private Condition notEmpty = lock.newCondition(); // Condição para aguardar a fila não estar vazia

    // Construtor que inicializa a fila bloqueante com capacidade máxima fornecida
    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        queue = new LinkedList<>();
    }

    // Método para inserir um elemento na fila, bloqueando se a fila estiver cheia
    public void put(T element) throws InterruptedException {
        lock.lock(); // Bloqueia o acesso concorrente à fila
        try {
            while (queue.size() == capacity) { // Enquanto a fila estiver cheia
                notFull.await(); // Aguarda até que a condição (fila não cheia) seja satisfeita
            }
            queue.add(element); // Adiciona o elemento na fila
            notEmpty.signal(); // Sinaliza que a fila não está vazia
        } finally {
            lock.unlock(); // Libera o lock
        }
    }

    // Método para remover um elemento da fila, bloqueando se a fila estiver vazia
    public T take() throws InterruptedException {
        lock.lock(); // Bloqueia o acesso concorrente à fila
        try {
            while (queue.isEmpty()) { // Enquanto a fila estiver vazia
                notEmpty.await(); // Aguarda até que a condição (fila não vazia) seja satisfeita
            }
            T element = queue.poll(); // Remove e retorna o elemento da fila
            notFull.signal(); // Sinaliza que a fila não está cheia
            return element;
        } finally {
            lock.unlock(); // Libera o lock
        }
    }

    public static void main(String[] args) {
        // Criando uma fila bloqueante com capacidade máxima de 3 elementos
        BlockingQueue<Integer> queue = new BlockingQueue<>(3);

        // Criando e iniciando duas threads produtoras
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 5; j++) {
                        int element = j + 1;
                        queue.put(element); // Adicionando elemento à fila
                        System.out.println("Produtor " + Thread.currentThread().threadId() + " colocou: " + element);
                        Thread.sleep(1000); // Simulando um processo de produção
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // Criando e iniciando uma thread consumidora
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int element = queue.take(); // Removendo elemento da fila
                    System.out.println("Consumidor " + Thread.currentThread().threadId() + " removeu: " + element);
                    Thread.sleep(2000); // Simulando um processo de consumo
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }







}