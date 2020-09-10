package br.com.jasgab.jasgab.activity;

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
    private AuthDAO authDAO;
    private CustomerDAO customerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JasgabUtils.hideActionBar(getSupportActionBar());

        run();
    }

    private void run(){
        if(!JasgabUtils.checkInternet(this)){
            startActivity(new Intent(this, NoConnectionActivity.class));
            finish();
            return;
        }

        authDAO = new AuthDAO(this);
        customerDAO = new CustomerDAO(this);
        Auth auth = authDAO.select();
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
        authDAO.insert(auth);
        end();
    }

    private void fail(){
        authDAO.delete();
        finish();
        startActivity(getIntent());
    }

    private void end() {
        if(customerDAO.selectCustomer() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }else{
            startActivity(new Intent(this, HomeActivity.class));
        }
        finishAffinity();
    }
}