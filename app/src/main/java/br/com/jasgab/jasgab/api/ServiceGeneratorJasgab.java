package br.com.jasgab.jasgab.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ServiceGeneratorJasgab {

    private static String BASE_URL = "https://api.jasgab.com.br/";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    static <S> S createService() {
        return retrofit.create((Class<S>) Conexao.class);
    }
}
