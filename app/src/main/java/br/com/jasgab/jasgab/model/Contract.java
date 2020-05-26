package br.com.jasgab.jasgab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contract {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("adherence_since")
    @Expose
    private String adherenceSince;
    @SerializedName("expiration_date")
    @Expose
    private String expirationDate;
    @SerializedName("plan")
    @Expose
    private String plan;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdherenceSince() {
        return adherenceSince;
    }

    public void setAdherenceSince(String adherenceSince) {
        this.adherenceSince = adherenceSince;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

}
