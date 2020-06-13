package br.com.jasgab.jasgab.api;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import br.com.jasgab.jasgab.model.Device;

import static android.content.Context.WIFI_SERVICE;

public class IpUtils {

    private Context mContext;
    public IpUtils(Context context){
        this.mContext = context;
    }

    public void discoveryIP(){
        try {
            int ipWifi = getIpWifi();

            Runtime runtime = Runtime.getRuntime();
            for(int i = 0; i <= 255; i++){
                String host = ipHostToString(ipWifi, i);
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 " + host);
                int exitValue = ipProcess.waitFor();
                if(exitValue == 0){
                    //TODO ADD TO ARRAY
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        //return false;

    }

    public int getNetmaskWifi(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        if(wifiManager != null) {
            return wifiManager.getDhcpInfo().netmask;
        }
        return 0;
    }

    public int getIpWifiDHCP(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        if(wifiManager != null) {
            return wifiManager.getDhcpInfo().ipAddress;
        }
        return 0;
    }

    public int getGatewayWifiDHCP(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        if(wifiManager != null) {
            return wifiManager.getDhcpInfo().gateway;
        }
        return 0;
    }

    public int getIpWifi() {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getIpAddress();
    }

    public String ipToString(int ip){
        return String.format(Locale.getDefault(), "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));
    }

    public String ipHostToString(int ip, int host){
        return String.format(Locale.getDefault(), "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (host & 0xff));
    }

    public String getIpMobile() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ignored) {}
        return null;
    }

    public void getDiscoveryDevices(){
        try {
            List<Device> devices = new ArrayList<>();
            int selfIp = getIpWifi();

            Runtime runtime = Runtime.getRuntime();
            for(int i = 0; i <= 255; i++){
                String ipHost = ipHostToString(selfIp, i);
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 " + ipHost);
                int exitValue = ipProcess.waitFor();
                if(exitValue == 0){
                    //TODO ADD TO ARRAY
                    InetAddress addr = InetAddress.getByName("192.168.1.1");
                    
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
