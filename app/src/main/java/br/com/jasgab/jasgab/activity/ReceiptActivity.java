package br.com.jasgab.jasgab.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model_http.ResponseDefault;
import br.com.jasgab.jasgab.util.JasgabUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptActivity extends AppCompatActivity {
    public static int GALLERY = 1, CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        JasgabUtils.hideActionBar(getSupportActionBar());
        JasgabUtils.setActionBar("Comprovante", getWindow().getDecorView(), this);

        setLayout();
        businessRules();
    }

    ImageView receipt_image;
    RelativeLayout receipt_confirm;
    ProgressBar receipt_confirm_progressbar;
    private void setLayout(){
        receipt_image = findViewById(R.id.receipt_image);
        receipt_confirm = findViewById(R.id.receipt_confirm);
        receipt_confirm_progressbar = findViewById(R.id.receipt_confirm_progressbar);
    }

    Customer customer;
    Bill bill;
    int bill_position = 0;
    private void businessRules(){
        customer = CustomerDAO.start(this).selectCustomer();
        bill = JasgabUtils.orderBills(CustomerDAO.start(this).selectBills()).get(bill_position);

        int receipt_type = GALLERY;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            receipt_type = bundle.getInt("receipt_type");
            bill_position = bundle.getInt("bill_position");
        }


        if (receipt_type == GALLERY){
            choosePhotoFromGallery();
        }else if (receipt_type == CAMERA){
            takePhotoFromCamera();
        }

        receipt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receipt_confirm_progressbar.setVisibility(View.VISIBLE);
                sendImage(path);
            }
        });
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    Uri imageUri;
    private void takePhotoFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Comprovante JASGAB " + bill.getDueDate() + " Data da foto:" + Calendar.getInstance().getTimeInMillis());
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA);
    }

    String path;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            finish();
            return;
        }

        try {
            if (data != null) {
                if (requestCode == GALLERY) {
                    Uri contentURI = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    showImage(bitmap);
                    path = saveImage(bitmap);
                    return;
                } else if (requestCode == CAMERA) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    showImage(bitmap);
                    path = getRealPathFromURI(imageUri);
                    sendImage(path);
                    return;
                }
            }

            //TODO ERROR
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO ERROR
        }
    }

    private void showImage(Bitmap bitmap){
        receipt_image.setImageBitmap(bitmap);
        receipt_image.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private static final String IMAGE_DIRECTORY = "/demonuts";
    private String saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {
            if(!wallpaperDirectory.mkdirs()){
                return "";
            }
        }

        try {
            File f = new File(wallpaperDirectory, "jasgab_"+ Calendar.getInstance().getTimeInMillis() + ".png");
            if(f.createNewFile()) {
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                MediaScannerConnection.scanFile(this,
                        new String[]{f.getPath()},
                        new String[]{"image/png"}, null);
                fo.close();

                return f.getAbsolutePath();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        }
        return res;
    }

    private void sendImage(String path){
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
            Call<ResponseDefault> call = JasgabApi.receipt(cpf, due_date, receipt, auth.getToken());
            call.enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(@NonNull Call<ResponseDefault> call, @NonNull Response<ResponseDefault> response) {
                    ResponseDefault responseDefault = response.body();
                    if (responseDefault != null && responseDefault.getStatus()) {
                        receipt_confirm_progressbar.setVisibility(View.INVISIBLE);
                        finish();
                        //TODO ENVIADO COM SUCESSO
                    }
                    //TODO ERRO AO ENVIAR
                }

                @Override
                public void onFailure(@NonNull Call<ResponseDefault> call, @NonNull Throwable t) {
                    receipt_confirm_progressbar.setVisibility(View.INVISIBLE);

                    //TODO ERRO AO ENVIAR
                }
            });
        } catch (Exception ignored){
            receipt_confirm_progressbar.setVisibility(View.INVISIBLE);

            //TODO ERRO AO ENVIAR
        }

    }
}