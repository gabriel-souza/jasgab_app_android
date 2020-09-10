package br.com.jasgab.jasgab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Maintenance {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("forecast_return")
    @Expose
    private String forecast_return;
    @SerializedName("maintenance")
    @Expose
    private Boolean maintenance;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("neighborhoods")
    @Expose
    private String neighborhoods;
    @SerializedName("title")
    @Expose
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getForecastReturn() {
        return forecast_return;
    }

    public void setForecastReturn(String forecast_return){
        this.forecast_return = forecast_return;
    }

    public Boolean getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Boolean maintenance) {
        this.maintenance = maintenance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getNeighborhoods() {
        return neighborhoods;
    }

    public void setNeighborhoods(String neighborhoods){
        this.neighborhoods = neighborhoods;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
