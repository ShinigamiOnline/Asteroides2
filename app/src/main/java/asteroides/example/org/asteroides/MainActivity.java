package asteroides.example.org.asteroides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    public static AlmacenPuntuacionesArray almacen = new AlmacenPuntuacionesArray();
    private Button bAcercaDe;
    private Button bPuntuaciones;

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, Activity_acerda_de.class);
        startActivity(i);
    }
    public void lanzarPuntuaciones (View view){
        Intent i = new Intent(this,Puntuaciones.class);
        startActivity(i);
    }
    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, PreferenciasActivity.class);
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
        bAcercaDe = (Button) findViewById(R.id.button4);
        bPuntuaciones = (Button) findViewById(R.id.button3);

        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lanzarAcercaDe(null);
            }
        });


        bPuntuaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarPuntuaciones(null);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    }

