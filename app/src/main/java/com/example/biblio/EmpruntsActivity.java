package com.example.biblio;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biblio.model.Emprunt;
import com.example.biblio.model.Livre;
import com.example.biblio.model.Membre;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EmpruntsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmpruntAdapter adapter;
    private TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprunts);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvCount = findViewById(R.id.tv_count);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showDialog());

        loadData();
    }

    private void loadData() {
        List<Emprunt> emprunts = Emprunt.listAll(Emprunt.class);
        tvCount.setText(emprunts.size() + " emprunt" + (emprunts.size() > 1 ? "s" : ""));
        adapter = new EmpruntAdapter(emprunts, this::onRetour, this::onDelete);
        recyclerView.setAdapter(adapter);
    }

    private void showDialog() {
        List<Livre> livresDispo = new ArrayList<>();
        for (Livre l : Livre.listAll(Livre.class))
            if (l.disponible) livresDispo.add(l);

        List<Membre> membres = Membre.listAll(Membre.class);

        if (livresDispo.isEmpty()) {
            Toast.makeText(this, "⚠️ Aucun livre disponible !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (membres.isEmpty()) {
            Toast.makeText(this, "⚠️ Aucun membre enregistré !", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_emprunt, null);

        Spinner spLivre  = dialogView.findViewById(R.id.sp_livre);
        Spinner spMembre = dialogView.findViewById(R.id.sp_membre);
        TextView tvDate1 = dialogView.findViewById(R.id.tv_date_emprunt);
        TextView tvDate2 = dialogView.findViewById(R.id.tv_date_retour);

        // Remplir spinner livres
        List<String> titres = new ArrayList<>();
        for (Livre l : livresDispo) titres.add(l.titre);
        spLivre.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, titres));

        // ✅ Remplir spinner membres avec getters
        List<String> noms = new ArrayList<>();
        for (Membre m : membres) noms.add(m.getPrenom() + " " + m.getNom());
        spMembre.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, noms));

        final String[] dates = {"", ""};

        dialogView.findViewById(R.id.btn_date_emprunt).setOnClickListener(v ->
                pickDate(date -> { dates[0] = date; tvDate1.setText(date); }));

        dialogView.findViewById(R.id.btn_date_retour).setOnClickListener(v ->
                pickDate(date -> { dates[1] = date; tvDate2.setText(date); }));

        new AlertDialog.Builder(this)
                .setTitle("➕ Nouvel emprunt")
                .setView(dialogView)
                .setPositiveButton("Enregistrer", (d, w) -> {
                    if (dates[0].isEmpty() || dates[1].isEmpty()) {
                        Toast.makeText(this, "⚠️ Choisissez les deux dates", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Livre livre   = livresDispo.get(spLivre.getSelectedItemPosition());
                    Membre membre = membres.get(spMembre.getSelectedItemPosition());

                    new Emprunt(livre, membre, dates[0], dates[1]).save();

                    livre.disponible = false;
                    livre.save();

                    loadData();
                    Toast.makeText(this, "✅ Emprunt enregistré !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void onRetour(Emprunt emprunt) {
        new AlertDialog.Builder(this)
                .setTitle("Retour du livre")
                .setMessage("Confirmer le retour de \"" + emprunt.livre.titre + "\" ?")
                .setPositiveButton("Oui", (d, w) -> {
                    emprunt.rendu = true;
                    emprunt.save();
                    emprunt.livre.disponible = true;
                    emprunt.livre.save();
                    loadData();
                    Toast.makeText(this, "✅ Livre retourné !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void onDelete(Emprunt emprunt) {
        new AlertDialog.Builder(this)
                .setTitle("🗑️ Supprimer")
                .setMessage("Supprimer cet emprunt ?")
                .setPositiveButton("Oui", (d, w) -> {
                    if (!emprunt.rendu && emprunt.livre != null) {
                        emprunt.livre.disponible = true;
                        emprunt.livre.save();
                    }
                    emprunt.delete();
                    loadData();
                    Toast.makeText(this, "🗑️ Supprimé !", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void pickDate(java.util.function.Consumer<String> callback) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (v, y, m, d) ->
                callback.accept(String.format("%02d/%02d/%04d", d, m + 1, y)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }
}