package br.com.jasgab.jasgab.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.tomer.fadingtextview.FadingTextView;

import java.util.Objects;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model_http.RequestCustomer;
import br.com.jasgab.jasgab.model_http.ResponseCustomer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tomer.fadingtextview.FadingTextView.MINUTES;

public class LoadingActivity extends AppCompatActivity {
    Context context;
    public static final int LOGIN = 1, SIGNUP = 2;
    int display = LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        context = this;
        display = Objects.requireNonNull(getIntent().getExtras()).getInt("loading", LOGIN);

        Objects.requireNonNull(getSupportActionBar()).hide();

        startTextAnimation();
        startCluodAnimation();

        switch (display){
            case LOGIN:
                getCustomerData();
                break;
            case SIGNUP:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        goToBegin();
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
                loading_title.setTimeout(60, MINUTES);
                loading_title_array = getResources().getStringArray(R.array.loading_title_login);
                loading_sub_title_array = getResources().getStringArray(R.array.loading_sub_title_login);
                break;
            case SIGNUP:
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

    private void getCustomerData(){
        Auth auth = AuthDAO.start(this).select();
        //If auth not found on DB return to LaunchPage
        if(auth == null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            return;
        }

        Customer customer = CustomerDAO.start(this).selectCustomer();
        //If customer not found on DB return to Login
        if(customer == null){
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
            return;
        }

        final String cpf = customer.getCpf();

        Call<ResponseCustomer> call = new JasgabApi().customer_data(new RequestCustomer(cpf), auth.getToken());
        call.enqueue(new Callback<ResponseCustomer>() {
            @Override
            public void onResponse(Call<ResponseCustomer> call, Response<ResponseCustomer> response) {
                ResponseCustomer responseCustomer = response.body();
                if(responseCustomer != null){
                    if(responseCustomer.getStatus()){
                        responseCustomer.getCustomer().setCpf(cpf);
                        CustomerDAO.start(context).inserir(responseCustomer);
                        customerDataFound();
                    }else{
                        goToBegin();
                    }
                }
                else{
                    goToBegin();
                }
            }

            @Override
            public void onFailure(Call<ResponseCustomer> call, Throwable t) {
                goToBegin();
            }
        });
    }

    private void customerDataFound(){
        startActivity(new Intent(this, HomeActivity.class));
        finishAffinity();
    }

    private void goToBegin(){
        CustomerDAO.start(this).delete();
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }
}
