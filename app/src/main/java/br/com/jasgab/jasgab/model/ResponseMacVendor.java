package br.com.jasgab.jasgab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMacVendor {


    @SerializedName("vendor")
    @Expose
    private String vendor;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
