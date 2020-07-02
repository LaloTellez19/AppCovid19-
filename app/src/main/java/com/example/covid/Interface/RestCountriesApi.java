package com.example.covid.Interface;

import com.example.covid.Model.Countries;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestCountriesApi {
    @GET("rest/v2/region/europe")
    Call<List<Countries>> getEurope();
    @GET("rest/v2/region/asia")
    Call<List<Countries>> getAsia();
    @GET("rest/v2/region/americas")
    Call<List<Countries>> getAmericas();
    @GET("rest/v2/region/africa")
    Call<List<Countries>> getAfrica();
    @GET("rest/v2/region/oceania")
    Call<List<Countries>> getOceania();
}
