package com.nurde.asynctaskjson;

public class Users {
    private String id;
    private String nama;
    private String username;
    private String email;
    private String address;

    public Users(String s, String id, String nama, String email, String address){
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.email = email;
        this.address = address;

    }
    public String getId(){
        return id;
    }
    public String getNama(){
        return nama;
    }
    public  String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public String getAddress(){
        return address;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setNama(String nama){
        this.nama = nama;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setAddress(String address){
        this.address = address;
    }
}
