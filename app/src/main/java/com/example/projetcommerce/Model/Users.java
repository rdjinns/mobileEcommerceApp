package com.example.projetcommerce.Model;

public class Users { // class user qui va être utilisé pour les connexions

    private String name, password,phone,address;

    public Users()
    {

    }

    public Users(String name, String password, String phone, String image, String address) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
