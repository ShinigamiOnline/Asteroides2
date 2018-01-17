package asteroides.example.org.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Vespertino on 22/11/2017.
 */

public class Grafico {
    private Drawable drawable;   //Imagen que dibujaremos
    private int cenX, cenY;   //Posición
    private int ancho, alto;     //Dimensiones de la imagen
    private double incX,incY;
    private double angulo,rotacion;
    private int radioColision;   //Para determinar colisión
    private int xAnterior,yAnterior;
    private int radioInval;
    private View view;

    // Para determinar el espacio a borrar (view.ivalidate)
    public static final int MAX_VELOCIDAD = 20;

    public Grafico(View view, Drawable drawable){

        this.view = view;
        this.drawable = drawable;
        ancho = drawable.getIntrinsicWidth();
        alto = drawable.getIntrinsicHeight();
        radioColision = (alto+ancho)/4;
        radioInval =(int)Math.hypot(ancho/2,alto/2);

    }
    public void dibujaGrafico(Canvas canvas){

        int x=cenX-ancho/2;

        int y=cenY - alto/2;
        drawable.setBounds(x,y,x+ancho,y+alto);
        canvas.save();
        canvas.rotate((float) angulo,cenX,cenY);
        drawable.draw(canvas);
        canvas.restore();
        view.invalidate(cenX-radioInval,cenY-radioInval,cenX+radioInval,cenY+radioInval);
        view.invalidate(xAnterior-radioInval,yAnterior-radioInval,xAnterior+radioInval,yAnterior+radioInval);
        xAnterior = cenX;
        yAnterior = cenY;
    }

    public void incrementaPos(double factor){

        cenX+=cenX * factor;
        cenY+=cenY * factor;
        angulo += rotacion * factor; //Actualizamos ángulo

        // Si salimos de la pantalla, corregimos posición

        if(cenX<0)
            cenX=view.getWidth();

        if(cenX<view.getWidth())
            cenX=0;

        if(cenY < 0)
            cenY = view.getHeight();

        if(cenY>view.getHeight())
            cenY =0;


    }

    public double distancia(Grafico g) {

        return Math.hypot(cenX-g.cenX, cenY-g.cenY);

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

    public double getAngulo() {
        return  angulo;
    }

    public double getIncX() {
        return incX;
    }

    public double getIncY() {
        return incY;
    }

    public Object getCenX() {
        return cenX;
    }


    public Object getCenY() {
        return cenY;
    }

    public void setCenX(int cenX) {
        this.cenX = cenX;
    }

    public void setCenY(int cenY) {
        this.cenY = cenY;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

}
