package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model.RequestCustomer;
import br.com.jasgab.jasgab.model.ResponseCustomer;
import br.com.jasgab.jasgab.model.ResponseDefault;
import br.com.jasgab.jasgab.util.JasgabUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {
    private Context context;
    private Bill bill;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        context = this;

        JasgabUtils.hideActionBar(getSupportActionBar());
        JasgabUtils.setActionBar("Pagamento", getWindow().getDecorView(), this);

        requestMultiplePermissions();

        //btn = (Button) findViewById(R.id.btn);
        //imageview = (ImageView) findViewById(R.id.iv);

        Button bill_send_recipe = findViewById(R.id.bill_send_recipe);
        bill_send_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        setLayout();
    }

    private void setLayout(){
        TextView bill_plan = findViewById(R.id.bill_plan);
        TextView bill_price = findViewById(R.id.bill_price);
        TextView bill_month = findViewById(R.id.bill_month);
        Button bill_barcode = findViewById(R.id.bill_barcode);
        Button bill_gpay = findViewById(R.id.bill_gpay);
        Button bill_send_recipe = findViewById(R.id.bill_send_recipe);

        //GET BILL
        List<Bill> bills = JasgabUtils.orderBills(CustomerDAO.start(this).selectBills());
        if(bills == null){
            finishAffinity();
            return;
        }

        customer = CustomerDAO.start(this).selectCustomer();
        bill = bills.get(getIntent().getExtras().getInt("bill_position",0));
        if(bill == null){
            finishAffinity();
            return;
        }

        //SET DATA ON LAYOUT
        bill_plan.setText(bill.getBillName().replace("Ref.: ", ""));
        bill_price.setText(String.format("R$ %s", bill.getAmount().toString().replace(".",",")));
        bill_month.setText(bill.getDueDate());

        bill_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PayBarcodeActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    String path;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    path = saveImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            path = saveImage(thumbnail);
        }

        sendImage(path);
    }

    public void sendImage(String path){
        File file = new File(path);

        MultipartBody.Part receipt = MultipartBody.Part.createFormData("receipt", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        RequestBody cpf = RequestBody.create(MediaType.parse("text/plain"), customer.getCpf());
        RequestBody due_date = RequestBody.create(MediaType.parse("text/plain"), bill.getDueDate().replace("/","."));

        try {
            Auth auth = AuthDAO.start(this).select();
            if(auth == null){
                finishAffinity();
                return;
            }
            Call<ResponseDefault> call = new JasgabApi().receipt(cpf, due_date, receipt, auth.getToken());
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    ResponseDefault responseDefault = response.body();
                    if (responseDefault != null) {
                        if (responseDefault.getStatus()) {
                            // TODO RESPONSE
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {

                }
            });
        } catch (Exception ignored){ }
    }

}