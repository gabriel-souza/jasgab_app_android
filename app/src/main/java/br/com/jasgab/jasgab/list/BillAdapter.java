package br.com.jasgab.jasgab.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.jasgab.jasgab.BillActivity;
import br.com.jasgab.jasgab.BillPdfActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.DeviceWifi;
import br.com.jasgab.jasgab.pattern.BillType;
import br.com.jasgab.jasgab.util.JasgabUtils;

public class BillAdapter extends RecyclerView.Adapter<BillHolder> {

    public List<Bill> bills;
    Context context;

    public BillAdapter(List<Bill> bills, Context context) {
        this.bills = bills;
        this.context = context;
    }

    @Override
    public BillHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BillHolder(LayoutInflater.from(context).inflate(R.layout.recicleview_bill, parent, false));
    }

    @Override
    public void onBindViewHolder(BillHolder holder, final int position) {
        holder.biil_due_date.setText(bills.get(position).getDueDate());
        holder.bill_price.setText(String.format("R$ %s %s", bills.get(position).getAmount().toString().replace(".",","), getBillType(position)));

        holder.bill_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BillPdfActivity.class);
                intent.putExtra("bill_position", position);
                context.startActivity(intent);
            }
        });

        holder.bill_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BillActivity.class);
                intent.putExtra("bill_position", position);
                context.startActivity(intent);
            }
        });

        int daysToExpire = JasgabUtils.daysToExpire(bills.get(position).getDueDate());
        if(daysToExpire < 0){
            holder.bill_expired.setVisibility(View.VISIBLE);
        }else{
            holder.bill_expired.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
         return bills.size();
    }

    public void update(){
        notifyDataSetChanged();
    }

    public void insert(Bill bill) {
        bills.add(bill);
        notifyDataSetChanged();
    }

    public String getBillType(int position){
        if(bills.size() == 1 || bills.size() == position+1){
            return BillType.MENSAL;
        }

        if(bills.get(position).getAmount() <  bills.get(position+1).getAmount())
            return BillType.PROPORCIONAL;

        if(bills.get(position).getAmount() >  bills.get(position+1).getAmount())
            return BillType.ADESAO;

        return BillType.MENSAL;
    }


}