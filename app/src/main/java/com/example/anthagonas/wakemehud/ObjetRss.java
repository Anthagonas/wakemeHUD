package com.example.anthagonas.wakemehud;

/**
 * Created by vtrjd on 04/05/2017.
 */

public class ObjetRss {

                                                                                    //DESCRIPTION
    /*Objet Rss caracterise par un titre et un lien d'element de flux Rss. Element de base de la liste*/

    private final String title;
    private final String link;

    //CONSTRUCTEUR
    public ObjetRss(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
