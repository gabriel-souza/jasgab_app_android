
package br.com.jasgab.jasgab.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Auth {

    @SerializedName("expire_date")
    @Expose
    private String expire_date;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("status")
    @Expose
    private String status;

    public String getExpireDate() {
        return expire_date;
    }

    public void setExpireDate(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
