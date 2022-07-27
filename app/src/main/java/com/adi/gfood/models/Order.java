package com.adi.gfood.models;

import java.io.Serializable;
import java.util.HashMap;

public class Order implements Serializable {
    private Address address;

    private String orderTime, deliveryTime, orderId;

    private Integer totalPayableAmount, paymentMode;

    public static final Integer UPI = 0;
    public static final Integer COB = 1;

    public static final String ORDERED = "Ordered";
    public static final String ORDER_READY = "Order ready";
    public static final String PICKED_UP = "On the way";
    public static final String DELIVERED = "Delivered";

    private String orderStatus;

    private HashMap<String, CartItem> contents = new HashMap<>();

    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void addContent(CartItem o, String uid){
        this.contents.put(uid, o);
    }

    public HashMap<String, CartItem> getContents() {
        return contents;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public void setTotalPayableAmount(Integer totalPayableAmount) {
        this.totalPayableAmount = totalPayableAmount;
    }

    public Integer getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Integer paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
