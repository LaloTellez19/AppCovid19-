package com.example.covid;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid.Interface.RestApiCovid;
import com.example.covid.Model.Countries;
import com.example.covid.Model.Country;
import com.example.covid.Model.Summary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentPaises extends Fragment implements AdapterDatosDelegate {
    static final String TAG = "FragmentPaises";
    SearchView buscar;
    RecyclerView lista;
    ProgressBar progress;
    List<Countries> countries;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private AdapterDatos adapterDatos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paisesfragment,container,false);
        lista =(RecyclerView)view.findViewById(R.id.idRecyclerFragmet);
        lista.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDatos = new AdapterDatos();
        adapterDatos.countries = countries;
        adapterDatos.adapterDatosDelegate = this;
        lista.setAdapter(adapterDatos);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.my_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.search_icon);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onQueryTextChange(final String newText) {
                    Predicate<Countries> likeCountry = countries -> countries.getName().contains(newText);
                    adapterDatos.countries = countries.stream().filter(likeCountry).collect(Collectors.toList());
                    adapterDatos.notifyDataSetChanged();
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_icon:
                // Not implemented here
                return false;

            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void countrySelected(Countries country) {
        getCases(country);
    }

    private void getCases(Countries contry)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestApiCovid restApiCovid = retrofit.create(RestApiCovid.class);
        Call<Summary> call = restApiCovid.getCases();
        call.enqueue(new Callback<Summary>() {
            @Override
            public void onResponse(Call<Summary> call, Response<Summary> response) {
                if(!response.isSuccessful())
                {
                    Log.d("TAG", "Codigo"+response.code());
                }
                for(Country countries: response.body().getCountries())
                {
                    if(countries.getCountryCode().equals(contry.getAlpha2Code()))
                    {
                        showAlert(countries);
                        return;
                    }
                }

            }

            @Override
            public void onFailure(Call<Summary> call, Throwable t) {

            }
        });

    }

    public void showAlert(Country country)
    {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.casos_paises,null);
        TextView totalNuevosCasosConfirmados = alertLayout.findViewById(R.id.txtNewConfirmed);
        TextView totalCasosConfirmaddos = alertLayout.findViewById(R.id.txtTotalConfirmed);
        TextView totalMuertes = alertLayout.findViewById(R.id.txtTotalDeaths);
        TextView totalRecuperados = alertLayout.findViewById(R.id.txtTotalRecovered);
        totalNuevosCasosConfirmados.setText(String.valueOf(country.getNewConfirmed()));
        totalCasosConfirmaddos.setText(String.valueOf(country.getTotalConfirmed()));
        totalMuertes.setText(String.valueOf(country.getTotalDeaths()));
        totalRecuperados.setText(String.valueOf(country.getTotalRecovered()));
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        alert.setTitle("SARS COV 2 en " + country.getCountry());
        alert.setView(alertLayout);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

}
