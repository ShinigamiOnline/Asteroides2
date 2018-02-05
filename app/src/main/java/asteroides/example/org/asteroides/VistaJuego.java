package asteroides.example.org.asteroides;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Vector;

/**
 * Created by Vespertino on 22/11/2017.
 */

public class VistaJuego extends View implements SensorEventListener {
    private static final double MAX_VELOCIDAD_NAVE = 50;

    // THREAD Y TIEMPO
    // Thread encargado de procesar el juego

    private ThreadJuego thread = new ThreadJuego();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    // Cuando se realizó el último proceso
    private long ultimoProceso = 0;

    // //// NAVE //////
    private Grafico nave; // Gráfico de la nave
    private int giroNave = 2; // Incremento de dirección
    private float aceleracionNave = 2; // aumento de velocidad
    // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 2;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    // //// ASTEROIDES //////

    private Vector<Grafico> asteroides; // Vector con los Asteroides

    private int numAsteroides = 5; // Número inicial de asteroides

    private int numFragmentos = 3; // Fragmentos en que se divide

    ////Pantalla tactil ////
    private float mX = 0, mY = 0;
    private boolean disparo = false;

    ////MISIL////////
    private Grafico misil;
    private Vector<Grafico> misiles; // Vector con los misiles.

    private static int PASO_VELOCIDAD_MISIL = 12;

    private boolean misilActivo = false;

    private int tiempoMisil;

    //SONIDOS
    //private MediaPlayer mpDisparo, mpExplosion;
    private SoundPool soundPool;
    private int idDisparo,idExplosion;

    // Puntuación
    private int puntuacion = 0;
    private Activity padre;
    //fragmentando los asteroides.
    private Drawable drawableAsteroide[] = new Drawable[3];


    //-------- 5.42

    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable drawableNave, drawableMisil = null;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(pref.getString("graficos","1").equals("0")) {
            Path pathAteroide = new Path();
            pathAteroide.moveTo((float) 0.3, (float) 0.0);
            pathAteroide.lineTo((float) 0.6, (float) 0.0);
            pathAteroide.lineTo((float) 0.6, (float) 0.3);
            pathAteroide.lineTo((float) 1.0, (float) 0.4);
            pathAteroide.lineTo((float) 0.8, (float) 0.6);
            pathAteroide.lineTo((float) 0.9, (float) 0.9);
            pathAteroide.lineTo((float) 0.8, (float) 1.0);
            pathAteroide.lineTo((float) 0.4, (float) 1.0);
            pathAteroide.lineTo((float) 0.0, (float) 0.6);
            pathAteroide.lineTo((float) 0.0, (float) 0.2);
            pathAteroide.lineTo((float) 0.3, (float) 0.0);

            for (int i = 0; i < 3;i++){
                ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAteroide, 1, 1));

                dAsteroide.getPaint().setColor(Color.WHITE);
                dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
                dAsteroide.setIntrinsicWidth(50 - i * 14);
                dAsteroide.setIntrinsicHeight(50 - i * 14);
                drawableAsteroide[i] = dAsteroide;
            }
            setBackgroundColor(Color.BLACK);
            setLayerType(View.LAYER_TYPE_SOFTWARE,null);

            Path pathNave = new Path();
            pathNave.moveTo((float) 0.0, (float) 0.0);
            pathNave.lineTo((float) 1.0, (float) 0.5);
            pathNave.lineTo((float) 0.0, (float) 1.0);
            pathNave.lineTo((float) 0.0, (float) 0.0);

            ShapeDrawable dNave = new ShapeDrawable(new PathShape(pathNave,1,1));
            dNave.getPaint().setColor(Color.WHITE);
            dNave.getPaint().setStyle(Paint.Style.STROKE);
            dNave.setIntrinsicWidth(20);
            dNave.setIntrinsicHeight(15);
            drawableNave = dNave;

            ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
            dMisil.getPaint().setColor(Color.WHITE);
            dMisil.getPaint().setStyle(Paint.Style.STROKE);
            dMisil.setIntrinsicWidth(15);
            dMisil.setIntrinsicHeight(3);
            drawableMisil = dMisil;

        }else{
            drawableAsteroide[0] = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableAsteroide[1] = context.getResources().getDrawable(R.drawable.asteroide2);
            drawableAsteroide[2] = context.getResources().getDrawable(R.drawable.asteroide3);
            setLayerType(View.LAYER_TYPE_HARDWARE,null);
            drawableNave = context.getResources().getDrawable(R.drawable.nave);
            drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
        }

        asteroides = new Vector<Grafico>();
        nave = new Grafico(this, drawableNave);
        misil = new Grafico(this, drawableMisil);


        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);

            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }

        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> listSensors = mSensorManager.getSensorList(
                Sensor.TYPE_ORIENTATION);
        if (!listSensors.isEmpty()) {
            Sensor orientationSensor = listSensors.get(0);
            mSensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);

        }
        //mpDisparo = MediaPlayer.create(context, R.raw.disparo);
       // mpExplosion = MediaPlayer.create(context, R.raw.explosion);
        soundPool = new SoundPool( 5, AudioManager.STREAM_MUSIC , 0);
        idDisparo = soundPool.load(context, R.raw.disparo, 0);
        idExplosion = soundPool.load(context, R.raw.explosion, 0);
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {

        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

        // Una vez que conocemos nuestro ancho y alto.

        for (Grafico asteroide : asteroides) {
            do {
                asteroide.setPosX((int) (Math.random() * ancho));

                asteroide.setPosY((int) (Math.random() * alto));
            }while(asteroide.distancia(nave) < (ancho+alto)/5);
        }

        nave.setPosX(this.getWidth() / 2);
        nave.setPosY(this.getHeight() / 2);


        ultimoProceso = System.currentTimeMillis();
        thread.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        for (Grafico asteroide : asteroides) {
            asteroide.dibujaGrafico(canvas);
        }
        nave.dibujaGrafico(canvas);

        if (misilActivo) {
            misil.dibujaGrafico(canvas);
        }

    }


    protected void actualizaFisica() {
        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            return; //Salir si el período de proceso no se ha cumplido
        }
        // Para una ejecución en tiempo real calculamos el factor de movimiento.
        double factorMov = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; // Para la próxima vez
        //Actualizamos velocidad y dirección de la nave a partir de giroNave y aceleracionNave (según la entrada del jugador)
        nave.setAngulo((int) (nave.getAngulo() + giroNave * factorMov));
        double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * factorMov;
        double nIncY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * factorMov;
        //Actualizamos si el módulo de la velocidad no excee el máximo

        if (Math.hypot(nIncX, nIncY) <= MAX_VELOCIDAD_NAVE) {
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        nave.incrementaPos(factorMov); // Actualizamos posicón

        for (Grafico asteroide : asteroides) {
            asteroide.incrementaPos(factorMov);
        }

        // Actualizamos posición de misil

        if (misilActivo) {
            misil.incrementaPos(factorMov);
            tiempoMisil -= factorMov;
            if (tiempoMisil < 0) {
                misilActivo = false;
            } else {
                for (int i = 0; i < asteroides.size(); i++)
                    if (misil.verificaColision(asteroides.elementAt(i))) {
                        destruyeAsteroide(i);

                        break;
                    }
            }
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {

        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disparo=true;
                break;

            case MotionEvent.ACTION_MOVE:

                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);

                if (dy<6 && dx>6){
                    giroNave = Math.round((x - mX) / 2);
                    disparo = false;
                } else if (dx<6 && dy>6){
                    aceleracionNave = Math.round((mY - y) / 25);
                    disparo = false;
                }

                break;
            case MotionEvent.ACTION_UP:

                giroNave = 0;
                aceleracionNave = 0;
                if (disparo){
                    ActivaMisil();
                }
                break;
        }

        mX=x; mY=y;
        return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
       private boolean hayValorInicial = false;
        private float valorInicial ;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float valor = event.values[1];

        if (!hayValorInicial) {
            valorInicial = valor;
            hayValorInicial = true;

        }

        giroNave = (int) (valor - valorInicial) / 3;
    }

    int tam;
    private void destruyeAsteroide(int i) {

        if(asteroides.get(i).getDrawable()!= drawableAsteroide[2]){
            if(asteroides.get(i).getDrawable() == drawableAsteroide[1]) {
                tam = 2;
            }else{
                tam = 1;
            }
            for (int  n = 0;n< numFragmentos; n++){
                Grafico asteroide = new Grafico(this,drawableAsteroide[tam]);
                asteroide.setPosX(asteroides.get(i).getPosX());
                asteroide.setPosY(asteroides.get(i).getPosY());
                asteroide.setIncX(Math.random()*7 - 2 - tam);
                asteroide.setIncY(Math.random()*7 - 2 - tam);
                asteroide.setAngulo((int) (Math.random()*360));
                asteroide.setRotacion((int)(Math.random()*8-4));
                asteroides.add(asteroide);
            }
        }

        asteroides.remove(i);
        misilActivo = false;
        //mpExplosion.start();
        soundPool.play(idExplosion,1,1,0,0,1);
        puntuacion += 1000;
        if(asteroides.isEmpty()){
            salir();
        }

    }

    private void ActivaMisil() {

        misil.setPosX( nave.getPosX());
        misil.setPosY(nave.getPosY());
        misil.setAngulo((int) nave.getAngulo());
        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
        misilActivo = true;

        //mpDisparo.start();
        soundPool.play(idDisparo,1,1,1,0,1);

    }

    public ThreadJuego getThread() {
        return thread;
    }

    class ThreadJuego extends Thread {
        private boolean pausa,corriendo;

        public synchronized void pausar() {
            pausa = true;
        }

        public synchronized void reanudar() {
            pausa = false;
            notify();
        }

        public void detener() {
            corriendo = false;
            if (pausa) reanudar();
        }

        @Override public void run() {
            corriendo = true;
            while (corriendo) {
                actualizaFisica();
                synchronized (this) {
                    while (pausa)
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                }
            }
        }
    }

    public  void setPadre(Activity padre){
        this.padre = padre;
    }

    private void salir(){
        Bundle bundle = new Bundle();
        bundle.putInt("puntuación",puntuacion);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        padre.setResult(Activity.RESULT_OK,intent);
        padre.finish();

    }


}


