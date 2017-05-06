package com.example.anthagonas.wakemehud;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Anthagonas on 05/05/2017.
 */

public class AgendaList {

    //Le nom des champs a recuperer des evenements des calendriers
    public static String[] CHAMPS =
            {
                    CalendarContract.Events.TITLE,
                    CalendarContract.Events.DTSTART,
                    CalendarContract.Events.DURATION
            };


    //un parser URI permettant de recuperer tout les calendriers synchronises avec l'appareil
    public static final Uri URI_parser = Uri.parse("content://com.android.calendar/events"); // Pour l'API 2.2 et plus

    //Creation d'un contentResolver, qui permet d'acceder aux valeurs des calendriers
    ContentResolver contentResolver;
    ArrayList<String> nomEvenement = new ArrayList<String>();
    ArrayList<String> dateDepartEvenement = new ArrayList<String>();
    ArrayList<String> dureeEvenement = new ArrayList<String>();



    //Constructeur de la classe
    public AgendaList(Context ctx) {
        contentResolver = ctx.getContentResolver();
    }

    public void initList() {
        // Recupere la liste de tout les evenements des agendas synchronises avec l'appareil
        Cursor eventCursor = contentResolver.query(URI_parser, CHAMPS, null, null, null);

        try {
            if (eventCursor.getCount() == 0) {
                eventCursor.moveToFirst();
                this.nomEvenement.add(eventCursor.getString(0));
                this.dateDepartEvenement.add(eventCursor.getString(1));
                this.dureeEvenement.add(eventCursor.getString(2));
                //Pour chaque element du curseur (donc chaque evenement)
                while (eventCursor.moveToNext()) {
                    String idEvent = eventCursor.getString(0);
                    String date = eventCursor.getString(1);
                    String duree = eventCursor.getString(2);
                    this.nomEvenement.add(idEvent); // recuperation du nom de l'evenement
                    this.dateDepartEvenement.add(date); // recuperation de la date de depart
                    this.dureeEvenement.add(duree); // recuperation de la duree de l'evenement
                }
            }
            else
            {
                nomEvenement.add("Aucun Evenement");
                dateDepartEvenement.add(" ");
                dureeEvenement.add(" ");
            }
        } catch (AssertionError ex) {Log.e("AssertionError","Bug majList() :",ex);}
        eventCursor.close();
    }

    //getters
    public ArrayList<String > getNomEvenement()
    {
        return nomEvenement;
    }
    public ArrayList<String > getDateDepartEvenement()
    {
        return dateDepartEvenement;
    }
    public ArrayList<String > getDureeEvenement()
    {
        return dureeEvenement;
    }

}
