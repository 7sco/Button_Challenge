package com.example.franciscoandrade.button_challenge.restApi.model;

import java.io.Serializable;

/**
 * Created by franciscoandrade on 3/1/18.
 */

public class RootObjectUser implements Serializable{
    private int id;
    private String name;
    private String email;
    private String candidate;

    public int getId() {
        return id;
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

