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
                                                                                //DESCRIPTION
    /*Activite principale de l'application : instancie les fragments, prend en charge et applique les preferences donnees par l'utilisateur
    dans l'activite "Configuration" pour le verrouillage de l'ecran et la gestion des fragmens (non implementee pour le moment car difficulte
    a recuperer les valeurs des ArrayList du xml des Configurations afin de les comparer aux clefs de notre ArrayList de fragments). Gere les
    swipes et les boutons HUD et parametres*/

                                                                            //DECLARATION DES VARIABLES

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


                                                                            //A LA CREATION DE L'ACTIVITE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //lie l'activite au fichier "activity_main.xml" pour le rendu graphique

        // RECUPERATION DES PREFERENCES
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Verification des autorisations de l'application :
        this.permissionAgenda = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);


        //INSTANCIATION DES FRAGMENTS
        T heure = (T) new Heure();
        T notif = (T) new Notifications();
        T meteo = (T) new Meteo();
        T rss = (T) new Rss();
        T agenda = (T) new Agenda();
        T agregateur = (T) new Agregateur();

        /*preparation des dictionnaires associant les fragments a leurs clefs, ceci permettra de selectionner les fragments affiches et celui affiche
        par defaut au lancement de l'application selon les preferences de l'utilisateur definie dans l'activite "Configuration"*/
        dicoFragments.put("heure", heure);
        dicoFragments.put("notif", notif);
        dicoFragments.put("meteo", meteo);
        dicoFragments.put("rss", rss);
        dicoFragments.put("agenda", agenda);
        dicoFragments.put("agregateur", agregateur);

        /*ajout des clefs des fragments dans l'ArrayList, cet ajout, pour l'instant automatique sera a modifier
        quand la gestion des fragments depuis l'activité "Configuration" sera implementee*/
        fragmentList.add("heure");
        fragmentList.add("notif");
        fragmentList.add("meteo");
        fragmentList.add("rss");
        fragmentList.add("agregateur");

        /*Verification de la permission puis ajout de la clef agenda. Cette verification de permission doit etre effectuee pour les donnees contenues dans l'appareil,
        les donnees recuperees par appels reseau n'ont pas besoin de demande de permission et doivent juste être declares dans le manifeste*/
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


        if (savedInstanceState != null)
        { return;} // la suite du code n'est a appliquer que si l'application demarre

        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
        fragmentManager.add(R.id.framelayout, dicoFragments.get(fragmentList.get(fragmentListPosition))); // affichage du fragment par defaut
        fragmentManager.commit();

        //COMPORTEMENT DES BOUTONS

        //bouton HUD
        Button hud = (Button) findViewById(R.id.hud); //liaison du bouton HUD à son identifiant dans le fichier "activity_main.xml"
        hud.setOnClickListener(new View.OnClickListener(){ //attribution d'un listener de clic au bouton
            @Override
            public void onClick(View v) //au clic :
            {
                FrameLayout layout = (FrameLayout) findViewById(R.id.framelayout); // Recupere le framelayout
                layout.setScaleY(-layout.getScaleY()); // retourne le framelayout ( et donc le fragment affiché)
            }
        });


        //bouton parametre
        Button parametres = (Button) findViewById(R.id.parametres);//liaison du bouton parametre à son identifiant dans le fichier "activity_main.xml"
        parametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { //au clic :
                Intent myIntent = new Intent(WakeMeHUD.this, Configuration.class); // Instanciation de l'activite "Configuration" depuis l'activite "WakeMeHUD"
                /*fin de l'activite WakeMeHUD necessaire pour la prise en compte dynamique des changements des parametres par l'utilisateur
                (obligation de quitter l'application et de la relancer, sinon). se renseigner sur le methode onResume() et onRestart() pour eviter de fermer l'activite :*/
                finish();
                startActivity(myIntent); // lancement de l'activite "Configuration"
            }
        });


        //COMPORTEMENT LIE A L'ACTTIVITE "Configuration"'
        // gestion du verrouillage automatique de l'ecran lors de modification dans l'activite "Configuration"
        Boolean verrou = preferences.getBoolean("example_switch_verrou",false); //recupere la valeur par defaut associee au widget du verrou par sa clef "example_switch_verrou"
        if (verrou){//
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//empeche le verrouillage par ajout de "flag"
        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//enleve les "flags" qui empechent le verrouillage
        }

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
    // Quitter l'application lors de l'appui sur la touche retour
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
