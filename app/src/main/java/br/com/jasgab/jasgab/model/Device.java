package br.com.jasgab.jasgab.model;

public class Device {

    public String address;
    public String hostName;
    public String macAddress;
    public String type;


    public Device(String address, String hostName, String macAddress, String type){
        this.address = address;
        this.hostName = hostName;
        this.macAddress = macAddress;
        this.type = type;
    }
    
    public void getType(String type){
        this.type = type;
    }
    public void getHostName(String hostName){
        this.hostName = hostName;
    }
    public void getMacAddress(String macAddress){
        this.macAddress = macAddress;
    }
    public void getAddress(String address){
        this.address = address;
    }

    public void setType(String type){
        this.type = type;
    }
    public void setHostName(String hostName){
        this.hostName = hostName;
    }
    public void setMacAddress(String macAddress){
        this.macAddress = macAddress;
    }
    public void setAddress(String address){
        this.address = address;
    }
    
    
    
    
}
