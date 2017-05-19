package com.example.anthagonas.wakemehud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anthagonas on 06/05/2017.
 */

public class AgendaListLayoutAdapter extends BaseAdapter {

    private ArrayList<String> noms; // ArrayList contenant les noms des evenements
    private ArrayList<String> dates; // ArrayList contenant les dates de début des evenements
    private ArrayList<String > duree; // ArrayList contenant la duree des evenements

    private LayoutInflater mInflater;

    public AgendaListLayoutAdapter(Context context, ArrayList<String> noms,ArrayList<String> dates,ArrayList<String> duree) {
        this.noms = noms;
        this.dates = dates;
        this.duree = duree;
        mInflater = LayoutInflater.from(context);
    }

    //methode a implementer obligatoirement, retourne le nombre d'items
    public int getCount()
    {
        return noms.size();
    }


    //methode a implementer obligatoirement, retourne l'item a la position X
    public Object getItem(int position) {
        String[] evenement = {noms.get(position), dates.get(position),duree.get(position)};
        return evenement;
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) { // Permet de recuperer 1 item a afficher dans la ListView Agenda
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.agendaligne_layout, null); // recupere le layout pour l'affichage de l'item
            holder = new ViewHolder();
            //Recuperation des TextView a remplir dans ce layout via un ViewHolder
            holder.nom = (TextView) convertView.findViewById(R.id.evenement_nom);
            holder.date = (TextView) convertView.findViewById(R.id.evenement_date);
            holder.duree = (TextView) convertView.findViewById(R.id.evenement_duree);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //remplissage du ViewHolder qui sera affiche dans une ligne de la ListView d'Agenda
        holder.nom.setText(this.noms.get(position));
        holder.date.setText(this.dates.get(position));
        holder.duree.setText(this.duree.get(position));

        return convertView;
    }


    static class ViewHolder { // l'item qui va contenir nos 3 strings detaillant l'evenement
        TextView nom;
        TextView date;
        TextView duree;
    }
}
