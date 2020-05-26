package br.com.jasgab.jasgab.crud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.jasgab.jasgab.model.Maintenance;

public class JasgabDB extends SQLiteOpenHelper {
    private static final String name = "jasgab.db";
    private static final int version = 1;

    JasgabDB(Context context){
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("" +
                "create table "+ AuthDAO.table +" (" +
                AuthDAO.expire+" varchar(64)," +
                AuthDAO.token+" varchar(1024))"
        );

        db.execSQL("" +
                "create table "+CustomerDAO.customers_table+" ("+CustomerDAO.id+" integer," +
                CustomerDAO.address+" varchar(1024)," +
                CustomerDAO.cpf+" varchar(32)," +
                CustomerDAO.email+" varchar(1024)," +
                CustomerDAO.fone+" varchar(32)," +
                CustomerDAO.name+" varchar(1024)," +
                CustomerDAO.neighborhood+" varchar(1024)," +
                CustomerDAO.status+" integer," +
                CustomerDAO.zipCode+" varchar(32))"
        );

        db.execSQL("" +
                "create table "+ CustomerDAO.bills_table+" ("+
                CustomerDAO.id+" integer," +
                CustomerDAO.amount+" double," +
                CustomerDAO.barcode+" varchar(1024)," +
                CustomerDAO.bill_name+" varchar(1024)," +
                CustomerDAO.expiration_date+" varchar(32)," +
                CustomerDAO.path_pdf+" varchar(2048))"
        );

        db.execSQL("" +
                "create table "+ CustomerDAO.connections_table+"("+
                CustomerDAO.id+" integer," +
                CustomerDAO.address+" varchar(1024)," +
                CustomerDAO.contract_id+" integer," +
                CustomerDAO.blocked+" integer," +
                CustomerDAO.blocking_reason+" varchar(1024)," +
                CustomerDAO.customer_since+" varchar(32)," +
                CustomerDAO.latitude+" varchar(32)," +
                CustomerDAO.longitude+" varchar(32)," +
                CustomerDAO.mac_address+" varchar(64)," +
                CustomerDAO.username+" varchar(512))"
        );

        db.execSQL("" +
                "create table "+ CustomerDAO.contracts_table+"("+
                CustomerDAO.id+" integer," +
                CustomerDAO.adherence_since+" varchar(32)," +
                CustomerDAO.expiration_date+" varchar(32)," +
                CustomerDAO.plan_ +" varchar(1024))"
        );

        db.execSQL("" +
                "create table "+ StatusDAO.table+"("+
                StatusDAO.status+" integer)"
        );

        db.execSQL("" +
                "create table "+ MaintenanceDAO.table+"("+
                MaintenanceDAO.forecast_return+" integer," +
                MaintenanceDAO.message+" varchar(2048)," +
                MaintenanceDAO.title +" varchar(512))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + AuthDAO.table + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CustomerDAO.customers_table + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CustomerDAO.bills_table + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CustomerDAO.connections_table + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CustomerDAO.contracts_table + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + StatusDAO.table + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + MaintenanceDAO.table + "'");
        onCreate(db);
    }
}
