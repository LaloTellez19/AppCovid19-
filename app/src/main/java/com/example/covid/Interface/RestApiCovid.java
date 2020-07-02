package com.example.covid.Interface;

import com.example.covid.Model.Case;
import com.example.covid.Model.Countries;
import com.example.covid.Model.Summary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApiCovid {
    @GET("summary")
    Call<Summary> getCases();
}
