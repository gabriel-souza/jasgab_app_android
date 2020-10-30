package br.com.jasgab.jasgab.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Plan;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class BillActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        context = this;

        JasgabUtils.hideActionBar(getSupportActionBar());
        JasgabUtils.setActionBar("Pagamento", getWindow().getDecorView(), this);
        JasgabUtils.checkAPP(this);

        setLayout();
    }

    private void setLayout(){
        ImageView bill_img = findViewById(R.id.bill_img);
        TextView bill_plan = findViewById(R.id.bill_plan);
        TextView bill_price = findViewById(R.id.bill_price);
        TextView bill_month = findViewById(R.id.bill_month);
        Button bill_barcode = findViewById(R.id.bill_barcode);

        //GET BILL
        List<Bill> bills = JasgabUtils.orderBills(CustomerDAO.start(this).selectBills());
        if(bills == null){
            finishAffinity();
            return;
        }

        Bill bill = bills.get(getIntent().getExtras().getInt("bill_position", 0));
        if(bill == null){
            finishAffinity();
            return;
        }

        //SET DATA ON LAYOUT
        bill_plan.setText(bill.getBillName().replace("Ref.: ", ""));
        bill_price.setText(String.format("R$ %s", bill.getAmount().toString().replace(".",",")));
        bill_month.setText(bill.getDueDate());

        bill_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PayBarcodeActivity.class);
                context.startActivity(intent);
            }
        });

        List<Plan> plans = new Plan().getPlans(this);
        for(Plan plan : plans){
            if(bill.getBillName().contains(plan.getName().replace("PLANO ", ""))){
                bill_img.setBackground(plan.getImage());
            }
        }
    }
}