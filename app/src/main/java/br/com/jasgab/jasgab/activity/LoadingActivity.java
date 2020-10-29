package br.com.jasgab.jasgab.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.tomer.fadingtextview.FadingTextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model_http.RequestCustomer;
import br.com.jasgab.jasgab.model_http.ResponseCustomer;
import br.com.jasgab.jasgab.util.JasgabUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.concurrent.TimeUnit.MINUTES;

public class LoadingActivity extends AppCompatActivity {
    public static final int LOGIN = 1, SIGN = 2;
    int display = LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        display = Objects.requireNonNull(getIntent().getExtras()).getInt("loading", LOGIN);

        JasgabUtils.hideActionBar(getSupportActionBar());

        businessRules();
    }

    private void businessRules(){
        startTextAnimation();
        startCluodAnimation();

        switch (display){
            case LOGIN:
                loading();
                break;
            case SIGN:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        loadingError();
                    }
                }, 20000);
                break;
        }
    }

    private void startTextAnimation(){
        FadingTextView loading_title = findViewById(R.id.loading_title);
        FadingTextView loading_sub_title = findViewById(R.id.loading_sub_title);
        String[] loading_title_array = getResources().getStringArray(R.array.loading_title_login);
        String[] loading_sub_title_array = getResources().getStringArray(R.array.loading_sub_title_login);

        switch (display){
            case LOGIN:
                loading_title.setTimeout(2, MINUTES);
                loading_title_array = getResources().getStringArray(R.array.loading_title_login);
                loading_sub_title_array = getResources().getStringArray(R.array.loading_sub_title_login);
                break;
            case SIGN:
                loading_title_array = getResources().getStringArray(R.array.loading_title_sign_up);
                loading_sub_title_array = getResources().getStringArray(R.array.loading_sub_title_sign_up);
                break;
        }

        loading_title.setTexts(loading_title_array);
        loading_sub_title.setTexts(loading_sub_title_array);
    }

    private void startCluodAnimation(){
        ImageView cloud_a = findViewById(R.id.nuvemA);
        ImageView cloud_b = findViewById(R.id.nuvemB);
        ImageView cloud_c = findViewById(R.id.nuvemC);
        ImageView cloud_d = findViewById(R.id.nuvemD);

        ObjectAnimator animatorA = ObjectAnimator.ofFloat(cloud_a, "x",-200f, 650f, -200f);
        animatorA.setRepeatCount(ObjectAnimator.INFINITE);
        animatorA.setDuration(8000);
        animatorA.setInterpolator(new LinearInterpolator());

        ObjectAnimator animatorB = ObjectAnimator.ofFloat(cloud_b, "x",800f, -100f, 800f);
        animatorB.setRepeatCount(ObjectAnimator.INFINITE);
        animatorB.setDuration(12000);
        animatorB.setInterpolator(new LinearInterpolator());

        ObjectAnimator animatorC = ObjectAnimator.ofFloat(cloud_c, "x",-100f, 700f, -100f);
        animatorC.setRepeatCount(ObjectAnimator.INFINITE);
        animatorC.setDuration(11000);
        animatorC.setInterpolator(new LinearInterpolator());

        ObjectAnimator animatorD = ObjectAnimator.ofFloat(cloud_d, "x",850f, -250f, 850f);
        animatorD.setRepeatCount(ObjectAnimator.INFINITE);
        animatorD.setDuration(9000);
        animatorD.setInterpolator(new LinearInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorA, animatorB, animatorC, animatorD);
        animatorSet.start();
    }

    private void loading(){
        if(AuthDAO.start(this).select() == null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            return;
        }

        if(CustomerDAO.start(this).selectCustomer() == null){
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
            return;
        }

        Call<ResponseCustomer> call = new JasgabApi().customer_data(new RequestCustomer(CustomerDAO.start(this).selectCustomer().getCpf()), AuthDAO.start(this).select().getToken());
        call.enqueue(new Callback<ResponseCustomer>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCustomer> call, @NonNull Response<ResponseCustomer> response) {
                ResponseCustomer responseCustomer = response.body();
                if(responseCustomer != null && responseCustomer.getStatus()){
                    loadingSuccess(responseCustomer);
                    return;
                }
                loadingError();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCustomer> call, @NonNull Throwable t) {
                loadingError();
            }
        });
    }

    private void loadingSuccess(ResponseCustomer responseCustomer){
        responseCustomer.getCustomer().setCpf(CustomerDAO.start(this).selectCustomer().getCpf());
        responseCustomer.getCustomer().setDateRequest(DateTime.now().toString(DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")));
        CustomerDAO.start(this).inserir(responseCustomer);

        finishAffinity();
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void loadingError(){
        AuthDAO.start(this).delete();
        CustomerDAO.start(this).delete();

        finishAffinity();
        startActivity(new Intent(this, MainActivity.class));
    }

}
