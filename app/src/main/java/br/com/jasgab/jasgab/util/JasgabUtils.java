package br.com.jasgab.jasgab.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.dialog.NoConnectionDialog;
import br.com.jasgab.jasgab.model.Bill;

public class JasgabUtils {

    public static Bill actualBill(List<Bill> bills) {
        if(bills == null || bills.isEmpty()){
            return null;
        }

        Bill bill = bills.get(0);

        for (Bill item : bills) {
            if(JasgabUtils.toDate(bill.getDueDate()).after(JasgabUtils.toDate(item.getDueDate()))){
                bill = item;
            }
        }

        return bill;
    }

    public static int daysToExpire(String data){
        if(data == null) {
            return -1;
        }
        Date date = JasgabUtils.toDate(data);
        long diff = date.getTime() - new Date().getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int percentageExpireDate(int expire_date){
        Calendar calendar = new GregorianCalendar(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return (daysInMonth - (int) expire_date) * 100 / 30;
    }

    private static Date toDate(String date){
        try{
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static void hideActionBar(ActionBar actionBar) {
        if(actionBar != null){
            actionBar.hide();
        }
    }

    public static void checkInternetDialog(Context context, FragmentActivity fragmentActivity) {
        try {
            if(!checkInternet(context)){
                NoConnectionDialog dialog = new NoConnectionDialog();
                dialog.show(fragmentActivity.getSupportFragmentManager(), "");
            }
        } catch (Exception ignored) {}
    }

    public static boolean checkInternet(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null) {
                    return networkInfo.isConnected();
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    public static void setActionBar(String title, final View view, final Activity activity){
        TextView actionbar_title = view.findViewById(R.id.actionbar_title);
        actionbar_title.setText(title);
        ImageView actionbar_back = view.findViewById(R.id.actionbar_back);
        actionbar_back.setVisibility(View.VISIBLE);
        actionbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public static void setActionBar(String title, View view){
        TextView actionbar_title = view.findViewById(R.id.actionbar_title);
        actionbar_title.setText(title);
    }

    public static List<Bill> orderBills(List<Bill> bills) {
        Collections.sort(bills, new Comparator<Bill>() {
            @SuppressLint("SimpleDateFormat")
            public int compare(Bill o1, Bill o2) {
                Date date1 = new Date();
                Date date2 = new Date();
                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(o1.getDueDate());
                    date2 = new SimpleDateFormat("dd/MM/yyyy").parse(o2.getDueDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(date1 == null){
                    date1 = new Date();
                    date2 = new Date();
                }
                return date1.compareTo(date2);
            }
        });
        return bills;
    }

}