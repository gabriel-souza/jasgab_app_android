package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import br.com.jasgab.jasgab.api.JasgabApi;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private AuthDAO mAuthDAO;
    private CustomerDAO mCustomerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthDAO = new AuthDAO(this);
        mCustomerDAO = new CustomerDAO(this);
        getSupportActionBar().hide();

        run();
    }

    private void run(){
        if(!new JasgabApi().internetOk()){
            //TODO: não conectado a internet
        }

        Auth auth = mAuthDAO.select();
        if(auth != null) {
            end();
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
                    success(auth);
                }else{
                    fail();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                fail();
            }
        });
    }

    private void success(Auth auth){
        mAuthDAO.insert(auth);
        end();
    }

    private void fail(){
        mAuthDAO.delete();
        finish();
        startActivity(getIntent());
    }

    private void end() {
        SharedPreferences prefs = getSharedPreferences(".br.com.jasgab.jasgab", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).apply();
            startActivity(new Intent(this, FirstAccessActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
            return;
        }

        if(mCustomerDAO.selectCustomer() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }else{
            startActivity(new Intent(this, HomeActivity.class));
        }
        finishAffinity();
    }
}