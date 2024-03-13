import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VetorSeguro {
    private int[] vetor;
    private Lock[] locks; // Array de locks para garantir a sincronização por índice

    public VetorSeguro(int tamanho) {
        vetor = new int[tamanho];
        locks = new Lock[tamanho];
        for (int i = 0; i < tamanho; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    // Método para ler o valor em um determinado índice
    public int ler(int indice) {
        locks[indice].lock(); // Adquire o lock para o índice específico
        try {
            return vetor[indice];
        } finally {
            locks[indice].unlock(); // Libera o lock
        }
    }

    // Método para escrever um valor em um determinado índice
    public void escrever(int indice, int valor) {
        locks[indice].lock(); // Adquire o lock para o índice específico
        try {
            vetor[indice] = valor;
        } finally {
            locks[indice].unlock(); // Libera o lock
        }
    }

    // Método para trocar os valores em dois índices distintos
    public void trocar(int indice1, int indice2) {
        // Garante a ordem de aquisição dos locks para evitar deadlock
        int menorIndice = Math.min(indice1, indice2);
        int maiorIndice = Math.max(indice1, indice2);
        locks[menorIndice].lock();
        locks[maiorIndice].lock();
        try {
            int temp = vetor[indice1];
            vetor[indice1] = vetor[indice2];
            vetor[indice2] = temp;
        } finally {
            locks[menorIndice].unlock();
            locks[maiorIndice].unlock();
        }
    }

    public static void main(String[] args) {
        VetorSeguro vetor = new VetorSeguro(10);

        // Criando e iniciando 4 threads para manipular o vetor
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                // Realiza inserções e leituras nos mesmos índices do vetor
                for (int j = 0; j < 5; j++) {
                    int indice = j;
                    vetor.escrever(indice, threadId * 5 + j);
                    System.out.println("Thread " + threadId + ": Escreveu " + (threadId * 5 + j) + " no índice " + indice);
                    System.out.println("Thread " + threadId + ": Leu " + vetor.ler(indice) + " no índice " + indice);
                }
                // Realiza trocas entre elementos do vetor
                int indice1 = 0;
                int indice2 = 1;
                vetor.trocar(indice1, indice2);
                System.out.println("Thread " + threadId + ": Trocou os valores nos índices " + indice1 + " e " + indice2);
            });
            threads[i].start();
        }

        // Aguarda todas as threads terminarem
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}