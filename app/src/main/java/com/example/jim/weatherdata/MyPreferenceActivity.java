package com.example.jim.weatherdata;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

/**
 * Created by Jim on 01/03/2016.
 */
public class MyPreferenceActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.mypreferenceheader, target);
    }

    @Override //Fra og med API 19.
    protected boolean isValidFragment(String fragmentName) {
        return (MyPreferenceFragment.class.getName().equals(fragmentName));
    }
}
