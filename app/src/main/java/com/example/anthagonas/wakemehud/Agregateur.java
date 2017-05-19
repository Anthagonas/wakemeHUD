package com.example.anthagonas.wakemehud;

import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.support.v4.app.RemoteInput;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by anthagonas on 29/03/17.
 */

public class Agregateur extends Fragment {
                                                                        //DESCRIPTION
    /*Fragment instancie dans l'activite "WakeMeHUD". Permet l'affichage du widget heure et gere le format du dit widget par l'activite "Configuration".
     Affiche également l'etat courant de la batterie du telephone*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_agregateur, container, false);/*lie le fragment au fichier "fragment_agregateur.xml" pour le rendu graphique.
        L'integralite du rendu de ce fragment se fait au niveau du xml avec l'inclusion de trois fragments dans le "LinearLayout"*/
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);
        linearLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return getActivity().onTouchEvent(event);
            }
        });
        return v;
    }
}
