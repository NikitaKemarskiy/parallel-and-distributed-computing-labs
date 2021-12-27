package com.nikita.entities;

public class Client {
    private String id;
    private String phoneNumber;

    public Client(String id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
