package br.com.jasgab.jasgab.model_http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import br.com.jasgab.jasgab.model.Maintenance;

public class ResponseMaintenance {

    @SerializedName("maintenance")
    @Expose
    private Maintenance maintenance;
    @SerializedName("status")
    @Expose
    private Boolean status;

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
