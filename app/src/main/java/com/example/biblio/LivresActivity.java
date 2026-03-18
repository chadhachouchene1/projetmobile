package com.example.biblio;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biblio.model.Livre;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class LivresActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LivreAdapter adapter;
    private TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livre);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvCount = findViewById(R.id.tv_count);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showDialog(null));

        loadData();
    }

    private void loadData() {
        List<Livre> livres = Livre.listAll(Livre.class);
        tvCount.setText(livres.size() + " livre" + (livres.size() > 1 ? "s" : ""));
        adapter = new LivreAdapter(livres, this::onEdit, this::onDelete);
        recyclerView.setAdapter(adapter);
    }

    private void showDialog(Livre livreEdite) {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_livre, null);

        EditText etTitre  = dialogView.findViewById(R.id.et_titre);
        EditText etAuteur = dialogView.findViewById(R.id.et_auteur);
        EditText etAnnee  = dialogView.findViewById(R.id.et_annee);

        if (livreEdite != null) {
            etTitre.setText(livreEdite.titre);
            etAuteur.setText(livreEdite.auteur);
            etAnnee.setText(String.valueOf(livreEdite.annee));
        }

        new AlertDialog.Builder(this)
                .setTitle(livreEdite == null ? "➕ Ajouter un livre" : "✏️ Modifier le livre")
                .setView(dialogView)
                .setPositiveButton("Enregistrer", (d, w) -> {
                    String titre    = etTitre.getText().toString().trim();
                    String auteur   = etAuteur.getText().toString().trim();
                    String anneeStr = etAnnee.getText().toString().trim();

                    if (titre.isEmpty() || auteur.isEmpty() || anneeStr.isEmpty()) {
                        Toast.makeText(this, "⚠️ Tous les champs sont requis", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (livreEdite == null) {
                        new Livre(titre, auteur, Integer.parseInt(anneeStr)).save();
                        Toast.makeText(this, "✅ Livre ajouté !", Toast.LENGTH_SHORT).show();
                    } else {
                        livreEdite.titre  = titre;
                        livreEdite.auteur = auteur;
                        livreEdite.annee  = Integer.parseInt(anneeStr);
                        livreEdite.save();
                        Toast.makeText(this, "✅ Livre modifié !", Toast.LENGTH_SHORT).show();
                    }
                    loadData();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void onEdit(Livre livre) { showDialog(livre); }

    private void onDelete(Livre livre) {
        new AlertDialog.Builder(this)
                .setTitle("🗑️ Supprimer")
                .setMessage("Supprimer \"" + livre.titre + "\" ?")
                .setPositiveButton("Oui", (d, w) -> {
                    livre.delete();
                    loadData();
                    Toast.makeText(this, "🗑️ Supprimé !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", null)
                .show();
    }
}