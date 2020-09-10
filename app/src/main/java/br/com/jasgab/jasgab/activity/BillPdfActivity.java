package br.com.jasgab.jasgab.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import java.util.List;
import java.util.Objects;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class BillPdfActivity extends AppCompatActivity {
    ImageView actionbar_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_pdf);

        setLayout();
    }

    @SuppressLint({"SetJavaScriptEnabled", "UseCompatLoadingForDrawables"})
    public void setLayout(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            finish();
            return;
        }

        int bill_position = bundle.getInt("bill_position");
        List<Bill> bills = new CustomerDAO(this).selectBills();
        bills = JasgabUtils.orderBills(bills);
        Bill bill = bills.get(bill_position);

        JasgabUtils.hideActionBar(getSupportActionBar());
        JasgabUtils.setActionBar("Fatura " + bill.getDueDate(), this.getWindow().getDecorView(), this);
        final String pdf = bill.getPathPdf();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando...");
        progressDialog.setCancelable(false);
        WebView web_view = findViewById(R.id.bill_pdf);
        //web_view.requestFocus();
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);
        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
            }
        });
        progressDialog.show();

        //SHARE BUTTON
        actionbar_button = findViewById(R.id.actionbar_button);
        actionbar_button.setBackground(getDrawable(R.drawable.ic_share));
        actionbar_button.setVisibility(View.VISIBLE);
        actionbar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "JASGAB TELECOM");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, pdf);
                startActivity(Intent.createChooser(sharingIntent, "Compartilhar"));
            }
        });
    }
}