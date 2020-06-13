package br.com.jasgab.jasgab.api;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.jasgab.jasgab.model.Bill;

public class JasgabUtils {

    public static Bill actualBill(List<Bill> bills) {
        if(bills == null || bills.isEmpty()){
            return null;
        }

        Bill bill = bills.get(0);

        for (Bill item : bills) {
            if(JasgabUtils.toDate(bill.getExpirationDate()).after(JasgabUtils.toDate(item.getExpirationDate()))){
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


}
