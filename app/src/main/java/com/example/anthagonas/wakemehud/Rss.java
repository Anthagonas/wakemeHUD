package com.example.anthagonas.wakemehud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
//liste extensive
import android.widget.ExpandableListView;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class Rss extends Fragment implements OnItemClickListener {
                                                                                    //DESCRIPTION
    /*Fragment instancie dans l'activite "WakeMeHUD". Permet l'affichage d'un flux rss recuperees par appel reseau utilisant un service en dans un widget de type "ListView".*/

                                                                                //DECLARATION DES VARIABLES
    private ProgressBar progressBar; //barre d'attente d'affichage des donnees
    private ListView listView; //ListView contenant les donnees


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss, container, false);//lie le fragment au fichier "fragment_rss.xml" pour le rendu graphique
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);/*liaison de la barre d'attente à son identifiant dans le fichier "fragment_heure.xml"*/
        listView = (ListView) view.findViewById(R.id.listView);/*liaison de la ListView à son identifiant dans le fichier "fragment_heure.xml"*/

        //PERMISSION DE SWIPE
        //permet de swiper depuis sur le ScrollView du fragment ver sun autre. Sinon, le swipe n'est pas pris en compte
        listView.setOnItemClickListener(this);
        listView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return getActivity().onTouchEvent(event);
            }
        });
        return view;
    }

    //Le service effectuant l'appel reseau est mis en route a la creation de l'activite "WakeMeHUD"
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startService();
    }

    //DEMARRAGE DU SERVICE
    //methode permettant de demarrer le service
    private void startService() {
        Intent intent = new Intent(getActivity(), RssService.class);
        getActivity().startService(intent);
    }

    //FIN DU SERVICE
    //a la fin de tache du service, le resultat est envoye a un BroadcastReceiver
    private BroadcastReceiver resultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);//enleve la barre d'attente
            List<ObjetRss> items = (List<ObjetRss>) intent.getSerializableExtra(RssService.ITEMS);//affichage de la liste remplie d'"ObjetRSS"
            if (items != null) {
                RssAdapteur adapter = new RssAdapteur(getActivity(), items);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "An error occurred while downloading the rss feed.",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RssAdapteur adapter = (RssAdapteur) parent.getAdapter();
        ObjetRss item = (ObjetRss) adapter.getItem(position);
        Uri uri = Uri.parse(item.getLink());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(RssService.ACTION_RSS_PARSED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(resultReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(resultReceiver);
    }
}
