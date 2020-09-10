package br.com.jasgab.jasgab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.util.JasgabUtils;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Customer;

public class PayBarcodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_barcode);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Set ActionBar
        TextView actionbar_text = findViewById(R.id.actionbar_title);
        actionbar_text.setText("Boleto");
        ImageView actionbar_back = findViewById(R.id.actionbar_back);
        actionbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //GET ALL LAYOUT
        TextView barcode_bank = findViewById(R.id.barcode_bank);
        TextView barcode_payday_info = findViewById(R.id.barcode_payday_info);
        TextView barcode_payday = findViewById(R.id.barcode_payday);
        TextView barcode_price = findViewById(R.id.barcode_price);
        TextView barcode_person = findViewById(R.id.barcode_person);
        TextView barcode_barcode = findViewById(R.id.bill_barcode);

        Button barcode_bill_status = findViewById(R.id.barcode_status);
        Button barcode_copy_paste = findViewById(R.id.barcode_copy_paste);
        Button barcode_save = findViewById(R.id.barcode_save);
        Button barcode_send = findViewById(R.id.barcode_send);

        //GET CUSTOMER DATA
        CustomerDAO customerDAO = CustomerDAO.start(this);
        Customer customer = customerDAO.selectCustomer();
        //TODO SELECT BILL BY BUNDLE
        final Bill bill = JasgabUtils.actualBill(customerDAO.selectBills());

        if(customer == null || bill == null){
            return;
        }

        //SET LAYOUT DATA
        //TODO SELECT BANK
        //barcode_bank.setText(selectBank());
        barcode_payday.setText(bill.getDueDate());
        int daysToExpire = JasgabUtils.daysToExpire(bill.getDueDate());
        if(daysToExpire > 0) {
            barcode_payday_info.setText(String.format("Vence em %d dias", daysToExpire));
        }else if(daysToExpire < 0){
            barcode_payday_info.setText("Atrasado");
        }else{
            barcode_payday_info.setText("Vence hoje");
        }
        barcode_price.setText(String.valueOf(bill.getAmount()));
        barcode_person.setText(customer.getName());
        barcode_barcode.setText(bill.getBarcode());

        barcode_copy_paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("barcode", bill.getBarcode());
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Código de barra copiado!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
