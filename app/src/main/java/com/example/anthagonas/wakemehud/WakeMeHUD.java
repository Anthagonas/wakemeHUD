package com.example.anthagonas.wakemehud;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Hashtable;


public class WakeMeHUD<T extends Fragment> extends AppCompatActivity {

    //ArrayList contenant les clefs des fragments
    protected ArrayList <String> fragmentList = new ArrayList();

    //Valeur du fragment a afficher en premier
    protected int fragmentListPosition = 0;

    //Dictionnaire contenant les fragments
    Hashtable <String,T> dicoFragments = new Hashtable<String,T>();

    //Variable determinant si l'appli est autorisee a utiliser le fragment Agenda :
    int permissionAgenda;
    int permissionAgendaTAG = 42; // Valeur empirique, permettant juste de donner un code a la demande d'autorisation

    //Variables pour la detection de mouvements
    private float x1,x2,y1,y2; // position ou le doigt est appuye
    static final int MIN_DISTANCE = 150; // Valeur empirique



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// recuperation des preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Verification des autorisations de l'application :
        this.permissionAgenda = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR);



        T heure = (T) new Heure();
        T notif = (T) new Notifications();
        T meteo = (T) new Meteo();
        T rss = (T) new Rss();
        T agenda = (T) new Agenda();
        T agregateur = (T) new Agregateur();

        dicoFragments.put("heure", heure);
        dicoFragments.put("notif", notif);
        dicoFragments.put("meteo", meteo);
        dicoFragments.put("rss", rss);
        dicoFragments.put("agenda", agenda);
        dicoFragments.put("agregateur", agregateur);




// TODO : gestion des fragments a afficher
        fragmentList.add("heure");
        fragmentList.add("notif");
        fragmentList.add("meteo");
        fragmentList.add("rss");
        fragmentList.add("agregateur");
        //Verification permission puis ajout de la clef agenda
        if (permissionAgenda != PackageManager.PERMISSION_GRANTED) // si l'acces aux calendriers n'est pas autorise :
        {
            //demande d'acces aux calendriers de l'appareil
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    permissionAgendaTAG);
            if (permissionAgenda == PackageManager.PERMISSION_GRANTED) // si l'utilisateur accepte l'acces :
            {
                fragmentList.add("agenda");
            }
        }
        else // si l'acces aux calendriers est autorise :
        {
            fragmentList.add("agenda");
        }

        //Configuration du comportement des boutons
        Button hud = (Button) findViewById(R.id.hud);
        hud.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                FrameLayout layout = (FrameLayout) findViewById(R.id.framelayout); // Recupere le framelayout
                layout.setScaleY(-layout.getScaleY()); // retourne le framelayout ( et donc le fragment affiché)
            }
        });



        Button parametres = (Button) findViewById(R.id.parametres);
        parametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(WakeMeHUD.this, Configuration.class); // Chargement de l'activite parametres
                finish();
                startActivity(myIntent); // lancement de l'activite
            }
        });



        if (savedInstanceState != null)
        { return;} // la suite du code n'est a appliquer que si l'appli demarre

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.add(R.id.framelayout, dicoFragments.get(fragmentList.get(fragmentListPosition))); // affichage du fragment par defaut
        fragmentManager.commit();

        //verrouillage de l'ecran
        Boolean verrou = preferences.getBoolean("example_switch_verrou",false);
        if (verrou){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

    // Quitter l'appli lors de l'appui sur la touche retour
    @Override
    public void onBackPressed()
    {
        finish();
    }

    //Swipe entre les different fragments
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                float deltaX = x1 - x2;
                float deltaY = y1 - y2; // permet d'annuler le swipe si il est fait verticalement
                if (Math.abs(deltaX) > MIN_DISTANCE && (x1>x2) && Math.abs(deltaY) < MIN_DISTANCE) // le deplacement du doigt de droite a gauche est significatif
                {
                    fragmentListPosition = (fragmentListPosition+1)%fragmentList.size(); // se déplace dans la liste des fragments
                    FragmentTransaction fragmentManager= getSupportFragmentManager().beginTransaction();
                    fragmentManager.replace(R.id.framelayout, dicoFragments.get(fragmentList.get(fragmentListPosition))); // Remplace le fragment actuel par le suivant dans la liste
                    fragmentManager.commit();
                }
                else if (Math.abs(deltaX) > MIN_DISTANCE && Math.abs(deltaY) < MIN_DISTANCE) // le deplacement du doigt de gauche a droite est significatif
                {
                    fragmentListPosition = (fragmentListPosition-1); // se déplace dans la liste des fragments
                    if (fragmentListPosition<0){ fragmentListPosition = fragmentList.size()-1;}
                    FragmentTransaction fragmentManager= getSupportFragmentManager().beginTransaction();
                    fragmentManager.replace(R.id.framelayout, dicoFragments.get(fragmentList.get(fragmentListPosition))); // Remplace le fragment actuel par le suivant dans la liste
                    fragmentManager.commit();
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}
