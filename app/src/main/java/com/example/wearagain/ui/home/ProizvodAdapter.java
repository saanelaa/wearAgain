package com.example.wearagain.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wearagain.R;
import com.example.wearagain.data.local.entity.ProizvodEntity;

import java.util.ArrayList;
import java.util.List;

public class ProizvodAdapter extends RecyclerView.Adapter<ProizvodAdapter.ProizvodViewHolder> {

    private List<ProizvodEntity> listaProizvoda = new ArrayList<>();
    private List<ProizvodEntity> punaLista = new ArrayList<>();

    public void postaviListu(List<ProizvodEntity> proizvodi) {
        listaProizvoda = new ArrayList<>(proizvodi);
        punaLista = new ArrayList<>(proizvodi);
        notifyDataSetChanged();
    }

    public void filtriraj(String tekst) {
        listaProizvoda.clear();
        if (tekst.isEmpty()) {
            listaProizvoda.addAll(punaLista);
        } else {
            String upit = tekst.toLowerCase().trim();
            for (ProizvodEntity p : punaLista) {
                if (p.getNaziv().toLowerCase().contains(upit) ||
                        p.getKategorija().toLowerCase().contains(upit)) {
                    listaProizvoda.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProizvodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pogled = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stavka_proizvod, parent, false);
        return new ProizvodViewHolder(pogled);
    }

    @Override
    public void onBindViewHolder(@NonNull ProizvodViewHolder holder, int position) {
        ProizvodEntity proizvod = listaProizvoda.get(position);
        holder.tvNaziv.setText(proizvod.getNaziv());
        holder.tvCijena.setText(proizvod.getCijena() + " KM");
        holder.tvVelicina.setText(proizvod.getVelicina());

        Glide.with(holder.itemView.getContext())
                .load(proizvod.getSlikaUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.ivSlika);
    }

    @Override
    public int getItemCount() {
        return listaProizvoda.size();
    }

    static class ProizvodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlika;
        TextView tvNaziv, tvCijena, tvVelicina;

        public ProizvodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSlika = itemView.findViewById(R.id.ivSlika);
            tvNaziv = itemView.findViewById(R.id.tvNaziv);
            tvCijena = itemView.findViewById(R.id.tvCijena);
            tvVelicina = itemView.findViewById(R.id.tvVelicina);
        }
    }
}