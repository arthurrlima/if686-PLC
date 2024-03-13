import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ArvoreBusca {
    static class No {
        int valor;
        No esquerda, direita;

        public No(int valor) {
            this.valor = valor;
        }
    }

    private No raiz;
    private Lock lock = new ReentrantLock();

    public void inserir(int valor) {
        lock.lock(); // Bloqueia o acesso concorrente à árvore
        try {
            raiz = inserirRecursivamente(raiz, valor);
        } finally {
            lock.unlock(); // Libera o lock
        }
    }

    private No inserirRecursivamente(No no, int valor) {
        if (no == null) {
            return new No(valor);
        }

        if (valor < no.valor) {
            no.esquerda = inserirRecursivamente(no.esquerda, valor);
        } else if (valor > no.valor) {
            no.direita = inserirRecursivamente(no.direita, valor);
        }

        return no;
    }

    public int contarNos() {
        lock.lock(); // Bloqueia o acesso concorrente à árvore
        try {
            return contarNosRecursivamente(raiz);
        } finally {
            lock.unlock(); // Libera o lock
        }
    }

    private int contarNosRecursivamente(No no) {
        if (no == null) {
            return 0;
        }
        return 1 + contarNosRecursivamente(no.esquerda) + contarNosRecursivamente(no.direita);
    }

    public static void main(String[] args) throws InterruptedException {
        ArvoreBusca arvore = new ArvoreBusca();
        Random random = new Random();
        int numThreads = 50;
        int numInsercoesPorThread = 2000;

        // Criando e iniciando threads para inserção de números aleatórios na árvore
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numInsercoesPorThread; j++) {
                    int valor = random.nextInt(100000); // Números aleatórios de 0 a 99999
                    arvore.inserir(valor);
                }
            });
            threads[i].start();
        }

        // Aguardando todas as threads terminarem
        for (Thread thread : threads) {
            thread.join();
        }

        // Contando o número total de nós na árvore
        int numNos = arvore.contarNos();
        System.out.println("Quantidade total de nós na árvore: " + numNos);
    }
}
