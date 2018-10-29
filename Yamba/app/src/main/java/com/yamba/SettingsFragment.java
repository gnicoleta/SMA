package com.yamba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

//9.2.1. Unlike regular fragments, SettingsFragment will subclass the Preference Fragment class
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences prefs;

    @Override
    //9.2.2. Just like other fragment, we override the OnCreate() method to initialize the fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //9.2.3. Our preference fragment sets its content from settings.xml(in my case prefs.xml) file
        // via a call to addPreferenceFromResource()
        addPreferencesFromResource(R.xml.prefs);
    }

    @Override
    public void onStart() {
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        
    }
}
