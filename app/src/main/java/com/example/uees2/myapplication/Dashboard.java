package com.example.uees2.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.onesignal.OneSignal;


public class Dashboard extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public static final String TAG = "Version";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //dasboard
        DashboardFrag dashboardFrag = new DashboardFrag();
        fragmentTransaction.replace(R.id.container, dashboardFrag);
        fragmentTransaction.commit();


        switch (getFirstTimeRun(this)) {
            case 0:
                Log.d(TAG, "Es la primera vez!");
                // acá haces el intent a tu activity especial
                Intent intent = new Intent(this, Informacion.class);
                startActivity(intent);
                break;
            case 1:
                Log.d(TAG, "Ya has iniciado la app alguna vez");
                //
                break;
            case 2:
                Log.d(TAG, "Es una versión nueva");
                //
                break;
        }


    }

    //Retorna: 0 primera vez / 1 no es primera vez / 2 nueva versión
    public static int getFirstTimeRun(Context contexto) {
        SharedPreferences sp = contexto.getSharedPreferences("MYAPP", 0);
        int result, currentVersionCode = BuildConfig.VERSION_CODE;
        int lastVersionCode = sp.getInt("FIRSTTIMERUN", -1);
        if (lastVersionCode == -1) result = 0;
        else
            result = (lastVersionCode == currentVersionCode) ? 1 : 2;
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply();
        return result;
    }
}
