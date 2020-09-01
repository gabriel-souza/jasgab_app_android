package br.com.jasgab.jasgab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.model.DeviceWifi;
import br.com.jasgab.jasgab.util.JasgabUtils;
import br.com.jasgab.jasgab.util.Pinger;
import br.com.jasgab.jasgab.util.InternetUtils;

import static android.content.Context.WIFI_SERVICE;

public class StatusOnlineOverviewFragment extends Fragment {

    TextView status_online_wifi, status_online_devices, status_online_overview_devices_ic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_online_overview, container, false);

        Context context = getContext();
        if(context == null){
            return view;
        }

        status_online_wifi = view.findViewById(R.id.status_online_wifi);
        status_online_devices = view.findViewById(R.id.status_online_devices);
        status_online_overview_devices_ic = view.findViewById(R.id.status_online_overview_devices_ic);

        status_online_wifi.setText(String.format("Wifi: %s", InternetUtils.getWifiName(context)));

        new Discovery().execute();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class Discovery extends AsyncTask<Void, Void, List<DeviceWifi>> {

        @SuppressLint("SetTextI18n")
        @Override
        protected List<DeviceWifi> doInBackground(Void... voids) {
            String ipString = getGatewayIpv4Address();

            if (ipString == null) {
                return new ArrayList<DeviceWifi>(1);
            }
            int lastdot = ipString.lastIndexOf(".");
            ipString = ipString.substring(0, lastdot);

            //status_online_devices.setText("Carregando...");

            return Pinger.getDevicesOnNetwork(ipString);
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(List<DeviceWifi> deviceWifis) {
            super.onPostExecute(deviceWifis);

            try {
                status_online_devices.setText(String.format("%d Aparelhos Conectados.", deviceWifis.size()));
                status_online_overview_devices_ic.setText(String.format("%d", deviceWifis.size()));
            }catch (Exception e) {
                status_online_devices.setText(String.format("%d Aparelhos Conectados.", deviceWifis.size()));
                //ic_status_online_overview_devices.setText(0);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @SuppressLint("DefaultLocale")
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

}
