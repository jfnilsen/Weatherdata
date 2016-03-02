package com.example.jim.weatherdata;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by Jim on 01/03/2016.
 */
public class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        findPreference("DELETE_BUTTON").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        WeatherDataSource data = new WeatherDataSource(getActivity());
        try {
            data.open();
            data.deleteAllStoredData();
            data.close();
            CharSequence text = "Database table deleted!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
