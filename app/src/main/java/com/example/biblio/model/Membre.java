package com.example.biblio.model;

import com.orm.SugarRecord;

public class Membre extends SugarRecord {

    //avec extends SugarRecord
    //SugarORM crée automatiquement
    // une table MEMBRE dans SQLite
    // avec les colonnes nom, prenom, telephone
    // + une colonne ID automatiqu

    private String nom;
    private String prenom;
    private String telephone;

    // ⚠️ Constructeur vide OBLIGATOIRE pour SugarORM
    public Membre() {}

    // Constructeur avec paramètres
    public Membre(String nom, String prenom, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    // ── Getters ──────────────────────────────
    public String getNom()       { return nom; }
    public String getPrenom()    { return prenom; }
    public String getTelephone() { return telephone; }

    // ── Setters ──────────────────────────────
    public void setNom(String nom)           { this.nom = nom; }
    public void setPrenom(String prenom)     { this.prenom = prenom; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}