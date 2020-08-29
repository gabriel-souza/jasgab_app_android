package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.jasgab.jasgab.util.JasgabUtils;

public class NoConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
        JasgabUtils.hideActionBar(getSupportActionBar());

        setLayout();
    }

    private void setLayout(){
        Button no_connection_button = findViewById(R.id.no_connection_button);
        no_connection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternet();
            }
        });
    }

    private void checkInternet(){
        if(JasgabUtils.checkInternet(this)){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        }else{
            TextView no_connection_sub_title = findViewById(R.id.no_connection_sub_title);
            no_connection_sub_title.setTextColor(getResources().getColor(R.color.red));
        }
    }
}