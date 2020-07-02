package com.example.covid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.example.covid.Model.Countries;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;


import java.net.MalformedURLException;

import java.util.List;

interface AdapterDatosDelegate {
    void countrySelected(Countries country);
}

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos>{

    List<Countries> countries;
    private View.OnClickListener listener;
    AdapterDatosDelegate adapterDatosDelegate;
    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlist,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(countries.get(position));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }


    class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView dato;
        ImageView imagen;
        ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            dato = itemView.findViewById(R.id.idDato);
            imagen = itemView.findViewById(R.id.imageCountry);
        }

        void asignarDatos(Countries country) {
            dato.setText(country.getName());
            GlideToVectorYou.justLoadImage((Activity) itemView.getContext(), Uri.parse(country.getFlag()), imagen);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapterDatosDelegate != null) {
                        adapterDatosDelegate.countrySelected(country);
                    }
                }
            });
        }
    }

}