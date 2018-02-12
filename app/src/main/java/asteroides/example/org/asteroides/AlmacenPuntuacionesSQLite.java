package asteroides.example.org.asteroides;

import android.content.Context;
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

    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {

    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        return null;
    }
}
