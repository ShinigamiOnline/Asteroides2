package asteroides.example.org.asteroides;

import android.content.Context;
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
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Vespertino on 22/11/2017.
 */

public class VistaJuego extends View implements SensorEventListener {
    private static final double MAX_VELOCIDAD_NAVE = 80;

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

    /////// ASTEROIDES //////

    private Vector<Grafico> asteroides; // Vector con los Asteroides

    private int numAsteroides = 5; // Número inicial de asteroides

    private int numFragmentos = 3; // Fragmentos en que se divide

    ////Pantalla tactil ////
    private float mX = 0, mY = 0;
    private boolean disparo = false;

    //Para los sensores
    private boolean hayValorInicial;
    private float valorInicial;

    ////MISIL////////
    private Grafico misil;
    private Vector<Grafico> misiles; // Vector con los Asteroides

    private static int PASO_VELOCIDAD_MISIL = 12;

    private boolean misilActivo = false;

    private int tiempoMisil;


    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable drawableNave, drawableAsteroide, drawableMisil;

        // drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (pref.getString("graficos", "1").equals("0")) {
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

            ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAteroide, 1, 1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(50);
            dAsteroide.setIntrinsicHeight(50);
            drawableAsteroide = dAsteroide;
            setBackgroundColor(Color.BLACK);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
            dMisil.getPaint().setColor(Color.WHITE);
            dMisil.getPaint().setStyle(Paint.Style.STROKE);
            dMisil.setIntrinsicWidth(15);
            dMisil.setIntrinsicHeight(3);

            drawableMisil = dMisil;

        } else {
            drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        drawableNave = context.getResources().getDrawable(R.drawable.nave);
        asteroides = new Vector<Grafico>();
        nave = new Grafico(this, drawableNave);

        drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
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
            mSensorManager.registerListener(this, orientationSensor,
                    SensorManager.SENSOR_DELAY_GAME);

        }

    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {

        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

        // Una vez que conocemos nuestro ancho y alto.

        for (Grafico asteroide : asteroides) {
            do {
                asteroide.setIncX((int) (Math.random() * ancho));

                asteroide.setIncY((int) (Math.random() * alto));
            } while (asteroide.distancia(nave) < (ancho + alto) / 5);
        }

        nave.setIncX(this.getWidth() / 2);
        nave.setIncY(this.getHeight() / 2);


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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        hayValorInicial = false;
        valorInicial = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float valor = event.values[1];

        if (!hayValorInicial) {
            valorInicial = valor;
            hayValorInicial = true;

        }

        giroNave = (int) (valor - valorInicial) / 3;
    }


    public class ThreadJuego extends Thread {
        @Override
        public void run() {
            while (true) {
                actualizaFisica();
            }
        }

    }

    @Override
    public boolean onKeyDown(int codigoTecla, KeyEvent evento) {

        super.onKeyDown(codigoTecla, evento);

        // Suponemos que vamos a procesar la pulsación

        boolean procesada = true;

        switch (codigoTecla) {

            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = +PASO_ACELERACION_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                giroNave = -PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = +PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                ActivaMisil();
                break;

            default:

                // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break;
        }
        return procesada;

    }

    @Override
    public boolean onKeyUp(int codigoTecla, KeyEvent evento) {

        super.onKeyUp(codigoTecla, evento);

        // Suponemos que vamos a procesar la pulsación

        boolean procesada = true;

        switch (codigoTecla) {

            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = 0;
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = 0;
                break;

            default:
                // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break;

        }
        return procesada;

    }

    /*La nave con la pantalla tactil*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disparo = true;
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy < 6 && dx > 6) {
                    giroNave = Math.round((x - mX) / 2);
                    disparo = false;

                } else if (dx < 6 && dy > 6) {
                    aceleracionNave = Math.round((mY - y) / 25);
                    disparo = false;

                }
                break;

            case MotionEvent.ACTION_UP:
                giroNave = 0;
                aceleracionNave = 0;
                if (disparo) {
                    ActivaMisil();
                }
                break;

        }

        mX = x;
        mY = y;

        return true;

    }

    private void destruyeAsteroide(int i) {

        asteroides.remove(i);
        misilActivo = false;
    }

    private void ActivaMisil() {

        misil.setCenX((Integer) nave.getCenX());
        misil.setCenY((Integer) nave.getCenY());
        misil.setAngulo((int) nave.getAngulo());
        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
        misilActivo = true;

    }


}
