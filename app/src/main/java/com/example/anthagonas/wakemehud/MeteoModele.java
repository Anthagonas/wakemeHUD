package com.example.anthagonas.wakemehud;

/**
 * Created by Utilisateur92 on 07/04/2017.
 */

public class MeteoModele {

    private String nomVille;
    private String pays;
    private String etatMeteo;
    private int temp_min;
    private int temp_max;


    public String getNomVille() {
        return nomVille;
    }

    public void setNomVille(String nomVille) {
        this.nomVille = nomVille;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getWeatherStatus() {
        return etatMeteo;
    }

    public void setWeatherStatus(String etatMeteo) {
        this.etatMeteo = etatMeteo;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(int temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(int temp_max) {
        this.temp_max = temp_max;
    }
}
