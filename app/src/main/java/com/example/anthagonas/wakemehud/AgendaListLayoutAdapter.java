package com.example.anthagonas.wakemehud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.agendarows_layout, null);
            holder = new ViewHolder();
            holder.nom = (TextView) convertView.findViewById(R.id.evenement_nom);
            holder.date = (TextView) convertView.findViewById(R.id.evenement_date);
            holder.duree = (TextView) convertView.findViewById(R.id.evenement_duree);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nom.setText(this.noms.get(position));
        holder.date.setText(this.dates.get(position));
        holder.duree.setText(this.duree.get(position));

        return convertView;
    }


    static class ViewHolder {
        TextView nom;
        TextView date;
        TextView duree;
    }
}
