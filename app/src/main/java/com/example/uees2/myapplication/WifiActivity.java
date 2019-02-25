package com.example.uees2.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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

        if (type.equals("wep")) {
            // wep
            conf.wepKeys[0] = "\"" + networkPass + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        } else if (type.equals("wpa")) {
            // wpa
            conf.preSharedKey = "\"" + networkPass + "\"";
        } else if (type.equals("open")) {
            // open
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
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
}
