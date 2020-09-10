package br.com.jasgab.jasgab.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import br.com.jasgab.jasgab.model.Maintenance;

public class MaintenanceDAO {

    private static SQLiteDatabase db;

    static final String table = "maintenance";
    static final String forecast_return = "forecast_return";
    static final String message = "message";
    static final String title = "title";


    public MaintenanceDAO(Context context){
        JasgabDB jasgabDB = new JasgabDB(context);
        db = jasgabDB.getWritableDatabase();
    }

    public static MaintenanceDAO start(Context context){
        return new MaintenanceDAO(context);
    }

    public void insert(Maintenance maintenance){
        delete();

        ContentValues values = new ContentValues();
        values.put(forecast_return, maintenance.getForecastReturn());
        values.put(message, maintenance.getMessage());
        values.put(title, maintenance.getTitle());

        db.insert(table, null, values);
    }

    public Maintenance select(){
        try{
            Cursor cursor = db.query(table,
                    new String[]{forecast_return, message, title},
                    null , null,null,null, null, null);

            Maintenance maintenance = null;

            if(cursor.moveToLast()) {
                maintenance = new Maintenance();
                maintenance.setForecastReturn(cursor.getString(0));
                maintenance.setMessage(cursor.getString(1));
                maintenance.setTitle(cursor.getString(2));
            }

            return maintenance;
        }catch (SQLiteException e){
            return null;
        }
    }

    private void delete(){
        db.delete(table, null, null);
    }
}
