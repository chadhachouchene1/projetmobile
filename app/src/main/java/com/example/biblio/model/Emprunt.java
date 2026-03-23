package com.example.biblio.model;

import com.orm.SugarRecord;

public class Emprunt extends SugarRecord {
    public Livre livre;
    public Membre membre;
    public String dateEmprunt;
    public String dateRetour;
    public boolean rendu = false;

    public Emprunt() {}

    public Emprunt(Livre livre, Membre membre, String dateEmprunt, String dateRetour) {
        this.livre = livre;
        this.membre = membre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
    }
}