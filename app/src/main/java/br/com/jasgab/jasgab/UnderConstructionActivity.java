package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.jasgab.jasgab.util.JasgabUtils;

public class UnderConstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_construction);
        JasgabUtils.hideActionBar(getSupportActionBar());

        Button under_construction_button = findViewById(R.id.under_construction_button);
        under_construction_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}