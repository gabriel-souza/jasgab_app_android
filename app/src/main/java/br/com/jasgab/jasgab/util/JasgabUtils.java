package br.com.jasgab.jasgab.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import br.com.jasgab.jasgab.activity.LoadingActivity;
import br.com.jasgab.jasgab.activity.LoginActivity;
import br.com.jasgab.jasgab.activity.MainActivity;
import br.com.jasgab.jasgab.activity.NoConnectionActivity;
import br.com.jasgab.jasgab.R;
import br.com.jasgab.jasgab.crud.AuthDAO;
import br.com.jasgab.jasgab.crud.CustomerDAO;
import br.com.jasgab.jasgab.crud.StatusDAO;
import br.com.jasgab.jasgab.fragment.OverviewFragment;
import br.com.jasgab.jasgab.model.Auth;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.pattern.StatusLayoutType;

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

    public static void hideActionBar(android.app.ActionBar actionBar) {
        if(actionBar != null){
            actionBar.hide();
        }
    }

    public static void checkInternetActivity(Context context, Activity activity) {
        try {
            if(!checkInternet(context)){
                activity.startActivity(new Intent(activity, NoConnectionActivity.class));
                activity.finish();
            }
        } catch (Exception ignored) {}
    }

    public static void checkWifiActivity(Context context, Activity activity) {
        try {
            if(!checkWifi(context)){
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
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

    public static boolean checkWifi(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connManager != null) {
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if(mWifi != null) {
                    return mWifi.isConnected();
                }
            }
            return false;
        } catch (Exception ignored) {
            return false;
        }
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

    public static void setActionBarHome(String title, View view, final Context context, final FragmentActivity fragmentActivity){
        TextView actionbar_title = view.findViewById(R.id.actionbar_title);
        actionbar_title.setText(title);
        ImageView actionbar_back = view.findViewById(R.id.actionbar_back);
        actionbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusDAO.start(context).insert(StatusLayoutType.Overview);
                OverviewFragment overviewFragment = new OverviewFragment();
                fragmentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_container, overviewFragment)
                        .commit();
            }
        });
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

    public static void sendToWhatsapp(Activity activity, String message) {
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=5511982288808&text="+message));
            activity.startActivity(intent);
        }
        catch(Exception e)
        {
            Toast.makeText(activity,"Error/n"+ e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean compareLoginData(String dateRequestSrt) {
        DateTime dateRequest = DateTime.parse(dateRequestSrt, DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss"));
        DateTime dateLate = DateTime.now().minusHours(-6);

        return dateLate.isBefore(dateRequest);
    }

    public static void checkAPP(Activity activity){
        try {
            if(!checkInternet(activity)){
                activity.startActivity(new Intent(activity, NoConnectionActivity.class));
                activity.finishAffinity();
            }

            Auth auth = AuthDAO.start(activity).select();
            if(auth == null){
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finishAffinity();
                return;
            }

            Customer customer = CustomerDAO.start(activity).selectCustomer();
            if(customer == null){
                activity.startActivity(new Intent(activity, LoginActivity.class));
                activity.finishAffinity();
                return;
            }

            if(JasgabUtils.compareLoginData(customer.getDateRequest())){
                Intent intent = new Intent(activity, LoadingActivity.class);
                intent.putExtra("loading", LoadingActivity.LOGIN);
                activity.startActivity(intent);
                activity.finishAffinity();
            }
        } catch (Exception ignored) {
            AuthDAO.start(activity).delete();
            CustomerDAO.start(activity).delete();
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finishAffinity();
        }
    }
}
