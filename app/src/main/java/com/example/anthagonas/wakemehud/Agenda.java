package com.example.anthagonas.wakemehud;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by anthagonas on 29/03/17.
 */

public class Agenda extends Fragment {
    //ListView affichant les evenements
    ListView listView;
    // AgendaList contenant les evenements
    AgendaList evenements;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);
        //creation de la liste d'evenement
        this.evenements = new AgendaList(this.getContext());
        this.evenements.resetListeEvenements(); // pour remettre la liste a 0
        //recuperation des evenements du calendrier android
        this.evenements.initList();
        //modification de l'affichage de la ListView pour afficher les evenements
        this.listView = (ListView) view.findViewById(R.id.eventlistview);
        listView.setAdapter(new AgendaListLayoutAdapter(this.getContext(),evenements.getNomEvenement(),
                evenements.getDateDepartEvenement(),evenements.getDureeEvenement()));
        listView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return getActivity().onTouchEvent(event);
            }
        });
        return view;

    }
}
