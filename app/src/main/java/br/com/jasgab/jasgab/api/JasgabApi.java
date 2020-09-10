package br.com.jasgab.jasgab.api;

import br.com.jasgab.jasgab.model.Auth;

import br.com.jasgab.jasgab.model_http.RequestAuth;
import br.com.jasgab.jasgab.model_http.RequestCustomer;
import br.com.jasgab.jasgab.model_http.RequestMacVendor;
import br.com.jasgab.jasgab.model_http.RequestStatus;
import br.com.jasgab.jasgab.model_http.RequestCustomerUnlock;
import br.com.jasgab.jasgab.model_http.ResponseDefault;
import br.com.jasgab.jasgab.model_http.ResponseAddress;
import br.com.jasgab.jasgab.model_http.ResponseCustomer;
import br.com.jasgab.jasgab.model_http.ResponseIsp;
import br.com.jasgab.jasgab.model_http.ResponseMacVendor;
import br.com.jasgab.jasgab.model_http.ResponseStatus;
import br.com.jasgab.jasgab.model_http.ResponseCustomerUnlock;
import br.com.jasgab.jasgab.model_http.ResponseMaintenance;
import br.com.jasgab.jasgab.model.CustomerNew;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class JasgabApi {

    public Call<Auth> auth() {
        RequestAuth requestAuth = new RequestAuth("gabriel","g@br131");
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.auth(requestAuth);
    }

    public Call<ResponseCustomer> customerNew(CustomerNew customerNew, String auth) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.customerNew(customerNew, "Bearer "+auth);
    }

    public Call<ResponseAddress> getAddress(CustomerNew customerNew, String auth) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.getAddress(customerNew, "Bearer "+auth);
    }

    public Call<ResponseCustomer> customer(RequestCustomer requestCustomer, String auth) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.customer(requestCustomer, "Bearer "+auth);
    }

    public Call<ResponseCustomer> customer_data(RequestCustomer requestCustomer, String auth) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.customer_data(requestCustomer, "Bearer "+auth);
    }

    public Call<ResponseStatus> status(RequestStatus requestStatus, String auth) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.status(requestStatus, "Bearer "+auth);
    }

    public Call<ResponseCustomerUnlock> unlock(RequestCustomerUnlock requestCustomerUnlock, String auth) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.unlock(requestCustomerUnlock, "Bearer "+auth);
    }

    public Call<ResponseMaintenance> maintenance(String auth) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.maintenance("Bearer "+auth);
    }

    public Call<ResponseMaintenance> check_neighborhood(String auth) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.check_neighborhood("Bearer "+auth);
    }

    public Call<ResponseMacVendor> mac_vendor(RequestMacVendor requestMacVendor) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.mac_vendor(requestMacVendor);
    }

    public Call<ResponseIsp> isp(String auth) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.isp("Bearer "+auth);
    }

    public Call<ResponseDefault> receipt(RequestBody cpf, RequestBody due_date, MultipartBody.Part receipt, String auth) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.receipt(cpf, due_date, receipt, "Bearer "+auth);
    }

    //---------------- CALL API FCM TO PUSH NOTIFICATION

    public Call<Void> setFcmDados(Integer CodigoPessoa, String NomePessoa, Integer CodigoBairro, String fcm_token) {
        InterfaceJasgabApi service = ConnJasgabApi.createService();
        return service.insert_fcm(CodigoPessoa, NomePessoa, CodigoBairro, fcm_token);
    }
}
