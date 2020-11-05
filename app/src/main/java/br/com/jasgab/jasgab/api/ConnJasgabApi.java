package br.com.jasgab.jasgab.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ConnJasgabApi {

    private static final String BASE_URL = "https://api.jasgab.com.br/";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    static <S> S createService() {
        return retrofit.create((Class<S>) InterfaceJasgabApi.class);
    }
}
