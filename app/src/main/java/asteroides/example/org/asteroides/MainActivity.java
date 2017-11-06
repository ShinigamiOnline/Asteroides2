package asteroides.example.org.asteroides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    private Button bAcercaDe;
    private Button bSalir;

    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(this, Activity_acerda_de.class);
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
        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lanzarAcercaDe(null);
            }
        });

        bSalir = (Button) findViewById(R.id.button3);
        bSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

}

