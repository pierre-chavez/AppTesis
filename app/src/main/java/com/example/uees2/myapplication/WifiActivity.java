package com.example.uees2.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

public class WifiActivity extends AppCompatActivity {
    Button button;
    ImageButton buttonRefresh;
    WebView webView;
    public String networkSSID = "PULSERA";
    public String networkPass = "12345678";
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        webView = findViewById(R.id.wifiViewWeb);
        button = findViewById(R.id.buttonInicio);
        buttonRefresh = (ImageButton) findViewById(R.id.buttonRecargar);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webView.loadUrl("file:///android_asset/error.html");
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("http://10.0.1.1");
                Log.d("Recargar", "true");
                //webView.reload();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (validaPrivilegios()) {
            validaUbicacion();
            configurarWifi();
        }else{
            ActivityCompat.requestPermissions(WifiActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            validaUbicacion();
            if (ActivityCompat.checkSelfPermission(WifiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WifiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WifiActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
            }else{
                // Write you code here if permission already given.
                validaUbicacion();
                configurarWifi();
            }

        }
    }
    public void validaUbicacion(){
        boolean gps_enabled = false;
        boolean network_enabled = false;
        LocationManager lm = (LocationManager)WifiActivity.this.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {Log.d("","");}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {Log.d("","");}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(WifiActivity.this)
                    .setMessage(R.string.gps_network_not_enabled)
                    .setNegativeButton(R.string.Cancel,null)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).show();
        }
    }

    public void configurarWifi(){
        if (wifiManager.getWifiState() == wifiManager.WIFI_STATE_DISABLING ||
                wifiManager.getWifiState() == wifiManager.WIFI_STATE_DISABLED) {
            enableWifi();
        }

        WifiInfo wifiInfo = null;
        if (wifiManager != null)
            wifiInfo = wifiManager.getConnectionInfo();
        String ssid = null;
        if (wifiInfo != null)
            ssid = wifiInfo.getSSID();
        Log.d("wifiInfo", ssid);
        Log.d("SSID", networkSSID);
        if (ssid.equals('"' + networkSSID + '"')) {
            webView.loadUrl("http://10.0.1.1");
        } else {
            connectWifi(networkSSID, "wpa", networkPass);
            webView.loadUrl("http://10.0.1.1");
            //Toast.makeText(getApplicationContext(), "Compruebe que la pulsera este encendida.", Toast.LENGTH_LONG).show();
        }
    }
    public void enableWifi() {
        wifiManager.setWifiEnabled(true);
    }

    public void disableWifi() {
        wifiManager.setWifiEnabled(false);
    }


    public void connectWifi(String networkSSID, String type, String networkPass) {
        if (wifiManager.getWifiState() == wifiManager.WIFI_STATE_DISABLING ||
                wifiManager.getWifiState() == wifiManager.WIFI_STATE_DISABLED) {
            enableWifi();
        }
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\""; // Please note the quotes. String
        // should contain ssid in quotes

        switch (type) {
            case "wep":
                // wep
                conf.wepKeys[0] = "\"" + networkPass + "\"";
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;
            case "wpa":
                // wpa
                conf.preSharedKey = "\"" + networkPass + "\"";
                break;
            case "open":
                // open
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;
        }

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }

    }

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !(connectionInfo.getSSID().isEmpty())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }
    public boolean validaPrivilegios(){
        // TODO: comprobar version actual de android que estamos corriendo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if( getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {////TODO: método para saber si tiene el permiso
                //COMPRUEBA POR ULTIMA VEZ EL PERMISO DEL SISTEMA  DESDE EL ACTIVITYCOMPAT QUE EL READ_PHONE_STATE HA SIDO ACEPTADO
                return ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

}
