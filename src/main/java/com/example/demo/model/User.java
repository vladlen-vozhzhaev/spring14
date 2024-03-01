package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public String lastname;
    public String email;
    public String pass;
    public User() {}

    public User(String name, String lastname, String email, String pass) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.pass = pass;
    }
}
