package br.com.jasgab.jasgab.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Auth;

import br.com.jasgab.jasgab.util.JasgabUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JasgabUtils.hideActionBar(getSupportActionBar());

        businessRules();
    }

    private void businessRules(){
        if(!JasgabUtils.checkInternet(this)){
            startActivity(new Intent(this, NoConnectionActivity.class));
            finish();
            return;
        }

        if(AuthDAO.start(this).select() != null) {
            end();
            return;
        }

        auth();
    }

    private void auth(){
        try {
            Call<Auth> call = JasgabApi.auth();
            call.enqueue(new Callback<Auth>() {
                @Override
                public void onResponse(@NonNull Call<Auth> call, @NonNull Response<Auth> response) {
                    Auth auth = response.body();
                    if (auth != null) {
                        authSuccess(auth);
                        return;
                    }
                    authFail();
                }

                @Override
                public void onFailure(@NonNull Call<Auth> call, @NonNull Throwable t) {
                    authFail();
                }
            });
        }catch (Exception e){
            authFail();
        }
    }

    private void authSuccess(Auth auth){
        AuthDAO.start(this).insert(auth);
        end();
    }

    private void authFail(){
        AuthDAO.start(this).delete();
        CustomerDAO.start(this).delete();
        startActivity(getIntent());
        finishAffinity();
    }

    private void end() {
        if(CustomerDAO.start(this).selectCustomer() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }else{
            startActivity(new Intent(this, HomeActivity.class));
        }
        finishAffinity();
    }
}