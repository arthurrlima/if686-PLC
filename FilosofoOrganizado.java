import java.util.concurrent.locks.*;


/**
  Locks are obtained in a predefined order: the one with the lower
  number of obtained first and the one with the greater number 
  later. Avoids circular wait.
*/
public class FilosofoOrganizado extends Thread {

    int fid = -1;

    static final int NUM = 5;
    static final int FOME = 20;
    static final int INTERVALO = 100;

    static Lock[] garfos = new Lock[NUM];
    
    static {
        for (int i = 0; i < garfos.length; i++) {
            garfos[i] = new ReentrantLock();
        }
    }
    
    public FilosofoOrganizado(int id) {
        this.fid = id;
    }
    
    public void run() {
        int contadorFome = 0;
        while(contadorFome < FOME) {
            try {
                Thread.sleep(INTERVALO);
            } catch (InterruptedException ie){}
            int direita = (fid+1)%NUM;
            int primeiro = fid;
            int segundo = direita;
            if (primeiro > segundo) {
                int temp = primeiro;
                primeiro = segundo;
                segundo = temp;
            }
            pegaGarfo(primeiro); 
            pegaGarfo(segundo); 
            System.out.println("Filosofo " + this.fid + " estah comendo " );            
            liberarGarfo(segundo);
            liberarGarfo(primeiro);

            contadorFome++;
        }
    }
    
    public void pegaGarfo(int numGarfo) {
        System.out.println("Filosofo " + this.fid + " tenta pegar o garfo " + numGarfo);  
        garfos[numGarfo].lock();
    }
    
    public void liberarGarfo(int numGarfo) {
        garfos[numGarfo].unlock();
    }





    public static void main(String args[]) {
        FilosofoOrganizado[] fs = new FilosofoOrganizado[NUM];
        
        for (int j = 0; j < fs.length; j++) {
            fs[j] = new FilosofoOrganizado(j);
            fs[j].start();
        }

        for (int k = 0; k < fs.length; k++) {
            try {             
                fs[k].join();
            } catch(InterruptedException ie) {}
            System.out.println("Filosofo " + k + " terminou.");
        }
    }

}