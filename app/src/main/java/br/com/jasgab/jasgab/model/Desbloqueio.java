package br.com.jasgab.jasgab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Desbloqueio {

    @SerializedName("CodigoPessoa")
    @Expose
    private Integer codigoPessoa;
    @SerializedName("Conexao")
    @Expose
    private String conexao;
    @SerializedName("LimiteExcecao")
    @Expose
    private String limiteExcecao;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Mensagem")
    @Expose
    private String mensagem;

    public Integer getCodigoPessoa() {
        return codigoPessoa;
    }

    public void setCodigoPessoa(Integer codigoPessoa) {
        this.codigoPessoa = codigoPessoa;
    }

    public String getConexao() {
        return conexao;
    }

    public void setConexao(String conexao) {
        this.conexao = conexao;
    }

    public String getLimiteExcecao() {
        return limiteExcecao;
    }

    public void setLimiteExcecao(String limiteExcecao) {
        this.limiteExcecao = limiteExcecao;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}