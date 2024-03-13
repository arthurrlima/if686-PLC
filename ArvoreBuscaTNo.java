import java.util.Random;

public class ArvoreBuscaTNo {
    static class No {
        int valor;
        No esquerda, direita;
        final Object lock = new Object(); // Lock para travamento do nó

        public No(int valor) {
            this.valor = valor;
        }
    }

    private No raiz;

    public void inserir(int valor) {
        raiz = inserirRecursivamente(raiz, valor);
    }

    private No inserirRecursivamente(No no, int valor) {
        if (no == null) {
            return new No(valor);
        }

        if (valor < no.valor) {
            synchronized (no.lock) { // Travamento por nó
                no.esquerda = inserirRecursivamente(no.esquerda, valor);
            }
        } else if (valor > no.valor) {
            synchronized (no.lock) { // Travamento por nó
                no.direita = inserirRecursivamente(no.direita, valor);
            }
        }

        return no;
    }

    public int contarNos() {
        return contarNosRecursivamente(raiz);
    }

    private int contarNosRecursivamente(No no) {
        if (no == null) {
            return 0;
        }
        return 1 + contarNosRecursivamente(no.esquerda) + contarNosRecursivamente(no.direita);
    }

    public static void main(String[] args) throws InterruptedException {
        ArvoreBuscaTNo arvore = new ArvoreBuscaTNo();
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
