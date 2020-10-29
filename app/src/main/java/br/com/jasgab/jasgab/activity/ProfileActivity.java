package br.com.jasgab.jasgab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Contract;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model_http.ResponseCustomer;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        JasgabUtils.hideActionBar(getSupportActionBar());
        JasgabUtils.setActionBar("Perfil", getWindow().getDecorView(), this);

        setLayout();
        businessRules();
    }

    protected Context context;
    private TextView profile_name, profile_cpf, profile_address_text, profile_email_text, profile_phone_text, profile_plan_text, profile_since_text, profile_logout_text;
    private void setLayout(){
        context = this;
        profile_name = findViewById(R.id.profile_name);
        profile_cpf = findViewById(R.id.profile_cpf);
        profile_address_text = findViewById(R.id.profile_address_text);
        profile_email_text = findViewById(R.id.profile_email_text);
        profile_phone_text = findViewById(R.id.profile_phone_text);
        profile_plan_text = findViewById(R.id.profile_plan_text);
        profile_since_text = findViewById(R.id.profile_since_text);
        profile_logout_text = findViewById(R.id.profile_logout_text);
    }

    private void businessRules(){
        ResponseCustomer responseCustomer = CustomerDAO.start(this).select();
        Customer customer = responseCustomer.getCustomer();
        Contract contract = responseCustomer.getCustomerData().getContracts().get(0);

        profile_name.setText(customer.getName());
        profile_cpf.setText(customer.getCpf());
        profile_address_text.setText(customer.getAddress().replace(" - ", "\n"));
        profile_email_text.setText(customer.getEmail());
        profile_phone_text.setText(customer.getPhone());
        profile_plan_text.setText(contract.getPlan());
        profile_since_text.setText(contract.getAdherenceSince());

        profile_logout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthDAO.start(context).delete();
                CustomerDAO.start(context).delete();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}