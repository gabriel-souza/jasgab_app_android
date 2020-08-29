package br.com.jasgab.jasgab.list;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.com.jasgab.jasgab.R;

public class BillHolder extends RecyclerView.ViewHolder {

    public TextView biil_due_date,
            bill_info,
            bill_price,
            bill_show;

    public BillHolder(View itemView) {
        super(itemView);
        biil_due_date = itemView.findViewById(R.id.biil_due_date);
        bill_info = itemView.findViewById(R.id.bill_info);
        bill_price = itemView.findViewById(R.id.bill_price);
        bill_show = itemView.findViewById(R.id.bill_show);
    }
}