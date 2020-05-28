package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabUtils;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Bill;

public class BillFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        //GET LAYOUT OBJECTS
        TextView container_text = view.findViewById(R.id.container_text);
        container_text.setText("Pagamento");
        ImageView container_back = view.findViewById(R.id.container_back);
        container_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverviewFragment overviewFragment = new OverviewFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_container, overviewFragment)
                        .commit();
            }
        });

        TextView bill_plan = view.findViewById(R.id.bill_plan);
        TextView bill_price = view.findViewById(R.id.bill_price);
        TextView bill_month = view.findViewById(R.id.bill_month);
        Button bill_barcode = view.findViewById(R.id.bill_barcode);
        Button bill_gpay = view.findViewById(R.id.bill_gpay);
        Button bill_send_recipe = view.findViewById(R.id.bill_send_recipe);

        //GET ACTUAL BILL
        Bill bill = JasgabUtils.actualBill(CustomerDAO.start(requireContext()).selectBills());

        if(bill == null){
            //TODO BILL NOT EXISTS
            return view;
        }

        //SET DATA ON LAYOUT
        bill_plan.setText(bill.getBillName().replace("Ref.: ", ""));
        bill_price.setText(String.format("R$ %s", bill.getAmount()));
        bill_month.setText(bill.getExpirationDate());

        bill_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayBarcodeFragment payBarcodeFragment = new PayBarcodeFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_container, payBarcodeFragment)
                        .commit();
            }
        });



        return view;
    }
}
