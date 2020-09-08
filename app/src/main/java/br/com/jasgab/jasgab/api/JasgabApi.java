package br.com.jasgab.jasgab.api;

import java.net.InetAddress;

import br.com.jasgab.jasgab.model.Auth;

import br.com.jasgab.jasgab.model.RequestAuth;
import br.com.jasgab.jasgab.model.RequestCustomer;
import br.com.jasgab.jasgab.model.RequestMacVendor;
import br.com.jasgab.jasgab.model.RequestStatus;
import br.com.jasgab.jasgab.model.RequestCustomerUnlock;
import br.com.jasgab.jasgab.model.ResponseDefault;
import br.com.jasgab.jasgab.model.ResponseAddress;
import br.com.jasgab.jasgab.model.ResponseCustomer;
import br.com.jasgab.jasgab.model.ResponseIsp;
import br.com.jasgab.jasgab.model.ResponseMacVendor;
import br.com.jasgab.jasgab.model.ResponseStatus;
import br.com.jasgab.jasgab.model.ResponseCustomerUnlock;
import br.com.jasgab.jasgab.model.ResponseMaintenance;
import br.com.jasgab.jasgab.model.CustomerNew;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class JasgabApi {

    public Boolean internetOk() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.toString().equals("");
        } catch (Exception e) {
            return false;
        }
    }

    public Call<Auth> auth() {
        RequestAuth requestAuth = new RequestAuth("gabriel","g@br131");
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.auth(requestAuth);
    }

    public Call<ResponseCustomer> customerNew(CustomerNew customerNew, String auth) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.customerNew(customerNew, "Bearer "+auth);
    }

    public Call<ResponseAddress> getAddress(CustomerNew customerNew, String auth) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.getAddress(customerNew, "Bearer "+auth);
    }

    public Call<ResponseCustomer> customer(RequestCustomer requestCustomer, String auth) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.customer(requestCustomer, "Bearer "+auth);
    }

    public Call<ResponseCustomer> customer_data(RequestCustomer requestCustomer, String auth) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.customer_data(requestCustomer, "Bearer "+auth);
    }

    public Call<ResponseStatus> status(RequestStatus requestStatus, String auth) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.status(requestStatus, "Bearer "+auth);
    }

    public Call<ResponseCustomerUnlock> unlock(RequestCustomerUnlock requestCustomerUnlock, String auth) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.unlock(requestCustomerUnlock, "Bearer "+auth);
    }

    public Call<ResponseMaintenance> maintenance(String auth) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.maintenance("Bearer "+auth);
    }

    public Call<ResponseMaintenance> check_neighborhood(String auth) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.check_neighborhood("Bearer "+auth);
    }

    public Call<ResponseMacVendor> mac_vendor(RequestMacVendor requestMacVendor) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.mac_vendor(requestMacVendor);
    }

    public Call<ResponseIsp> isp(String auth) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.isp("Bearer "+auth);
    }

    public Call<ResponseDefault> receipt(RequestBody cpf, RequestBody due_date, MultipartBody.Part receipt, String auth) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.receipt(cpf, due_date, receipt, "Bearer "+auth);
    }

    //---------------- CALL API FCM TO PUSH NOTIFICATION

    public Call<Void> setFcmDados(Integer CodigoPessoa, String NomePessoa, Integer CodigoBairro, String fcm_token) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.insert_fcm(CodigoPessoa, NomePessoa, CodigoBairro, fcm_token);
    }
}
