package com.example.franciscoandrade.button_challenge.restApi.model;

/**
 * Created by franciscoandrade on 3/1/18.
 */

public class RootObjectSendAmounts {
    private String candidate;
    private String amount;
    private int  user_id;

    public RootObjectSendAmounts(String candidate, String amount, int user_id) {
        this.candidate = candidate;
        this.amount = amount;
        this.user_id = user_id;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
