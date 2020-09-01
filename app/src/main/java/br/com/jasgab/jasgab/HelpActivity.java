package br.com.jasgab.jasgab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class HelpActivity extends AppCompatActivity {
    EditText help_name, help_cpf, help_message;
    Button help_send;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setLayout();
    }

    private void setLayout(){
        //--
        activity = this;
        JasgabUtils.hideActionBar(getSupportActionBar());
        JasgabUtils.setActionBar("Ajuda", getWindow().getDecorView(), activity);

        //GET LAYOUT
        help_name = findViewById(R.id.help_name);
        help_cpf = findViewById(R.id.help_cpf);
        help_message = findViewById(R.id.help_message);
        help_send = findViewById(R.id.help_send);

        //GET CUSTOMER
        Customer customer = CustomerDAO.start(this).selectCustomer();

        //SET LAYOUT
        help_name.setText(customer.getName());
        help_cpf.setText(customer.getCpf());

        help_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = help_name.getText().toString();
                String cpf = help_cpf.getText().toString();
                String message = help_message.getText().toString();

                String formattedMessage = String.format("Olá eu sou %s e meu CPF é %s,\n\nMensagem:%s", name, cpf, message);
                JasgabUtils.sendToWhatsapp(activity, formattedMessage);
            }
        });
    }
}
