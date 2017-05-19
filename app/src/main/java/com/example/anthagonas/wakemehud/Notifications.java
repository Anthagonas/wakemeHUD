package com.example.anthagonas.wakemehud;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by anthagonas on 29/03/17.
 */

public class Notifications extends Fragment {
                                                                                        //DESCRIPTION
    /*Fragment instancie par l'activite WakeMeHUD mais encore non implemente car nombreux echecs pour raison encore
    indeterminee. Permettrait a terme d'afficher le flux de notification "push" de l'appareil*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_notifications, container, false);//lie le fragment au fichier "fragment_notifications.xml" pour le rendu graphique
    }
}
