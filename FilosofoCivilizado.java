import java.util.concurrent.locks.*;


/**
  Avoids deadlocks by releasing acquired locks when it is not possible
  to acquire both locks.
*/
public class FilosofoCivilizado extends Thread {

    int fid = -1;

    static final int NUM = 5;
    static final int FOME = 20;
    static final int INTERVALO = 10;

    static Lock[] garfos = new Lock[NUM];
    
    static {
        for (int i = 0; i < garfos.length; i++) {
            garfos[i] = new ReentrantLock();
        }
    }
    
    public FilosofoCivilizado(int id) {
        this.fid = id;
    }
    
    public static void main(String args[]) {
        FilosofoCivilizado[] fs = new FilosofoCivilizado[NUM];
        
        for (int j = 0; j < fs.length; j++) {
            fs[j] = new FilosofoCivilizado(j);
            fs[j].start();
        }

        for (int k = 0; k < fs.length; k++) {
            try {             
                fs[k].join();
            } catch(InterruptedException ie) {}
            System.out.println("Filosofo " + k + " terminou.");
        }
    }
    
    public void run() {
        int contadorFome = 0;
        while(contadorFome < FOME) {
            try {
                Thread.sleep(INTERVALO);
            } catch (InterruptedException ie){}
            int direita = (fid+1)%NUM;
            boolean esq = pegaGarfo(fid);      // Esquerda
            boolean dir = pegaGarfo(direita);  // Direita
            try {
                while (!esq || !dir) {
                    if (esq) {
                        liberarGarfo(fid); // esquerda
                    }
                    if (dir) {
                        liberarGarfo(direita);
                    }
                    Thread.sleep(INTERVALO);                     
                
                    esq = pegaGarfo(fid);      // Esquerda
                    dir = pegaGarfo(direita);  // Direita  
                }
                // se chegou aqui, adquiriu os dois garfos.
                Thread.sleep(INTERVALO);
            } catch(InterruptedException ie) {                
            } finally {
                    if (esq) {
                        liberarGarfo(fid); // esquerda
                    }
                    if (dir) {
                        liberarGarfo(direita);
                    }
            }
            contadorFome++;
            System.out.println("Filosofo " + this.fid + " estah comendo " );
        }
    }
    
    public boolean pegaGarfo(int numGarfo) {
        System.out.println("Filosofo " + this.fid + " tenta pegar o garfo " + numGarfo);  
        return garfos[numGarfo].tryLock();
    }
    
    public void liberarGarfo(int numGarfo) {
        garfos[numGarfo].unlock();
    }
}