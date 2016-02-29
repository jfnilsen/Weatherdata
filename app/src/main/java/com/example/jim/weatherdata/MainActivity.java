package com.example.jim.weatherdata;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.net.CookieHandler;
import java.net.CookieManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Aktiver cookies:
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        FragmentMenu fragmentMenu = new FragmentMenu();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_frame, fragmentMenu);
        transaction.commit();

        setContentView(R.layout.activity_main);
    }

    public void download(View view) {
        RetainedFragment fragment = new RetainedFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.retained_fragment,fragment);
        transaction.commit();
        fragment.getWeatherFromJson(60);

    }

}
