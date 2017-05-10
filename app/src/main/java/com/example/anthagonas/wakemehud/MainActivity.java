package com.example.anthagonas.wakemehud;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MainActivity <T extends Fragment> extends AppCompatActivity {

    protected ArrayList <T> fragmentList = new ArrayList <T>();
    protected int fragmentListPosition = 0;

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

        //Verification des autorisations de l'application :
        this.permissionAgenda = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR);

        // Ajout des differents fragments
        T bob = (T) new Heure();
        T notif = (T) new Notifications();
        T meteo = (T) new Meteo();
        T rss = (T) new Rss();
        T agenda = (T) new Agenda();
        T agregateur = (T) new Agregateur();

        fragmentList.add(bob);
        fragmentList.add(notif);
        fragmentList.add(meteo);
        fragmentList.add(rss);
        if (permissionAgenda != PackageManager.PERMISSION_GRANTED) // si l'acces aux calendriers n'est pas autorise :
        {
            //demande d'acces aux calendriers de l'appareil
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    permissionAgendaTAG);
            if (permissionAgenda == PackageManager.PERMISSION_GRANTED) // si l'utilisateur accepte l'acces :
            {
                fragmentList.add(agenda);
            }
        }
        else // si l'acces aux calendriers est autorise :
        {
            fragmentList.add(agenda);
        }
        fragmentList.add(agregateur);


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
                Intent myIntent = new Intent(MainActivity.this, Parametres.class); // Chargement de l'activite parametres
                startActivity(myIntent); // lancement de l'activite
            }
        });

        if (savedInstanceState != null)
        { return;} // la suite du code n'est a appliquer que si l'appli demarre

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.add(R.id.framelayout, fragmentList.get(0)); // affichage du fragment par defaut (içi, premier dans la liste)
        fragmentManager.commit();
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
                    fragmentManager.replace(R.id.framelayout, fragmentList.get(fragmentListPosition)); // Remplace le fragment actuel par le suivant dans la liste
                    fragmentManager.commit();
                }
                else if (Math.abs(deltaX) > MIN_DISTANCE && Math.abs(deltaY) < MIN_DISTANCE) // le deplacement du doigt de gauche a droite est significatif
                {
                    fragmentListPosition = (fragmentListPosition-1); // se déplace dans la liste des fragments
                    if (fragmentListPosition<0){ fragmentListPosition = fragmentList.size()-1;}
                    FragmentTransaction fragmentManager= getSupportFragmentManager().beginTransaction();
                    fragmentManager.replace(R.id.framelayout, fragmentList.get(fragmentListPosition)); // Remplace le fragment actuel par le suivant dans la liste
                    fragmentManager.commit();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

}
