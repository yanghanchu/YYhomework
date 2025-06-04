package com.example.myapplicationx429as;

public class RateItem {
    private int id;
    private String currencyName;
    private float rate;
    private String date; // yyyy-MM-dd

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCurrencyName() { return currencyName; }
    public void setCurrencyName(String currencyName) { this.currencyName = currencyName; }

    public float getRate() { return rate; }
    public void setRate(float rate) { this.rate = rate; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
