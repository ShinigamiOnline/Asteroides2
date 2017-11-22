package asteroides.example.org.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Vespertino on 22/11/2017.
 */

public class Grafico {
    private Drawable drawable;   //Imagen que dibujaremos

    private double posX, posY;   //Posición

    private double incX, incY;   //Velocidad desplazamiento

    private int angulo, rotacion;//Ángulo y velocidad rotación

    private int ancho, alto;     //Dimensiones de la imagen

    private int radioColision;   //Para determinar colisión

    //Donde dibujamos el gráfico (usada en view.ivalidate)

    private View view;

    // Para determinar el espacio a borrar (view.ivalidate)

    public static final int MAX_VELOCIDAD = 20;

    public Grafico(View view, Drawable drawable){

        this.view = view;

        this.drawable = drawable;

        ancho = drawable.getIntrinsicWidth();

        alto = drawable.getIntrinsicHeight();

        radioColision = (alto+ancho)/4;

    }
    public void dibujaGrafico(Canvas canvas){

        canvas.save();

        int x=(int) (posX+ancho/2);

        int y=(int) (posY+alto/2);

        canvas.rotate((float) angulo,(float) x,(float) y);

        drawable.setBounds((int)posX, (int)posY,
                (int)posX+ancho, (int)posY+alto);

        drawable.draw(canvas);

        canvas.restore();

        int rInval = (int) Math.hypot(ancho,alto)/2 + MAX_VELOCIDAD;

        view.invalidate(x-rInval, y-rInval, x+rInval, y+rInval);

    }

    public void incrementaPos(double factor){

        posX+=incX * factor;

        // Si salimos de la pantalla, corregimos posición

        if(posX<-ancho/2) {posX=view.getWidth()-ancho/2;}

        if(posX>view.getWidth()-ancho/2) {posX=-ancho/2;}

        posY+=incY * factor;

        if(posY<-alto/2) {posY=view.getHeight()-alto/2;}

        if(posY>view.getHeight()-alto/2) {posY=-alto/2;}

        angulo += rotacion * factor; //Actualizamos ángulo

    }

    public double distancia(Grafico g) {

        return Math.hypot(posX-g.posX, posY-g.posY);

    }



    public boolean verificaColision(Grafico g) {

        return(distancia(g) < (radioColision+g.radioColision));

    }


    public void setIncY(double incY) {
        this.incY = incY;
    }

    public void setIncX(double incX) {
        this.incX = incX;
    }

    public void setAngulo(int angulo) {
        this.angulo = angulo;
    }

    public void setRotacion(int rotacion) {
        this.rotacion = rotacion;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }
}
