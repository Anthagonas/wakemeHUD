package com.example.anthagonas.wakemehud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by anthagonas on 28/03/17.
 */

public class Heure extends Fragment
{
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_heure, container, false);

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
