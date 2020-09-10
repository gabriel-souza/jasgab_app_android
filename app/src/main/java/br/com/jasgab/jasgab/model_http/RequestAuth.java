package br.com.jasgab.jasgab.model_http;

public class RequestAuth {

    final public String username;
    final public String password;

    public RequestAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
