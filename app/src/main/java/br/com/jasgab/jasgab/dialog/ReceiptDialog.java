package br.com.jasgab.jasgab.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import java.util.List;
import java.util.Objects;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.activity.ReceiptActivity;

import static br.com.jasgab.jasgab.activity.ReceiptActivity.CAMERA;
import static br.com.jasgab.jasgab.activity.ReceiptActivity.GALLERY;

public class ReceiptDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(requireActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_receipt, null);

        Button receipt_gallery = view.findViewById(R.id.receipt_gallery);
        Button receipt_camera = view.findViewById(R.id.receipt_camera);

        receipt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReceiptActivity(GALLERY);
            }
        });

        receipt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReceiptActivity(CAMERA);
            }
        });

        builder.setView(view);

        return builder.create();
    }

    private void startReceiptActivity(int TYPE){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

            Dexter.withContext(requireContext()).withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @Override public void onPermissionsChecked(MultiplePermissionsReport report) {}
                        @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            showPermissionRationale(token);
                        }
                    }).check();
            return;
        }

        Intent intent = new Intent(requireContext(), ReceiptActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("bill_position", requireActivity().getIntent().getIntExtra("bill_position", 0));
        if(TYPE == GALLERY) {
            bundle.putInt("receipt_type", GALLERY);
        } else if(TYPE == CAMERA){
            bundle.putInt("receipt_type", CAMERA);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void showPermissionRationale(final PermissionToken token) {
        new AlertDialog.Builder(requireContext()).setTitle("Acesso a câmera")
                .setMessage("Para você ter essa disponibilidade você precisa dar acesso a Câmera")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.cancelPermissionRequest();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.continuePermissionRequest();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .show();
    }
}