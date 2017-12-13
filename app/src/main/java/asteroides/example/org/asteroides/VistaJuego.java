package asteroides.example.org.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Vector;

/**
 * Created by Vespertino on 22/11/2017.
 */

public class VistaJuego extends View {

    // THREAD Y TIEMPO
    // Thread encargado de procesar el juego

    private ThreadJuego thread = new ThreadJuego();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    // Cuando se realizó el último proceso
    private long ultimoProceso = 0;

    // //// NAVE //////
    private Grafico nave; // Gráfico de la nave
    private int giroNave; // Incremento de dirección
    private float aceleracionNave; // aumento de velocidad
    // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    // //// ASTEROIDES //////

    private Vector<Grafico> asteroides; // Vector con los Asteroides

    private int numAsteroides= 5; // Número inicial de asteroides

    private int numFragmentos= 3; // Fragmentos en que se divide



    public VistaJuego(Context context, AttributeSet attrs) {
        super(context,attrs);

        Drawable drawableNave, drawableAsteroide, drawableMisil;

        drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
        drawableNave = context.getResources().getDrawable(R.drawable.nave);

        asteroides = new Vector<Grafico>();
        nave = new Grafico(this,drawableNave);

        for (int i = 0; i < numAsteroides; i++) {

            Grafico asteroide = new Grafico(this, drawableAsteroide);

            asteroide.setIncY(Math.random() * 4 - 2);

            asteroide.setIncX(Math.random() * 4 - 2);

            asteroide.setAngulo((int) (Math.random() * 360));

            asteroide.setRotacion((int) (Math.random() * 8 - 4));

            asteroides.add(asteroide);

        }


    }

    @Override protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {

        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

        // Una vez que conocemos nuestro ancho y alto.

        for (Grafico asteroide: asteroides) {

            asteroide.setPosX(Math.random()*
                    (ancho-asteroide.getAncho()));

            asteroide.setPosY(Math.random()*
                    (alto-asteroide.getAlto()));
        }

        ultimoProceso = System.currentTimeMillis();
        thread.start();

    }

    @Override protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        for (Grafico asteroide: asteroides) {

            asteroide.dibujaGrafico(canvas);

        }

    }


    protected void actualizaFisica(){
        long ahora = System.currentTimeMillis();
        if(ultimoProceso + PERIODO_PROCESO > ahora){
            return; //Salir si el período de proceso no se ha cumplido
        }
        // Para una ejecución en tiempo real calculamos el factor de movimiento.
        double factorMov = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; // Para la próxima vez
        //Actualizamos velocidad y dirección de la nave a partir de giroNave y aceleracionNave (según la entrada del jugador)
        nave.setAngulo((int)  (nave.getAngulo() + giroNave * factorMov));
        double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * factorMov;
        double nIncY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * factorMov;
        //Actualizamos si el módulo de la velocidad no excee el máximo

        if(Math.hypot(nIncX,nIncY)<= MAX_VELOCIDAD_NAVE){
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        nave.incrementaPos(factorMov); // Actualizamos posicón

        for(Grafico asteroide : asteroides){
            asteroide.incrementaPos(factorMov);
        }


    }




}
