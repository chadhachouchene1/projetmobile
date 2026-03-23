package com.example.biblio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.biblio.model.Membre;

import java.util.List;

public class MembreAdapter extends RecyclerView.Adapter<MembreAdapter.ViewHolder> {

    // ── Interface pour les clics ──────────────────────────────
    public interface OnMembreClickListener {
        void onEdit(Membre membre);      // clic simple → modifier
        void onDelete(Membre membre);    // appui long  → supprimer
    }

    private List<Membre> membres;
    private OnMembreClickListener listener;

    // ── Constructeur ──────────────────────────────────────────
    public MembreAdapter(List<Membre> membres, OnMembreClickListener listener) {
        this.membres  = membres;
        this.listener = listener;
    }

    // ── ViewHolder : représente une ligne de la liste ─────────
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomPrenom;
        TextView tvTelephone;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNomPrenom = itemView.findViewById(R.id.tvNomPrenom);
            tvTelephone = itemView.findViewById(R.id.tvTelephone);
        }
    }

    // ── Créer la vue d'une ligne ──────────────────────────────
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_membre, parent, false);
        return new ViewHolder(v);
    }

    // ── Remplir les données d'une ligne ──────────────────────
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Membre m = membres.get(position);

        // Afficher prénom + nom
        holder.tvNomPrenom.setText(m.getPrenom() + " " + m.getNom());

        // Afficher téléphone
        holder.tvTelephone.setText("📞 " + m.getTelephone());

        // Clic simple → modifier
        holder.itemView.setOnClickListener(v -> listener.onEdit(m));

        // Appui long → supprimer
        holder.itemView.setOnLongClickListener(v -> {
            listener.onDelete(m);
            return true;
        });
    }

    // ── Nombre total de membres ───────────────────────────────
    @Override
    public int getItemCount() {
        return membres.size();
    }

    // ── Rafraîchir la liste ───────────────────────────────────
    public void refresh(List<Membre> newList) {
        this.membres = newList;
        notifyDataSetChanged();
    }
}