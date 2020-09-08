package br.com.jasgab.jasgab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import br.com.jasgab.jasgab.NoConnectionActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.list.StatusDeviceAdapter;
import br.com.jasgab.jasgab.model.DeviceWifi;
import br.com.jasgab.jasgab.util.JasgabUtils;

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
        devices.add(new DeviceWifi("0.0.0.0", "FF:FF:FF:FF:FF:FF", "SELF", "SELF"));

        mAdapter = new StatusDeviceAdapter(devices, requireContext());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        SubnetDevices.fromLocalAddress().findDevices(new SubnetDevices.OnSubnetDeviceFound() {
            @Override
            public void onDeviceFound(final Device device) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        devices.add(new DeviceWifi(device.ip, device.mac, "Generico", "Indisponivel"));
                        mAdapter.notifyDataSetChanged();
                        status_online_device_count.post(new Runnable() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void run() {
                                status_online_device_count.setText(String.format("%d", devices.size()));
                            }
                        });
                    }
                });
            }

            @Override
            public void onFinished(ArrayList<Device> devicesFound) {
            }
        });

        return view;
    }

}