package com.example.anthagonas.wakemehud;

/**
 * Created by vtrjd on 04/05/2017.
 */

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RssParser {
                                                                                //DESCRIPTION
    /*parser décomposant les documents xml et renseignant les objetRss en se servant des balises de ces liens xml comme reperes */

                                                                        //DECLARATION DES VARIABLES
    private static final String TAG_TITLE = "title";
    private static final String TAG_LINK = "link";
    private static final String TAG_RSS = "rss";
    private final String ns = null;

    //PREPARE LE PARSER AU FLUX ENTRANT DONNE PAR LE SERVICE
    public List<ObjetRss> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }

    //LIT LE FLUX ENTRANT DONNE PAR LE SERVICE
    //Renseigne les objetRss par le titre et le lien lorsque les données du flux en lui meme ne correspondent ni a une balise de titre ni a une balise de lien
    private List<ObjetRss> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, TAG_RSS);
        String title = null;
        String link = null;
        List<ObjetRss> items = new ArrayList<ObjetRss>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TAG_TITLE)) {
                title = readTitle(parser);
            } else if (name.equals(TAG_LINK)) {
                link = readLink(parser);
            }
            if (title != null && link != null) {
                ObjetRss item = new ObjetRss(title, link);
                items.add(item);
                title = null;
                link = null;
            }
        }
        return items;
    }

    private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_LINK);
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_LINK);
        return link;
    }

    private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_TITLE);
        return title;
    }


    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
