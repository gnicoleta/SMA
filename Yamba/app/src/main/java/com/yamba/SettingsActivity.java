package com.yamba;
import android.app.Activity;
import android.os.Bundle;

import com.yamba.SettingsFragment;


public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Check whether this activity was created before
        if (savedInstanceState == null) {
            //Create a fragment
            //9.1. As before, in case this is teh first time we're creating this activity,
            //we create the instance of the fragment that will be housed here
            SettingsFragment fragment = new SettingsFragment();

            //9.2. Next, we obtain the fragment transaction from teh fragment manager,
            //and add this fragment to the activity's main content
            getFragmentManager().beginTransaction().add(android.R.id.content, fragment,
                    fragment.getClass().getSimpleName()).commit();
        }
    }

    ;
}