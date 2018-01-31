package asteroides.example.org.asteroides;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.R.attr.actionLayout;
import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    public static AlmacenPuntuacionesPreferencias almacen;
    private Button bAcercaDe;
    private Button bPuntuaciones;
    private Button bJugar;
    private MediaPlayer mp;

    static final int ACTIV_JUEGO = 0;

    public void lanzarAcercaDe(View view){

        Intent i = new Intent(this, Activity_acerda_de.class);
        startActivityForResult(i,ACTIV_JUEGO);
    }
    public void lanzarPuntuaciones (View view){
        Intent i = new Intent(this,Puntuaciones.class);
        startActivity(i);
    }
    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, PreferenciasActivity.class);
        startActivity(i);
    }

    public void lanzarJuego(View vista){
        Intent i = new Intent(this, Juego.class);
        startActivity(i);
    }

    public boolean onOptionsItemSelected() {
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        } else
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setBackgroundResource(R.drawable.degradado);

        bAcercaDe = (Button) findViewById(R.id.button4);
        bPuntuaciones = (Button) findViewById(R.id.button3);
        bJugar = (Button) findViewById(R.id.button2);

        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lanzarAcercaDe(null);
            }
        });


        bPuntuaciones.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarPuntuaciones(null);

            }
        });
        bJugar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lanzarJuego(null);
            }
        });

        mp = MediaPlayer.create(this,R.raw.audio);
        mp.start();

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        almacen = new AlmacenPuntuacionesPreferencias(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onPause() {
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override protected void onStop() {
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override protected void onDestroy() {
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode == ACTIV_JUEGO && resultCode == RESULT_OK && data != null){
            int puntuacion = data.getExtras().getInt("puntuacion");
            String nombre = "Yo";
            //Mejor leer nombre desde un AlertDialog.Builder o preferencias
            almacen.guardarPuntuacion(puntuacion,nombre,System.currentTimeMillis());
            lanzarPuntuaciones(null);
        }


    }
}

