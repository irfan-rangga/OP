package com.katapanda.op.model;

public class User {
    int id;
    String username, email, nama, token;

    public User() {
    }

    public User(int id, String username, String email, String nama, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nama = nama;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
