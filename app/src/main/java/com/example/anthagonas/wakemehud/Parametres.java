package com.example.anthagonas.wakemehud;

import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Vincent on 30/03/17.
 */

public class Parametres extends AppCompatActivity {

                                                //DECLARATIONS
    //listes pour fragments
    private ListViewNonScrollable multi_parametres=null;
    private ListViewNonScrollable simple_parametre_fragment=null;
    private String[] fragments_coche={"Heure","Notifications", "Météo","Flux RSS", "Calendrier","Aggrégateur"};

    //format heure
    private ListViewNonScrollable simple_parametre_format_heure=null;
    private String[] format_heure={"Heure","Heure et Date"};

    //verrou
    private ToggleButton verrouillage=null;
    private TextView etat_verrou;

    //bouton sauvegarde des parametres
    private Button valide=null;

    //bouton retour au fragment en cours
    private Button retour=null;

    //luminosite
    private SeekBar luminosite;
    private int brightness; //pour stocker la valeur de luminosite
    TextView txtPerc;
    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current window
    private Window window;

                                                    //METHODES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        //texte etat_verrou invisible si verrouillage automatique actif
        etat_verrou=(TextView)findViewById(R.id.etat_verrou);
        etat_verrou.setVisibility(View.INVISIBLE);

        //attribution des id aux ListViews
        multi_parametres = (ListViewNonScrollable) findViewById(R.id.choix_fragments);
        simple_parametre_fragment=(ListViewNonScrollable)findViewById(R.id.unique_liste_fragment);
        simple_parametre_format_heure=(ListViewNonScrollable)findViewById(R.id.unique_liste_format_heure);

        //SeekBar
        luminosite=(SeekBar) findViewById(R.id.seekBar);
        txtPerc = (TextView) findViewById(R.id.txtPercentage);

        //attribution des id aux boutons
        valide = (Button) findViewById(R.id.valider);
        retour = (Button) findViewById(R.id.retour);
        verrouillage = (ToggleButton) findViewById(R.id.verrouillage);

        //gestion des ListView
        simple_parametre_fragment.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, fragments_coche));
        simple_parametre_fragment.setItemChecked(0, true);

        simple_parametre_format_heure.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, format_heure));
        simple_parametre_format_heure.setItemChecked(0, true);

        multi_parametres.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, fragments_coche));
        //assez sale
        multi_parametres.setItemChecked(0, true);
        multi_parametres.setItemChecked(1, true);
        multi_parametres.setItemChecked(2, true);
        multi_parametres.setItemChecked(3, true);
        multi_parametres.setItemChecked(4, true);
        multi_parametres.setItemChecked(5, true);










        valide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Parametres.this, "Données modifiées", Toast.LENGTH_LONG).show();
            }
        });
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        verrouillage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEtatVerouillage(v);
            }
        });
    }


    public void changeEtatVerouillage (View view)
    {
        //si le togglebutton est en mode "on", ca renvoie "true", sinon, ca revoie "false"
        boolean check=((ToggleButton)view).isChecked();
        //si le toggle est en mode "on"
        if(check)
        {
            etat_verrou.setVisibility(View.VISIBLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }
        else
        {
            etat_verrou.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

}
