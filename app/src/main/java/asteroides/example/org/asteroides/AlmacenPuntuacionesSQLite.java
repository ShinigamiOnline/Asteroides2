package asteroides.example.org.asteroides;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

/**
 * Created by Vespertino on 12/02/2018.
 */

public class AlmacenPuntuacionesSQLite extends SQLiteOpenHelper implements AlmacenPuntuaciones{
    public AlmacenPuntuacionesSQLite(Context context){
        super (context, "puntuaciones", null,1);
    }

    //Métodos de SQLiteOpenHelper

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE puntuaciones (" +
        "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
        "puntos INTEGER,nombre TEXT,fecha BIGINT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //En caso de una nueva versión habría que actualizar las tablas.
    }

    //Métodos de AlmacenPuntuaciones.
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
    SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO puntuaciones VALUES (null, "+puntos+", '"+nombre+"',"+fecha+")");
        db.close();
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {

        Vector<String> result = new Vector<String>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT puntos, nombre FROM " + "puntuaciones ORDER BY puntos DESC LIMIT " + cantidad, null);
        while  (cursor.moveToNext()){
            result.add(cursor.getInt(0)+ " "+ cursor.getString(1));
        }
        cursor.close();
        db.close();
        return result;
    }
}
