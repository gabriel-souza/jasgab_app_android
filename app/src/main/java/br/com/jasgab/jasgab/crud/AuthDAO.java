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

public class AuthDAO {

    private static SQLiteDatabase db;

    static final String table = "auths";
    static final String expire = "expire";
    static final String token = "token";

    public AuthDAO(Context context){
        JasgabDB jasgabDB = new JasgabDB(context);
        db = jasgabDB.getWritableDatabase();
    }

    public static AuthDAO start(Context context){
        return new AuthDAO(context);
    }

    public void insert(Auth auth){
        delete();

        ContentValues values = new ContentValues();
        values.put(expire, auth.getExpireDate());
        values.put(token, auth.getToken());

        db.insert(table, null, values);
    }

    public Auth select(){
        try{
            Cursor cursor = db.query(table,
                    new String[]{expire, token},
                    null , null,null,null, null, null);

            Auth auth = null;

            if(cursor.moveToLast()) {
                auth = new Auth();
                auth.setExpireDate(cursor.getString(0));
                auth.setToken(cursor.getString(1));
            }

            if(auth != null) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date now = new Date();

                if (now.before(format.parse(auth.getExpireDate()))) {
                    return auth;
                }
            }
        }catch (SQLiteException | ParseException e){
            return null;
        }
        return null;
    }

    private void delete(){
        db.delete(table, null, null);
    }
}
