package asteroides.example.org.asteroides;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Vespertino on 06/11/2017.
 */

public class PreferencisFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        addPreferencesFromResource(R.xml.preferencias);


    }

}
