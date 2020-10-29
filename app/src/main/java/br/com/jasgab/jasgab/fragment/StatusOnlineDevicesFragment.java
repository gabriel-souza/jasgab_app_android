package br.com.jasgab.jasgab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stealthcopter.networktools.SubnetDevices;
import com.stealthcopter.networktools.subnet.Device;

import java.util.ArrayList;
import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.list.StatusDeviceAdapter;
import br.com.jasgab.jasgab.model.DeviceWifi;
import br.com.jasgab.jasgab.util.JasgabUtils;

import static android.content.Context.WIFI_SERVICE;

public class StatusOnlineDevicesFragment extends Fragment {
    View view;
    List<DeviceWifi> devices;
    StatusDeviceAdapter mAdapter;
    TextView status_online_device_count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_status_online_devices, container, false);
        Context context = requireContext();
        JasgabUtils.checkWifiActivity(context, requireActivity());

        RecyclerView mRecyclerView = view.findViewById(R.id.status_online_device_devices);
        status_online_device_count = view.findViewById(R.id.status_online_device_count);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        mRecyclerView.setLayoutManager(layoutManager);

        devices = new ArrayList<>();
        devices.add(new DeviceWifi(getIpAddress(), "", "Própio", ""));

        mAdapter = new StatusDeviceAdapter(devices, requireContext());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        final String gateway = getGatewayAddress();

        SubnetDevices.fromLocalAddress().findDevices(new SubnetDevices.OnSubnetDeviceFound() {
            @Override
            public void onDeviceFound(final Device device) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!gateway.equals(device.ip)) {
                            if (getIpAddress().equals(device.ip)) {
                                devices.get(0).setMac(device.mac);
                                devices.get(0).setBrand("");
                                mAdapter.notifyDataSetChanged();
                                return;
                            }
                            devices.add(new DeviceWifi(device.ip, device.mac, "Dispositivo desconhecido", ""));
                            mAdapter.notifyDataSetChanged();
                            status_online_device_count.post(new Runnable() {
                                @SuppressLint("DefaultLocale")
                                @Override
                                public void run() {
                                    status_online_device_count.setText(String.format("%d", devices.size()));
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onFinished(ArrayList<Device> devicesFound) {
            }
        });

        return view;
    }

    @SuppressLint("DefaultLocale")
    private String getGatewayAddress() {
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

    @SuppressLint("DefaultLocale")
    private String getIpAddress() {
        try {
            WifiManager wifiManager = (WifiManager) requireContext().getApplicationContext().getSystemService(WIFI_SERVICE);
            if (wifiManager != null) {
                DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                int ip = dhcpInfo.ipAddress;
                return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
            }
            return "";
        } catch (Exception e){
            return "";
        }
    }

}