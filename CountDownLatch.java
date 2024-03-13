import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownLatch {
    private int count;
    private final Lock lock = new ReentrantLock(); // Lock para garantir a sincronização entre threads
    private final Condition condition = lock.newCondition(); // Condição para aguardar a contagem chegar a zero

    // Construtor que inicializa o contador interno com o valor fornecido
    public CountDownLatch(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count should be a positive integer");
        }
        this.count = count;
    }

    // Método para aguardar até que o contador chegue a zero
    public void await() throws InterruptedException {
        lock.lock(); // Bloqueia o acesso concorrente ao contador
        try {
            while (count > 0) { // Enquanto o contador for maior que zero
                condition.await(); // Aguarda até que a condição (contador igual a zero) seja satisfeita
            }
        } finally {
            lock.unlock(); // Libera o lock
        }
    }

    // Método para decrementar o contador interno e sinalizar todas as threads aguardando quando o contador chega a zero
    public void countDown() {
        lock.lock(); // Bloqueia o acesso concorrente ao contador
        try {
            if (count > 0) { // Se o contador for maior que zero
                count--; // Decrementa o contador
                if (count == 0) { // Se o contador chegar a zero
                    condition.signalAll(); // Sinaliza todas as threads que estão aguardando
                }
            }
        } finally {
            lock.unlock(); // Libera o lock
        }
    }


    public static void main(String[] args) {
        // Criando uma instância de CountDownLatch com contagem inicial de 3
        CountDownLatch latch = new CountDownLatch(3);

        // Criando e iniciando três threads
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println("Thread " + Thread.currentThread().threadId() + " está aguardando.");
                    latch.await(); // Aguardando até que o contador chegue a zero
                    System.out.println("Thread " + Thread.currentThread().threadId() + " foi liberada.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // Simulando alguma operação que decrementa o contador
        try {
            Thread.sleep(2000); // Aguardando um pouco antes de chamar countDown
            System.out.println("Decrementando o contador...");
            latch.countDown(); // Decrementando o contador
            Thread.sleep(2000); // Aguardando mais um pouco
            System.out.println("Decrementando o contador...");
            latch.countDown(); // Decrementando o contador
            Thread.sleep(2000); // Aguardando mais um pouco
            System.out.println("Decrementando o contador...");
            latch.countDown(); // Decrementando o contador
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }













}

    
