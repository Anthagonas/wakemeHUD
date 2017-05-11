package com.example.anthagonas.wakemehud;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    long septJoursEnMilliSec = 604800000; // 7 jours convertis en millisecondes

    SimpleDateFormat formatDate = new SimpleDateFormat("EEE dd, hh:mm"); // Format de la date de debut de l'evenement


    //un parser URI permettant de recuperer le calendrier Anroid de l'appareil
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
        // Recupere la liste de tout les evenements
        Cursor eventCursor = contentResolver.query(URI_parser, CHAMPS, null, null, null);

        try {
            if (eventCursor.getCount() > 0) {
                eventCursor.moveToFirst();
                String nomEvent = eventCursor.getString(0);
                Calendar aujourdhui = Calendar.getInstance();
                long aujourdhuiMS = aujourdhui.getTimeInMillis(); // Date actuelle en millisec depuis 1 jan 1970
                long dateMS = eventCursor.getLong(1); // Date de debut de l'evenement en millisec depuis 1 jan 1970
                String date = formatDate.format(dateMS);
                String duree = eventCursor.getString(2);

                if (dateMS-aujourdhuiMS < septJoursEnMilliSec && dateMS-aujourdhuiMS > 0) // Si la date est comprise dans les 7 jours a venir
                {
                    this.nomEvenement.add(nomEvent);
                    this.dateDepartEvenement.add(date);
                    this.dureeEvenement.add(duree);
                }
                //Pour chaque element du curseur (donc chaque evenement)
                while (eventCursor.moveToNext()) {
                    nomEvent = eventCursor.getString(0);
                    dateMS = eventCursor.getLong(1);
                    duree = eventCursor.getString(2);
                    date = formatDate.format(dateMS); // conversion de la date en jour/heure (voir attribut format)
                    if (dateMS-aujourdhuiMS < septJoursEnMilliSec && dateMS-aujourdhuiMS > 0) // Si la date est comprise dans les 7 jours a venir
                    {
                        this.nomEvenement.add(nomEvent); // recuperation du nom de l'evenement
                        this.dateDepartEvenement.add(date); // recuperation de la date de depart
                        this.dureeEvenement.add(duree); // recuperation de la duree de l'evenement
                    }
                }
                if (nomEvenement.size() == 0)
                {
                    nomEvenement.add("Aucun Evenement");
                    dateDepartEvenement.add(" ");
                    dureeEvenement.add(" ");

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

    public void resetListeEvenements ()
    {
        nomEvenement.clear();
        dateDepartEvenement.clear();
        dureeEvenement.clear();
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
