package br.com.jasgab.jasgab.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.jasgab.jasgab.BillPdfActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.DeviceWifi;

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
        holder.bill_price.setText(String.format("%s mensal", bills.get(position).getAmount()));

        holder.bill_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BillPdfActivity.class);
                intent.putExtra("bill_position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
         return bills.size();
    }

    public void update(){
        notifyDataSetChanged();
    }

    public void updateList(Bill bill) {
        insertItem(bill);
    }

    private void insertItem(Bill bill) {
        bills.add(bill);
        notifyDataSetChanged();
    }
}