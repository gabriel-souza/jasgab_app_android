package br.com.jasgab.jasgab.model_http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDefault {

    @SerializedName("status")
    @Expose
    private Boolean status;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
