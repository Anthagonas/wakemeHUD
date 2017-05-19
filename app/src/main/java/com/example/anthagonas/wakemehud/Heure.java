package com.example.anthagonas.wakemehud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

/**
 * Created by anthagonas on 28/03/17.
 */

public class Heure extends Fragment {
                                                                        //DESCRIPTION
    /*Fragment instancie dans l'activite "WakeMeHUD". Permet l'affichage du widget heure et gere le format du dit widget par l'activite "Configuration".
     Affiche également l'etat courant de la batterie du telephone*/

                                                                    //DECLARATION DES VARIABLES
    View v;
    String formatHeure;

                                                                    //A L'AFFICHAGE DU FRAGMENT
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){//a l'affichage du fragment
        v = inflater.inflate(R.layout.fragment_heure, container, false);//lie le fragment au fichier "fragment_heure.xml" pour le rendu graphique

        //COMPORTEMENT LIE A L'ACTTIVITE "Configuration"'
        // gestion du du format de l'heure lors de modification dans l'activite "Configuration"
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        Boolean date_heure = preferences.getBoolean("example_switch_heure",false);//recupere la valeur par defaut associee au widget du verrou par sa clef "example_switch_heure"
        if (date_heure){
            formatHeure = "HH:mm:ss\nEEE dd MMM";//format avec date
        }
        else{
            formatHeure = "HH:mm:ss";//format sans date
        }
        TextClock clock = (TextClock) v.findViewById(R.id.textClock);//liaison du widget à son identifiant dans le fichier "fragment_heure.xml"
        clock.setFormat12Hour(formatHeure);
        clock.setFormat24Hour(formatHeure);

        //affichage de l'etat de la batterie :
        BroadcastReceiver infos_batterie = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                int level = i.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                TextView etat = (TextView) v.findViewById(R.id.batterie);
                etat.setText("Niveau de batterie: " + Integer.toString(level) + "%");
            }
        };

        //recuperation de l'etat de la batterie
        this.getActivity().registerReceiver(infos_batterie, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        return v;
    }
}
