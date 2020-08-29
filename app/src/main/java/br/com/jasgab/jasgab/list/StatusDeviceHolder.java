package br.com.jasgab.jasgab.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.com.jasgab.jasgab.R;

public class StatusDeviceHolder extends RecyclerView.ViewHolder {

    public ImageView status_online_device_ic;
    public TextView status_online_device_name,
            status_online_device_ip,
            status_online_device_brand,
            status_online_device_mac;

    public StatusDeviceHolder(View itemView) {
        super(itemView);
        status_online_device_ic = itemView.findViewById(R.id.status_online_device_ic);
        status_online_device_name = itemView.findViewById(R.id.status_online_device_name);
        status_online_device_ip = itemView.findViewById(R.id.status_online_device_ip);
        status_online_device_brand = itemView.findViewById(R.id.status_online_device_brand);
        status_online_device_mac = itemView.findViewById(R.id.status_online_device_mac);
    }
}