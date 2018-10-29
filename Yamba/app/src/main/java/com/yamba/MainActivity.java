package com.yamba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Called to lazily initialize the action bare
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu items to the action bar
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Called every time user click on an action
    @Override
    //9.4.1. onOptionItemSelcted() is called when user selects an item in the menu
    public boolean onOptionsItemSelected(MenuItem item) {

        //9.4.2.Because teh same method is called regardless of which item the user clicks,
        //you need to figure out the ID of the item, and based on that,
        //switch to a specific case to handle each item. At this point, we only have one menu item,
        //but that might change in the furute.
        //Switching ID is very scalable approach and will adapt nicely as our application grows in complexity
        switch (item.getItemId()) {
            case R.id.action_settings:

                //9.4.3. the startActivity() method in context launches a new activity.
                //In this case, we are creating a new intent that specifies starting the SettingActivity class.
                //Intents are Android's way of specifying what is the target of your request to startActivity()
                //In this case, we're starting out setting activity
                startActivity(new Intent(this, SettingsActivity.class));
                return true; //9.4.4. return true to consume the event here

            case R.id.action_tweet:
                startActivity(new Intent(this, StatusActivity.class));
                return true;

            default:
                return false;
        }
    }
}
