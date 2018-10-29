package com.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class StatusActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.new_activity_status);

        //Check if this activity was created before
        if (savedInstanceState == null) {
            //4.1. we no longer nee setContentView(). Instead,we check whether this is the first time
            //this activity is created, because we could also have gotten to this point in the code when
            //the activity exists and the screen has been rotated by the user
            StatusFragment fragment = new StatusFragment(); // 4.2. first time around, wee need to instantiate the status fragment

            //4.3. Next, we obtain the fragment manager fro the current context, start a transaction,
            // attach a new fragment to the root of this activity identified by the system ID android.R.id.content,
            // and commit the transaction
            getFragmentManager().beginTransaction().add(android.R.id.content,
                    fragment, fragment.getClass().getSimpleName()).commit();
        }
    }

    @Override
    //9.3.1. Called the first time this menu needs to be displayed,
    //such as the first time this activity is rendered on the screen
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //9.3.2. Get the MenuInflater object from the context view getMenuInflater()
        //Then use the inflater to inflate the menu from the XML resource
        getMenuInflater().inflate(R.menu.main, menu);

        //9.3.3. You must return true for this menu to me displayed
        return true;
    }
}
