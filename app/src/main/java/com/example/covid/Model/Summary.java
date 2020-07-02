package com.example.covid.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Summary {
    public String getDate() {
        return date;
    }

    public Global getGlobal() {
        return global;
    }

    public List<Country> getCountries() {
        return countries;
    }

    @SerializedName("Date")
    String date;
    @SerializedName("Global")
    Global global;
    @SerializedName("Countries")
    public List<Country> countries;
}

class Global {

}

