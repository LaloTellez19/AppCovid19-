package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.covid.Interface.RestApiCovid;
import com.example.covid.Model.Case;
import com.example.covid.Model.Countries;
import com.example.covid.Model.Country;
import com.example.covid.Model.Summary;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {
    private TextView europe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        europe = (TextView) findViewById(R.id.json);
        getCase();
    }
    private void getCase()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RestApiCovid restApiCovid = retrofit.create(RestApiCovid.class);
        Call<Summary> call = restApiCovid.getCases();
        call.enqueue(new Callback<Summary>() {
            @Override
            public void onResponse(Call<Summary> call, Response<Summary> response) {
                if(!response.isSuccessful())
                {
                    europe.setText("Codigo: "+response.code());
                    return;
                }
                List<Country> countriesCases = response.body().countries;
                for(Country country: countriesCases)
                {
                    String content = "";
                    content += country.getCountry()+"\n";
                    content += country.getNewConfirmed()+"\n";
                    content += country.getTotalConfirmed()+"\n";
                    content += country.getNewDeaths()+"\n";
                    content += country.getTotalDeaths()+"\n";
                    content += country.getNewRecovered()+"\n";
                    content += country.getTotalRecovered()+"\n\n";
                    europe.append(content);
                }
            }

            @Override
            public void onFailure(Call<Summary> call, Throwable t) {
                europe.setText(t.getMessage());
            }
        });

    }
}