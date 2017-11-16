package asteroides.example.org.asteroides;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Vespertino on 06/11/2017.
 */

public class PreferenciasActivity extends Activity {
    @Override
    protected void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new PreferenciasFragment()).commit();
    }
}
