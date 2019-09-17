package com.example.projetcommerce.Model;

import java.io.Serializable;

public class Cart implements Serializable {

    private String keyCart,pid, pname, price, quantity, discount,orderID;


    public Cart() {
    }

    public Cart(String keyCart, String pid, String pname, String price, String quantity, String discount, String orderID) {
        this.keyCart = keyCart;
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.orderID = orderID;
    }

    public String getKeyCart() {
        return keyCart;
    }

    public void setKeyCart(String keyCart) {
        this.keyCart = keyCart;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
