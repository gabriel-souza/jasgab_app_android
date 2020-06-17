package br.com.jasgab.jasgab.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import br.com.jasgab.jasgab.model.Device;
import br.com.jasgab.jasgab.pattern.DeviceType;

import static android.content.Context.WIFI_SERVICE;

public class IpUtil {

    private Context mContext;
    public IpUtil(Context context){
        this.mContext = context;
    }

    public List<Device> Discovery() throws IOException {
        List<Device> devices = new ArrayList<>();
        int networkLenght = 255;
        String self = toStringIp(getIntIp());
        String gateway = getGateway();
        String network = getNetwork();

        for(int i = 0; i <= networkLenght; i++){
            String deviceHost = network+i;
            Device device = findHost(deviceHost);
            if(device != null){
                //device.setType(getDeviceType(self,gateway,deviceHost));
                devices.add(device);
            }
        }
        return devices;
    }

    public int getDeviceType(String self, String gateway, String deviceHost){
        if(deviceHost.equals(self))
            return DeviceType.Self;
        if(deviceHost.equals(gateway))
            return DeviceType.Gateway;

        return DeviceType.Host;
    }

    public String getGateway(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        if(wifiManager != null) {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            return toStringIp(dhcpInfo.gateway);
        }

        return "0.0.0.0";
    }

    @SuppressLint("DefaultLocale")
    public String getNetwork(){
        int ip = getIntIp();
        if(ip == 0){
            return "0.0.0.";
        }
        return String.format("%d.%d.%d.", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff));
    }

    @SuppressLint("DefaultLocale")
    public String toStringIp(int ip){
        if(ip == 0){
            return "0.0.0.0";
        }
        return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
    }

    public int getIntIp(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        if(wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getIpAddress();
        }
        return 0;
    }

    public Device findHost(String host) throws IOException {
        int timeout = 5;
        InetAddress inetAddress = InetAddress.getByName(host);
        if (inetAddress.isReachable(timeout)){
            Device device = new Device();
            //device.setHostname(inetAddress.toString());
            //device.setAddress(inetAddress.getHostAddress());
            //device.setMac(getMacAddressFromHost(inetAddress.getHostAddress()));
            return device;
        }
        return null;
    }

    @SuppressLint("HardwareIds")
    public String getSelfMacAddress(){
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null ) {
            WifiInfo info = wifiManager.getConnectionInfo();
            return info.getMacAddress();
        }
        return "N/A";
    }

    public String getMacAddressFromHost(String host){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted.length >= 4) {
                    String ip = splitted[0];
                    String mac = splitted[3];
                    if (mac.matches("..:..:..:..:..:..")) {
                        if (ip.equalsIgnoreCase(host)) {
                            return mac;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

}
