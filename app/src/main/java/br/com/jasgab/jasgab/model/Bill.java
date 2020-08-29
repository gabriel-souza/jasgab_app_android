package br.com.jasgab.jasgab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bill {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("bill_name")
    @Expose
    private String billName;
    @SerializedName("expiration_date")
    @Expose
    private String expirationDate;
    @SerializedName("path_pdf")
    @Expose
    private String pathPdf;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getDueDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPathPdf() {
        return pathPdf;
    }

    public void setPathPdf(String pathPdf) {
        this.pathPdf = pathPdf;
    }

}