package com.example.biblio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biblio.model.Emprunt;
import java.util.List;

public class EmpruntAdapter extends RecyclerView.Adapter<EmpruntAdapter.ViewHolder> {

    public interface OnAction { void onAction(Emprunt emprunt); }

    private final List<Emprunt> emprunts;
    private final OnAction onRetour, onDelete;

    public EmpruntAdapter(List<Emprunt> emprunts, OnAction onRetour, OnAction onDelete) {
        this.emprunts = emprunts;
        this.onRetour = onRetour;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emprunt, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Emprunt e = emprunts.get(position);

        h.tvLivre.setText(e.livre != null ? e.livre.titre : "?");
        h.tvMembre.setText(e.membre != null ?
                e.membre.getPrenom() + " " + e.membre.getNom() : "?");
        h.tvDateEmprunt.setText("📅 Emprunté le : " + e.dateEmprunt);
        h.tvDateRetour.setText("🔙 Retour prévu : " + e.dateRetour);

        if (e.rendu) {
            h.tvStatut.setText("✅ Rendu");
            h.tvStatut.setTextColor(0xFF2E7D32);
            h.btnRetour.setVisibility(View.GONE);
        } else {
            h.tvStatut.setText("⏳ En cours");
            h.tvStatut.setTextColor(0xFFE65100);
            h.btnRetour.setVisibility(View.VISIBLE);
            h.btnRetour.setOnClickListener(v -> onRetour.onAction(e));
        }

        h.btnDelete.setOnClickListener(v -> onDelete.onAction(e));
    }

    @Override
    public int getItemCount() { return emprunts.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLivre, tvMembre, tvDateEmprunt, tvDateRetour, tvStatut;
        Button btnRetour;
        ImageButton btnDelete;

        ViewHolder(View v) {
            super(v);
            tvLivre       = v.findViewById(R.id.tv_livre);
            tvMembre      = v.findViewById(R.id.tv_membre);
            tvDateEmprunt = v.findViewById(R.id.tv_date_emprunt);
            tvDateRetour  = v.findViewById(R.id.tv_date_retour);
            tvStatut      = v.findViewById(R.id.tv_statut);
            btnRetour     = v.findViewById(R.id.btn_retour);
            btnDelete     = v.findViewById(R.id.btn_delete);
        }
    }
}