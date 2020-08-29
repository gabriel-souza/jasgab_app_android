package br.com.jasgab.jasgab.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.model.DeviceWifi;

public class StatusDeviceAdapter extends RecyclerView.Adapter<StatusDeviceHolder> {

    public List<DeviceWifi> devices;
    Context context;

    public StatusDeviceAdapter(List<DeviceWifi> devices, Context context) {
        this.devices = devices;
        this.context = context;
    }

    @Override
    public StatusDeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StatusDeviceHolder(LayoutInflater.from(context).inflate(R.layout.recicleview_status_online_device, parent, false));
    }

    @Override
    public void onBindViewHolder(StatusDeviceHolder holder, int position) {
        holder.status_online_device_name.setText("Generic");
        holder.status_online_device_ip.setText(devices.get(position).getIp());
        holder.status_online_device_brand.setText(devices.get(position).getBrand());
        holder.status_online_device_mac.setText(devices.get(position).getMac());
    }

    @Override
    public int getItemCount() {
         return devices.size();
    }

    public void updateList(DeviceWifi user) {
        insertItem(user);
    }

    private void insertItem(DeviceWifi deviceWifi) {
        devices.add(deviceWifi);
        notifyDataSetChanged();
    }

}