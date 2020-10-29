package br.com.jasgab.jasgab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        try {
            if(adherenceSince == null)
                return;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
            Date date = format.parse(adherenceSince);
            if(date == null)
                return;

            format = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);
            adherenceSince = format.format(date);

            this.adherenceSince = adherenceSince;
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
