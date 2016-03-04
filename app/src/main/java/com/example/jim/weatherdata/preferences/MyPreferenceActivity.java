package com.example.jim.weatherdata.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.jim.weatherdata.R;

import java.util.List;

/**
 * Created by Jim on 01/03/2016.
 */
public class MyPreferenceActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.mypreferenceheader, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return (MyPreferenceFragment.class.getName().equals(fragmentName));
    }
}
