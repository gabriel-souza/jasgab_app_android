package br.com.jasgab.jasgab.model_http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model.CustomerData;

public class ResponseCustomer {

    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("customerData")
    @Expose
    private CustomerData customerData;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerData getCustomerData() {
        return customerData;
    }

    public void setCustomerData(CustomerData customerData) {
        this.customerData = customerData;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
