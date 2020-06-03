package br.com.jasgab.jasgab.crud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.jasgab.jasgab.api.JasgabUtils;
import br.com.jasgab.jasgab.model.Bill;
import br.com.jasgab.jasgab.model.Connection;
import br.com.jasgab.jasgab.model.Contract;
import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model.CustomerData;
import br.com.jasgab.jasgab.model.ResponseCustomer;

public class UnlockDAO {
    private SQLiteDatabase db;
    private Context context;

    static final String table = "unlock";
    static final String bill_id = "bill_id";

    private UnlockDAO(Context context){
        JasgabDB jasgabDB = new JasgabDB(context);
        this.context = context;
        db = jasgabDB.getWritableDatabase();
    }

    public static UnlockDAO start(Context context){
        return new UnlockDAO(context);
    }

    public boolean select(){
        Cursor cursor = db.query(table, new String[] {this.bill_id},null,null,null,null,null, null);

        boolean lock = false;

        if(cursor.moveToLast()) {
            Bill bill = JasgabUtils.actualBill(CustomerDAO.start(context).selectBills());
            if (bill == null){
                return false;
            }

            lock = bill.getId() == cursor.getInt(0);
        }

        return lock;
    }

    public void insert(int bill_id){
        delete();
        ContentValues values = new ContentValues();
        values.put(this.bill_id, bill_id);
        db.insert(table, null, values);
    }


    private void delete(){
        db.delete(table, null,null);
    }
}
