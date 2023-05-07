package com.masai;

public class ConsumerEntity extends UserEntity{
    private String address;
    private Integer mobileNumber;

    public ConsumerEntity(String firstName, String lastName, String email, String password, String address, Integer mobileNumber) {
        super(firstName, lastName, email, password);
        this.address = address;
        this.mobileNumber = mobileNumber;
    }

    public ConsumerEntity(String address, Integer mobileNumber) {
        this.address = address;
        this.mobileNumber = mobileNumber;
    }

    public ConsumerEntity() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Integer mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
