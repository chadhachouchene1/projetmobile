package com.example.biblio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biblio.model.Livre;
import java.util.List;

public class LivreAdapter extends RecyclerView.Adapter<LivreAdapter.ViewHolder> {

    public interface OnAction { void onAction(Livre livre); }

    private final List<Livre> livres;
    private final OnAction onEdit, onDelete;

    public LivreAdapter(List<Livre> livres, OnAction onEdit, OnAction onDelete) {
        this.livres   = livres;
        this.onEdit   = onEdit;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livre, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Livre livre = livres.get(position);

        h.tvTitre.setText(livre.titre);
        h.tvAuteur.setText("✍️ " + livre.auteur + "  •  " + livre.annee);

        if (livre.disponible) {
            h.tvDispo.setText("✅ Disponible");
            h.tvDispo.setTextColor(0xFF2E7D32);
            h.tvDispo.setBackgroundResource(R.drawable.bg_badge);
        } else {
            h.tvDispo.setText("🔴 Emprunté");
            h.tvDispo.setTextColor(0xFFC62828);
            h.tvDispo.setBackgroundResource(R.drawable.bg_badge_red);
        }

        h.btnEdit.setOnClickListener(v -> onEdit.onAction(livre));
        h.btnDelete.setOnClickListener(v -> onDelete.onAction(livre));
    }

    @Override
    public int getItemCount() { return livres.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvAuteur, tvDispo;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View v) {
            super(v);
            tvTitre   = v.findViewById(R.id.tv_titre);
            tvAuteur  = v.findViewById(R.id.tv_auteur);
            tvDispo   = v.findViewById(R.id.tv_dispo);
            btnEdit   = v.findViewById(R.id.btn_edit);
            btnDelete = v.findViewById(R.id.btn_delete);
        }
    }
}