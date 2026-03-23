package com.example.biblio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biblio.model.Membre;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MembresActivity extends AppCompatActivity
        implements MembreAdapter.OnMembreClickListener {

    private RecyclerView recyclerView;
    private MembreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membres);

        // ── 1. Connecter le RecyclerView ──────────────────────
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ── 2. Connecter le bouton + (FAB) ────────────────────
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showDialog(null)); // null = mode Ajout

        // ── 3. Charger la liste au démarrage ──────────────────
        loadData();
    }

    // ─────────────────────────────────────────────────────────
    // READ — Charger tous les membres depuis SugarORM
    // ─────────────────────────────────────────────────────────
    private void loadData() {
        List<Membre> membres = Membre.listAll(Membre.class);
        adapter = new MembreAdapter(membres, this);
        recyclerView.setAdapter(adapter);
    }

    // ─────────────────────────────────────────────────────────
    // CREATE / UPDATE — Formulaire dans un AlertDialog
    // ─────────────────────────────────────────────────────────
    private void showDialog(Membre membreEdite) {

        // Charger le formulaire dialog_membre.xml
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_membre, null);

        EditText etPrenom    = dialogView.findViewById(R.id.etPrenom);
        EditText etNom       = dialogView.findViewById(R.id.etNom);
        EditText etTelephone = dialogView.findViewById(R.id.etTelephone);

        // Si mode Modifier → pré-remplir les champs
        if (membreEdite != null) {
            etPrenom.setText(membreEdite.getPrenom());
            etNom.setText(membreEdite.getNom());
            etTelephone.setText(membreEdite.getTelephone());
        }

        // Construire le dialog
        new AlertDialog.Builder(this)
                .setTitle(membreEdite == null ? "➕ Ajouter un membre"
                        : "✏️ Modifier le membre")
                .setView(dialogView)
                .setPositiveButton("Enregistrer", (d, w) -> {

                    String prenom    = etPrenom.getText().toString().trim();
                    String nom       = etNom.getText().toString().trim();
                    String telephone = etTelephone.getText().toString().trim();

                    // Vérification des champs obligatoires
                    if (nom.isEmpty() || prenom.isEmpty()) {
                        Toast.makeText(this,
                                "⚠️ Nom et prénom obligatoires !",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (membreEdite == null) {
                        // ── CREATE ──────────────────────────────
                        new Membre(nom, prenom, telephone).save();
                        Toast.makeText(this,
                                "✅ Membre ajouté !",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // ── UPDATE ──────────────────────────────
                        membreEdite.setNom(nom);
                        membreEdite.setPrenom(prenom);
                        membreEdite.setTelephone(telephone);
                        membreEdite.save();
                        Toast.makeText(this,
                                "✅ Membre modifié !",
                                Toast.LENGTH_SHORT).show();
                    }

                    loadData(); // Rafraîchir la liste
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    // ─────────────────────────────────────────────────────────
    // UPDATE — Clic simple sur un membre → ouvrir le formulaire
    // ─────────────────────────────────────────────────────────
    @Override
    public void onEdit(Membre membre) {
        showDialog(membre); // passer le membre → mode Modifier
    }

    // ─────────────────────────────────────────────────────────
    // DELETE — Appui long → confirmation puis suppression
    // ─────────────────────────────────────────────────────────
    @Override
    public void onDelete(Membre membre) {
        new AlertDialog.Builder(this)
                .setTitle("🗑️ Supprimer")
                .setMessage("Supprimer " + membre.getPrenom()
                        + " " + membre.getNom() + " ?")
                .setPositiveButton("Oui", (d, w) -> {
                    membre.delete(); // ── DELETE ──
                    loadData();      // Rafraîchir la liste
                    Toast.makeText(this,
                            "🗑️ Membre supprimé !",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", null)
                .show();
    }
}