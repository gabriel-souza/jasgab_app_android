package br.com.jasgab.jasgab.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.api.JasgabUtils;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Customer;


public class PayBarcodeFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_barcode, container, false);

        //GET ALL LAYOUT
        TextView barcode_bank = view.findViewById(R.id.barcode_bank);
        TextView barcode_payday_info = view.findViewById(R.id.barcode_payday_info);
        TextView barcode_payday = view.findViewById(R.id.barcode_payday);
        TextView barcode_price = view.findViewById(R.id.barcode_price);
        TextView barcode_person = view.findViewById(R.id.barcode_person);
        TextView barcode_barcode = view.findViewById(R.id.barcode_barcode);

        Button barcode_bill_status = view.findViewById(R.id.barcode_status);
        Button barcode_copy_paste = view.findViewById(R.id.barcode_copy_paste);
        Button barcode_save = view.findViewById(R.id.barcode_save);
        Button barcode_send = view.findViewById(R.id.barcode_send);


        //GET CUSTOMER DATA
        CustomerDAO customerDAO = CustomerDAO.start(requireContext());
        Customer customer = customerDAO.selectCustomer();
        //TODO SELECT BILL BY BUNDLE
        Bill bill = JasgabUtils.actualBill(customerDAO.selectBills());

        if(customer == null || bill == null){
            return view;
        }

        //SET LAYOUT DATA
        //TODO SELECT BANK
        //barcode_bank.setText(selectBank());
        barcode_payday.setText(bill.getExpirationDate());
        int daysToExpire = JasgabUtils.daysToExpire(bill.getExpirationDate());
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

        return view;
    }
}
