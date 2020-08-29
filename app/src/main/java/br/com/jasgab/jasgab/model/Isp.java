
package br.com.jasgab.jasgab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Isp {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("isp")
    @Expose
    private String isp;
    @SerializedName("nat")
    @Expose
    private String nat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getNat() {
        return nat;
    }

    public void setNat(String nat) {
        this.nat = nat;
    }

}
