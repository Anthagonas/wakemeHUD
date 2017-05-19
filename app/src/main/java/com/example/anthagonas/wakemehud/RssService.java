package com.example.anthagonas.wakemehud;

/**
 * Created by vtrjd on 04/05/2017.
 */

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

public class RssService extends IntentService {
                                                                                    //DESCRIPTION
    /*Service effectuant l'appel reseau et permettant de mettre en lien le xml du flux Rss à une URL donnee avec un parser */

                                                                                //DECLARATION DES VARIABLES
    private static final String RSS_LINK = "http://www.pcworld.com/index.rss"; /*lien utilise, implementation a changer avec l'EditText de l'activite "Configuration" permettant alors
    de renseigner plusieures adresses dans une liste. Les flux Rss de chaque element de cette surliste seront alors affiches*/
    public static final String ITEMS = "items";
    public static final String ACTION_RSS_PARSED = "com.example.anthagonas.wakemehud.ACTION_RSS_PARSED";

    public RssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("RssApp", "Service started");
        List<ObjetRss> objetsRss = null;// preparation de la liste d'objets Rss
        try {
            RssParser parser = new RssParser(); //Instanciation d'un parser Rss afin de renseigner les objets Rss avant de les inclure a la liste
            objetsRss = parser.parse(getInputStream(RSS_LINK));//renseignement des objets Rss
        } catch (XmlPullParserException | IOException e) {
            Log.w(e.getMessage(), e);
        }

        // résultat
        Intent resultIntent = new Intent(ACTION_RSS_PARSED);
        resultIntent.putExtra(ITEMS, (Serializable) objetsRss);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    //CONNECTION DU SERVICE ET APPEL RESEAU
    public InputStream getInputStream(String link) {
        try {
            URL url = new URL(link);
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            Log.w("RssApp", "Exception while retrieving the input stream", e);
            return null;
        }
    }
}

