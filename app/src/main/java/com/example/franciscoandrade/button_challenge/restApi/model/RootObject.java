package com.example.franciscoandrade.button_challenge.restApi.model;

/**
 * Created by franciscoandrade on 3/1/18.
 */

public class RootObject {

    private String name;
    private String email;
    private String candidate;

    public RootObject( String name, String email, String candidate) {
        this.name = name;
        this.email = email;
        this.candidate = candidate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCandidate() {
        return candidate;
    }
}