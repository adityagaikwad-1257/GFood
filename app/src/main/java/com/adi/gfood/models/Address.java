package com.adi.gfood.models;

import java.io.Serializable;

public class Address implements Serializable {
    private String name, addressLine1, addressLine2, landMark, pinCode, state, address;

    private String addressUid;

    Address(){}

    public Address(String name, String addressLine1, String addressLine2, String landMark, String pinCode, String state) {
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.landMark = landMark;
        this.pinCode = pinCode;
        this.state = state;

        this.address = addressLine1 + "\n";
        this.address += addressLine2 + "\n";
        if (!landMark.trim().isEmpty())
            this.address += landMark + "\n";

        this.address += state + "-";
        this.address += pinCode + ".";
    }

    public String getAddressUid() {
        return addressUid;
    }

    public void setAddressUid(String addressUid) {
        this.addressUid = addressUid;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
