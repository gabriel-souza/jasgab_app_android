package br.com.jasgab.jasgab.api;

import java.net.InetAddress;

import br.com.jasgab.jasgab.model.Auth;

import br.com.jasgab.jasgab.model.RequestAuth;
import br.com.jasgab.jasgab.model.RequestCustomer;
import br.com.jasgab.jasgab.model.RequestStatus;
import br.com.jasgab.jasgab.model.RequestCustomerUnlock;
import br.com.jasgab.jasgab.model.ResponseCustomer;
import br.com.jasgab.jasgab.model.ResponseStatus;
import br.com.jasgab.jasgab.model.ResponseCustomerUnlock;
import br.com.jasgab.jasgab.model.ResponseMaintenance;
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

    //---------------- CALL API FCM TO PUSH NOTIFICATION

    public Call<Void> setFcmDados(Integer CodigoPessoa, String NomePessoa, Integer CodigoBairro, String fcm_token) {
        Conexao service = ServiceGeneratorJasgab.createService();
        return service.insert_fcm(CodigoPessoa, NomePessoa, CodigoBairro, fcm_token);
    }
}
