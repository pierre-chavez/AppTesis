package com.example.uees2.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;


public class Dashboard extends AppCompatActivity {
    public static String EMAIL;
    private static Context mContext;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String playerId = "";

    FirebaseAuth mAuth;
    Usuario usuario;
    DatabaseReference databaseUsuarios;


    public static final String TAG = "Version";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        setContentView(R.layout.activity_dashboard);
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .init();
        OneSignal.enableVibrate(true);
        OneSignal.enableSound(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("Dashboard", "User:" + userId);
                playerId = userId;
                if (registrationId != null)
                    Log.d("Dashboard", "registrationId:" + registrationId);

            }
        });
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        DashboardFrag dashboardFrag = new DashboardFrag();
        fragmentTransaction.add(R.id.container, dashboardFrag);
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

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        Dashboard.mContext = mContext;
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

    @Override
    protected void onResume() {
        super.onResume();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (mAuth.getUid() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (mAuth.getUid() != null) {
            databaseUsuarios = FirebaseDatabase.getInstance().getReference("Usuario").child(mAuth.getUid());
            databaseUsuarios.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    usuario = dataSnapshot.getValue(Usuario.class);
                    EMAIL = usuario.getEmail();
                    // cambiarDasboard(usuario);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        databaseUsuarios = FirebaseDatabase.getInstance().getReference("Usuario").child(mAuth.getUid());
        databaseUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usuario = dataSnapshot.getValue(Usuario.class);
                EMAIL = usuario.getEmail();
               // cambiarDasboard(usuario);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void cambiarDasboard(Usuario usuario){

        if(usuario.getRol().equals("Admin") || usuario.getRol().equals("Enfermero") ){
            Log.d("","");


        }else{

            Intent intent = new Intent(this.getContext(), Familiar.class);
            startActivity(intent);

        }

    }
}


