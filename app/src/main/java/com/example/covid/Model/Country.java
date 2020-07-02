package com.example.covid.Model;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("Country")
    String country;
    @SerializedName("CountryCode")
    String countryCode;
    @SerializedName("NewConfirmed")
    long newConfirmed;
    @SerializedName("TotalConfirmed")
    long totalConfirmed;
    @SerializedName("NewDeaths")
    long newDeaths;
    @SerializedName("TotalDeaths")
    long totalDeaths;
    @SerializedName("NewRecovered")
    long newRecovered;
    @SerializedName("TotalRecovered")
    long totalRecovered;
    @SerializedName("Date")
    String date;

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public long getNewConfirmed() {
        return newConfirmed;
    }

    public long getTotalConfirmed() {
        return totalConfirmed;
    }

    public long getNewDeaths() {
        return newDeaths;
    }

    public long getTotalDeaths() {
        return totalDeaths;
    }

    public long getNewRecovered() {
        return newRecovered;
    }

    public long getTotalRecovered() {
        return totalRecovered;
    }

    public String getDate() {
        return date;
    }
}
