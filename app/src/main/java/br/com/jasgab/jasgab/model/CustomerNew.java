package br.com.jasgab.jasgab.model;

import com.google.gson.annotations.SerializedName;

public class CustomerNew {

    final static int SIGNUP = 0;
    final static int UPDATEPLAN = 1;

    @SerializedName("address_number")
    private String address_number;
    @SerializedName("born")
    private String born;
    @SerializedName("cpf")
    private String cpf;
    @SerializedName("complement")
    private String complement;
    @SerializedName("due_date")
    private String due_date;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("payment_method")
    private String payment_method;
    @SerializedName("phone")
    private String phone;
    @SerializedName("plan")
    private String plan;
    @SerializedName("rg")
    private String rg;
    @SerializedName("type")
    private Integer type;
    @SerializedName("zip_code")
    private String zip_code;

    public CustomerNew() {
        this.address_number = "";
        this.born = "";
        this.complement = "";
        this.cpf = "";
        this.due_date = "";
        this.email = "";
        this.name = "";
        this.payment_method = "";
        this.phone = "";
        this.plan = "";
        this.rg = "";
        this.type = -1;
        this.zip_code = "";
    }

    public CustomerNew(String address_number,
                       String born,
                       String complement,
                       String cpf,
                       String due_date,
                       String email,
                       String name,
                       String payment_method,
                       String phone,
                       String plan,
                       String rg,
                       String zip_code) {
        this.address_number = address_number;
        this.born = born;
        this.complement = complement;
        this.cpf = cpf;
        this.due_date = due_date;
        this.email = email;
        this.name = name;
        this.payment_method = payment_method;
        this.phone = phone;
        this.plan = plan;
        this.rg = rg;
        this.type = SIGNUP;
        this.zip_code = zip_code;
    }

    public CustomerNew(String zip_code) {
        this.zip_code = zip_code;
    }

    public CustomerNew(String name, String cpf, String phone, String plan) {
        this.address_number = "";
        this.born = "";
        this.complement = "";
        this.cpf = cpf != null ? cpf : "";
        this.due_date = "";
        this.email = "";
        this.name = name != null ? name : "";
        this.payment_method = "";
        this.phone = phone != null ? phone : "";
        this.plan = plan != null ? plan : "";
        this.rg = "";
        this.type = UPDATEPLAN;
        this.zip_code = "";
    }

    public void setAddress_number(String address_number) {
        this.address_number = address_number;
    }

    public String getAddress_number() {
        return this.address_number;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getBorn() {
        return this.born;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getDue_date() {
        return this.due_date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_method() {
        return this.payment_method;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getRg() {
        return this.rg;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getZip_code() {
        return this.zip_code;
    }
}