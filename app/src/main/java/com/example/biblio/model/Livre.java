package com.example.biblio.model;

import com.orm.SugarRecord;

public class Livre extends SugarRecord {
    public String titre;
    public String auteur;
    public int annee;
    public boolean disponible = true;

    public Livre() {}

    public Livre(String titre, String auteur, int annee) {
        this.titre = titre;
        this.auteur = auteur;
        this.annee = annee;
    }

    @Override
    public String toString() {
        return titre + " — " + auteur + " (" + annee + ") " + (disponible ? "✅" : "🔴");
    }
}