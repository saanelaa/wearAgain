package com.example.wearagain.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wearagain.R;
import com.example.wearagain.data.local.entity.KorpaEntity;

import java.util.ArrayList;
import java.util.List;

public class KorpaAdapter extends RecyclerView.Adapter<KorpaAdapter.KorpaViewHolder> {

    public interface OnUkloniListener {
        void onUkloni(KorpaEntity stavka);
    }

    private List<KorpaEntity> listaStavki = new ArrayList<>();
    private OnUkloniListener ukloniListener;

    public KorpaAdapter(OnUkloniListener listener) {
        this.ukloniListener = listener;
    }

    public void postaviListu(List<KorpaEntity> stavke) {
        listaStavki = new ArrayList<>(stavke);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KorpaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pogled = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stavka_korpa, parent, false);
        return new KorpaViewHolder(pogled);
    }

    @Override
    public void onBindViewHolder(@NonNull KorpaViewHolder holder, int position) {
        KorpaEntity stavka = listaStavki.get(position);
        holder.tvNaziv.setText(stavka.getNaziv());
        holder.tvCijena.setText(stavka.getCijena() + " KM");
        holder.tvVelicina.setText(stavka.getVelicina());

        Glide.with(holder.itemView.getContext())
                .load(stavka.getSlikaUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.ivSlika);

        holder.btnUkloni.setOnClickListener(v -> ukloniListener.onUkloni(stavka));
    }

    @Override
    public int getItemCount() {
        return listaStavki.size();
    }

    static class KorpaViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlika;
        TextView tvNaziv, tvCijena, tvVelicina;
        ImageButton btnUkloni;

        public KorpaViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSlika = itemView.findViewById(R.id.ivSlika);
            tvNaziv = itemView.findViewById(R.id.tvNaziv);
            tvCijena = itemView.findViewById(R.id.tvCijena);
            tvVelicina = itemView.findViewById(R.id.tvVelicina);
            btnUkloni = itemView.findViewById(R.id.btnUkloni);
        }
    }
}
