package br.com.jasgab.jasgab.crud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.jasgab.jasgab.model.Auth;

public class StatusDAO {

    private static SQLiteDatabase db;

    static final String table = "status";
    static final String status = "status";

    public StatusDAO(Context context){
        JasgabDB jasgabDB = new JasgabDB(context);
        db = jasgabDB.getWritableDatabase();
    }

    public static StatusDAO start(Context context){
        return new StatusDAO(context);
    }

    public void insert(Integer item){
        delete();

        ContentValues values = new ContentValues();
        values.put(status, item);

        db.insert(table, null, values);
    }

    public Integer select(){
        try{
            Cursor cursor = db.query(table,
                    new String[]{status},
                    null , null,null,null, null, null);

            int item = 0;

            if(cursor.moveToLast()) {
                item = cursor.getInt(0);
            }

            return item;
        }catch (SQLiteException e){
            return 0;
        }
    }

    public void delete(){
        db.delete(table, null, null);
    }
}
