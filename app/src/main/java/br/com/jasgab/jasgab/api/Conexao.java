package br.com.jasgab.jasgab.api;

import br.com.jasgab.jasgab.model.Auth;

import br.com.jasgab.jasgab.model.RequestAuth;
import br.com.jasgab.jasgab.model.RequestCustomer;
import br.com.jasgab.jasgab.model.ResponseCustomer;
import br.com.jasgab.jasgab.model.ResponseMaintenance;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Conexao {

    //---------------- CALL API JASGAB-MK
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/login")
    Call<Auth> auth(@Body RequestAuth requestAuth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/customer")
    Call<ResponseCustomer> customer(@Body RequestCustomer requestCustomer, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("/customer_data")
    Call<ResponseCustomer> customer_data(@Body RequestCustomer requestCustomer, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/maintenance")
    Call<ResponseMaintenance> maintenance(@Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/check_neighborhood")
    Call<ResponseMaintenance> check_neighborhood(@Header("Authorization") String auth);

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