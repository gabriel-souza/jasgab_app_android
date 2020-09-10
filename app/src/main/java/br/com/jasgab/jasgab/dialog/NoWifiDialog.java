package br.com.jasgab.jasgab.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import br.com.jasgab.jasgab.R;

public class NoWifiDialog extends DialogFragment {
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = Objects.requireNonNull(requireActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_no_wifi, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        builder.setView(view).setPositiveButton("ATIVAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WifiManager wifi = (WifiManager) requireContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if(wifi != null) {
                    wifi.setWifiEnabled(true);
                }
            }
        });

        builder.setView(view).setNeutralButton("CONFIGURAÇÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
            }
        });

        final AlertDialog dialog =  builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                Typeface typeface = Typeface.createFromAsset(requireContext().getAssets(), "segoe_ui.ttf");
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setTypeface(typeface);

                Button neutral = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                neutral.setTypeface(typeface);
            }
        });

        dialog.show();

        return dialog;
    }
}
