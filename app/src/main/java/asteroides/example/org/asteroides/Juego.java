package asteroides.example.org.asteroides;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Vespertino on 22/11/2017.
 */
public class Juego extends Activity {
    private VistaJuego vistaJuego;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);
        vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
    }

    @Override
    protected void onPause() {
        vistaJuego.getThread().pausar();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vistaJuego.getThread().reanudar();
    }

    @Override
    protected void onDestroy() {
        vistaJuego.getThread().detener();
        super.onDestroy();
    }
}

