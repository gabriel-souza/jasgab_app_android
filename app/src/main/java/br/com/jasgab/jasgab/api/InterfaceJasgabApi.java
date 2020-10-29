package br.com.jasgab.jasgab.api;

import br.com.jasgab.jasgab.model.Auth;

import br.com.jasgab.jasgab.model.Customer;
import br.com.jasgab.jasgab.model_http.RequestAuth;
import br.com.jasgab.jasgab.model_http.RequestCheckNeighborhood;
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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface InterfaceJasgabApi {

    //---------------- CALL API JASGAB-MK
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/login")
    Call<Auth> auth(@Body RequestAuth requestAuth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/customer/new")
    Call<ResponseCustomer> customerNew(@Body CustomerNew customerNew, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/customer/address")
    Call<ResponseAddress> getAddress(@Body CustomerNew customerNew, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/customer")
    Call<ResponseCustomer> customer(@Body RequestCustomer requestCustomer, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/customer_data")
    Call<ResponseCustomer> customer_data(@Body RequestCustomer requestCustomer, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/status")
    Call<ResponseStatus> status(@Body RequestStatus requestStatus, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/customer/unlock")
    Call<ResponseCustomerUnlock> unlock(@Body RequestCustomerUnlock requestCustomerUnlock, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/maintenance")
    Call<ResponseMaintenance> maintenance(@Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/check_neighborhood")
    Call<ResponseDefault> check_neighborhood(@Body RequestCheckNeighborhood customer, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/tools/mac_vendor")
    Call<ResponseMacVendor> mac_vendor(@Body RequestMacVendor requestMacVendor);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/network/isp")
    Call<ResponseIsp> isp(@Header("Authorization") String auth);

    @Multipart
    @POST("/customer/receipt")
    Call<ResponseDefault> receipt(@Part("cpf") RequestBody cpf, @Part("due_date") RequestBody due_date, @Part MultipartBody.Part receipt, @Header("Authorization") String auth);

    //---------------- CALL API FCM TO PUSH NOTIFICATION
    @GET("/insert_fcm")
    Call<Void> insert_fcm(@Query("id") Integer id, @Query("name") String name, @Query("neighborhood") Integer neighborhood, @Query("token") String token);

    /*@Headers({"Accept: text/xml",
            "User-Agent: something"})*//*
    @Headers("Cache-Control: max-age=640000")
    @HEAD("/headers")
    //HEAD must be in a method that returns Call<Void>
    Call<Void> testStaticHeaders();

    @HEAD("/headers")
    Call<Void> testDynamicHeaders(@Header("Content-Type") String contentRange);

    @GET("/headers")
    Call<Void> testDynamicMapHeaders(@HeaderMap Map<String, String> headers);

    @POST("/post")
    Call<ResponseBody> postField(@Field("key") String value, @Field(value = "default Value", encoded = true) String n);

    @FormUrlEncoded //FieldMap can only be used if you specify @FormUlrEncoded, otherwise crash!!
    @POST("/post")
    Call<ResponseBody> post(@FieldMap Map<String, String> map);

    @GET
    Call<ResponseBody> getImage(@Url String url);

    @Multipart
    @POST("/post")
    Call<ResponseBody> uploadFile(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    //@FormUrlEncoded Cannot be used in @Body, otherwise crash!!
    @POST("/post")
    Call<String> sendScalar(@Body String body);

    @POST("/post")
    Call<ResponseBody> post(@Body RequestBody requestBody);

    @POST("/post")
    Call<ResponseBody> post(@Body String stringBody);
    */
}