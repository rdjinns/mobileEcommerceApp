package com.example.projetcommerce.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Orders implements Serializable {

    private String adresse, city, date, etat,name, phone, time, totalAmount, phoneInitial,pid;
    private List<Cart> products;

    public Orders() {

    }

    public Orders(String pid, String adresse, String city, String date, String etat, String name, String phone, String time, String totalAmount, String phoneInitial, List<Cart> products) {

        this.adresse = adresse;
        this.city = city;
        this.date = date;
        this.etat = etat;
        this.name = name;
        this.phone = phone;
        this.time = time;
        this.totalAmount = totalAmount;
        this.phoneInitial = phoneInitial;
        this.products = products;
        this.pid = pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPhoneInitial() {
        return phoneInitial;
    }

    public void setPhoneInitial(String phoneInitial) {
        this.phoneInitial = phoneInitial;
    }

    public List<Cart> getProducts() {
        return products;
    }

    public void setProducts(List<Cart> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        // return "Orders{" +
        //         "adresse='" + adresse + '\'' +
        //         ", city='" + city + '\'' +
        //         ", date='" + date + '\'' +
        //         ", etat='" + etat + '\'' +
        //         ", name='" + name + '\'' +
        //         ", phone='" + phone + '\'' +
        //         ", time='" + time + '\'' +
        //         ", totalAmount='" + totalAmount + '\'' +
        //         ", phoneInitial='" + phoneInitial + '\'' +
        //         '}';
        return etat + " | " + totalAmount.toString();
    }


    public Map<String,Object> toMap()
    {
        Map<String, Object> ordersMap = new HashMap<>();

        ordersMap.put("products", this.products);
        ordersMap.put("totalAmount", this.totalAmount);
        ordersMap.put("name", this.name);
        ordersMap.put("phone", this.phone);
        ordersMap.put("adresse", this.adresse);
        ordersMap.put("city", this.city);
        ordersMap.put("date", this.date);
        ordersMap.put("time", this.time);
        ordersMap.put("etat", this.etat);
        ordersMap.put("phoneInitial", this.phoneInitial);
        ordersMap.put("pid", this.pid);
        return ordersMap;
    }


}