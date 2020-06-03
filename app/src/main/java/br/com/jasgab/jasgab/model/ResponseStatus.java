package br.com.jasgab.jasgab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseStatus {

    @SerializedName("internet_status")
    @Expose
    private int internet_status;
    @SerializedName("maintenance")
    @Expose
    private Maintenance maintenance;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public int getInternetStatus() {
        return internet_status;
    }

    public void setInternetStatus(int internet_status) {
        this.internet_status = internet_status;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
