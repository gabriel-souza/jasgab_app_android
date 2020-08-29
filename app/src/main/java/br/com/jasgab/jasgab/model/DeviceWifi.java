package br.com.jasgab.jasgab.model;

public class DeviceWifi {
    private String ip;
    private String name;
    private String mac;
    private String brand;


    public DeviceWifi(String ip, String mac, String name, String brand) {
        this.ip = ip;
        this.mac = mac;
        this.name = name;
        this.brand = brand;
    }

    public DeviceWifi() {
        this.ip = String.valueOf("");
        this.mac = String.valueOf("");
        this.name = String.valueOf("");
        this.brand = String.valueOf("");

    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
