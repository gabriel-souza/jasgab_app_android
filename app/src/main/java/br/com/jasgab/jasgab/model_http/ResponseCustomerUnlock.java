package br.com.jasgab.jasgab.model_http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCustomerUnlock {

    @SerializedName("expire_date")
    @Expose
    private String expireDate;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
