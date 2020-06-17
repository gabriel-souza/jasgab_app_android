package br.com.jasgab.jasgab.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.Pinger;
import br.com.jasgab.jasgab.model.Device;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.WIFI_SERVICE;

public class FAQFragment extends Fragment {
    View view;
    TextView textView;
    String test;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_faq, container, false);

        textView = view.findViewById(R.id.textView3);
        test ="";
        textView.setText(text);

        //new Discovery().execute();
        //WifiSinal();
        rescan();
        return view;
    }

    private void rescan(){
        ConnectivityManager connManager = (ConnectivityManager) requireContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() && mWifi.isAvailable()){
            AsyncScan scan = new AsyncScan();
            scan.execute();
        }
    }

    private class AsyncScan extends AsyncTask<Void, Void, List<Device>> {

        @Override
        protected List<Device> doInBackground(Void... voids) {
            String ipString = getGatewayIpv4Address();

            if (ipString == null) {
                return new ArrayList<Device>(1);
            }
            int lastdot = ipString.lastIndexOf(".");
            ipString = ipString.substring(0, lastdot);

            return Pinger.getDevicesOnNetwork(ipString);
        }

        @Override
        protected void onPostExecute(List<Device> devices) {
            super.onPostExecute(devices);

            String text = "";
            for(Device device : devices){
                text += "Hostname: " + device.getDeviceName() +  "MAC: " + device.getMacAddress() + " Address: " + device.getIpAddress() + " Type: " +  "\n";
            }

            textView.setText(text);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        public String getGatewayIpv4Address() {
            WifiManager wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(WIFI_SERVICE);
            if(wifiManager != null) {
                DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                int ip = dhcpInfo.gateway;
                return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
            }
            return "";
        }
    }

    String text;
    private int WifiSinal(){
        WifiManager wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        text = wifiInfo.getBSSID(); // MAC WIFI

        text += "\n SSID " + wifiInfo.getSSID(); // NOME DA REDE
        text += "\n FREQ " + wifiInfo.getFrequency(); // FREQUENCIA

        text += "\n Level " + WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 100); // Qualidade do sinal
        text += "\n Signal " +wifiInfo.getRssi(); // Sinal dbm




        scanWifi();

        textView.setText(text);
        return 0;
    }

    private WifiManager wifiManager;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();

    private void scanWifi() {
        wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        arrayList.clear();
        requireContext().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        //Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            requireContext().unregisterReceiver(this);
            int i = 0;
            for (ScanResult scanResult : results) {
                //arrayList.add(scanResult.SSID + " - " + scanResult.capabilities);

                text += "\n SCAN I: " + i;
                text += "\n SCAN SSID: "+ scanResult.SSID;
                text += "\n SCAN CAP: "+ scanResult.capabilities;
                text += "\n SCAN FREQ: "+ scanResult.frequency;
                text += "\n SCAN BSSID: "+ scanResult.BSSID;
                text += "\n SCAN Level : "+scanResult.level;

                i++;
            }

            textView.setText(text);
        }
    };
}
