package br.com.jasgab.jasgab.model_http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import br.com.jasgab.jasgab.model.Isp;

public class ResponseIsp {


    @SerializedName("isp")
    @Expose
    private Isp isp;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public Isp getIsp() {
        return isp;
    }

    public void setIsp(Isp isp) {
        this.isp = isp;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
