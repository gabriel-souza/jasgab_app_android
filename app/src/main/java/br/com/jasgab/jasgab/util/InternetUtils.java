package br.com.jasgab.jasgab.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

import br.com.jasgab.jasgab.pattern.WifiSignalType;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class InternetUtils {

    public static boolean wifiIsOn(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if(connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(mWifi != null) {
                return mWifi.isConnected() && mWifi.isAvailable();
            }
        }
        return false;
    }

    public static String getWifiName(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getSSID();
        }
        return "Não identificado";
    }

    public static int getWifiSignalLevel(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 100);
        }
        return 0;
    }

    public static int getWifiSignalStatus(int signal){
        if(signal <= 50){
            return WifiSignalType.Bad;
        }
        if(signal <= 70){
            return WifiSignalType.Medio;
        }
        return WifiSignalType.Good;
    }

    public static String getWifiGateway(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null) {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            return toStringIp(dhcpInfo.gateway);
        }
        return "0.0.0.0";
    }

    private static String toStringIp(int ip){
        if(ip == 0){
            return "0.0.0.0";
        }
        return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
    }
}
