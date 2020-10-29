package br.com.jasgab.jasgab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.model.DeviceWifi;
import br.com.jasgab.jasgab.util.InternetUtils;
import br.com.jasgab.jasgab.util.JasgabUtils;
import br.com.jasgab.jasgab.util.Pinger;

import static android.content.Context.WIFI_SERVICE;

public class StatusOnlineOverviewFragment extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_status_online_overview, container, false);
        Context context = requireContext();
        JasgabUtils.checkWifiActivity(context, requireActivity());

        loadLayout();
        new Discovery().execute();

        return view;
    }

    TextView status_online_wifi, status_online_devices, status_online_overview_devices_ic, status_online_overview_devices_text;
    private void loadLayout(){
        status_online_wifi = view.findViewById(R.id.status_online_wifi);
        status_online_devices = view.findViewById(R.id.status_online_devices);
        status_online_overview_devices_ic = view.findViewById(R.id.status_online_overview_devices_ic);
        status_online_overview_devices_text = view.findViewById(R.id.status_online_overview_devices_text);

        String wifiName = InternetUtils.getWifiName(requireContext()).replace("\"", "");
        if(wifiName.equals("<unknown ssid>")){
            wifiName = "não encontrado";
        }
        status_online_wifi.setText(String.format(getString(R.string.status_online_wifi), wifiName));
    }

    @SuppressLint("StaticFieldLeak")
    private class Discovery extends AsyncTask<Void, Void, List<DeviceWifi>> {
        String gateway;

        @SuppressLint("SetTextI18n")
        @Override
        protected List<DeviceWifi> doInBackground(Void... voids) {
            gateway = getGatewayIpv4Address();

            int lastdot = gateway.lastIndexOf(".");
            gateway = gateway.substring(0, lastdot);

            status_online_devices.post(new Runnable() {
                @Override
                public void run() {
                    status_online_devices.setText(R.string.status_online_devices_loading);
                }
            });

            return Pinger.getDevicesOnNetwork(gateway);
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(List<DeviceWifi> deviceWifis) {
            super.onPostExecute(deviceWifis);
            final  List<DeviceWifi> list = removeGateway(deviceWifis);
            status_online_devices.post(new Runnable() {
                @Override
                public void run() {
                    status_online_devices.setText(String.format(getString(R.string.status_online_devices_found), list.size()));
                }
            });
            status_online_overview_devices_ic.post(new Runnable() {
                @Override
                public void run() {
                    status_online_overview_devices_ic.setText(String.format("%d", list.size()));
                }
            });
            status_online_overview_devices_text.post(new Runnable() {
                @Override
                public void run() {
                    status_online_overview_devices_text.setText(getString(R.string.status_online_devices_text));
                }
            });
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @SuppressLint("DefaultLocale")
        private String getGatewayIpv4Address() {
            try {
                WifiManager wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(WIFI_SERVICE);
                if (wifiManager != null) {
                    DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                    int ip = dhcpInfo.gateway;
                    return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
                }
                return "";
            } catch (Exception e){
                return "";
            }
        }

        private List<DeviceWifi> removeGateway(List<DeviceWifi> deviceWifis){
            for (DeviceWifi deviceWifi : deviceWifis){
                if(deviceWifi.getIp().equals(gateway)){
                    deviceWifis.remove(deviceWifi);
                }
            }
            return deviceWifis;
        }
    }
}
