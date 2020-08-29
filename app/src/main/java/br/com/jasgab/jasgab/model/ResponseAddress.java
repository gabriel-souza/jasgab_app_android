package br.com.jasgab.jasgab.model;
import org.json.*;
import java.util.*;
import com.google.gson.annotations.SerializedName;


public class ResponseAddress{

    @SerializedName("address")
    private String address;
    @SerializedName("city")
    private String city;
    @SerializedName("code")
    private String code;
    @SerializedName("district")
    private String district;
    @SerializedName("ok")
    private boolean ok;
    @SerializedName("state")
    private String state;
    @SerializedName("status")
    private int status;
    @SerializedName("statusText")
    private String statusText;

    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setDistrict(String district){
        this.district = district;
    }
    public String getDistrict(){
        return this.district;
    }
    public void setOk(boolean ok){
        this.ok = ok;
    }
    public boolean isOk(){
        return this.ok;
    }
    public void setState(String state){
        this.state = state;
    }
    public String getState(){
        return this.state;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
}