package asteroides.example.org.asteroides;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button bAcercaDe;

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, Activity_acerda_de.class);
        startActivity(i);
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

    }

}

