package com.example.jim.weatherdata;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Jim on 01/03/2016.
 */
public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
