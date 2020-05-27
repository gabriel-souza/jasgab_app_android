package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Objects;

import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;

import br.com.jasgab.jasgab.model.Auth;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Objects.requireNonNull(getSupportActionBar()).hide();

        if(!new JasgabApi().internetOk()){
            //TODO: não conectado a internet
        }

        Auth auth = AuthDAO.start(context).select();
        if(auth != null) {
            finishLoading();
            return;
        }

        auth();
    }

    private void auth(){
        Call<Auth> call = new JasgabApi().auth();
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                Auth auth = response.body();
                if(auth != null) {
                    AuthDAO.start(context).insert(auth);
                    finishLoading();
                }else{
                    AuthDAO.start(context).delete();
                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                AuthDAO.start(context).delete();
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void finishLoading() {
        SharedPreferences prefs = getSharedPreferences(".br.com.jasgab.jasgab", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).apply();
            startActivity(new Intent(this, FirstAccessActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
            return;
        }

        if(CustomerDAO.start(context).selectCustomer() == null){
            startActivity(new Intent(context, LoginActivity.class));
        }else{
            startActivity(new Intent(context, HomeActivity.class));
        }
        finishAffinity();
    }
}