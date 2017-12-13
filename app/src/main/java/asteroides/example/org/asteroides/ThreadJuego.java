package asteroides.example.org.asteroides;

/**
 * Created by Vespertino on 13/12/2017.
 */

public class ThreadJuego extends Thread {
    @Override
    public void run(){
        while (true){
            actualizaFisica();
        }
    }
}
