package com.example.anthagonas.wakemehud;

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ScrollView;
        import android.widget.TextView;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.Volley;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;


public class Meteo extends Fragment {
                                                                                //DESCRIPTION
    /*Fragment instancie dans l'activite "WakeMeHUD". Permet l'affichage des donnees meteo recuperees par appel reseau Asynctask en TextViews (widgets de texte).
    Code implemente par Sami, certains points restent obscurs lors de la redaction de ce commentaire mais le code a ete reduit et est renseigne dans sa majorite*/

                                                                            //DECLARATION DES VARIABLES

    //donnees necessaires pour monter l'URL necessaire a l'appel reseau
    final String URL_BASE = "http://api.openweathermap.org/data/2.5/forecast";
    final String URL_COORD = "?lat=44.8079982&lon=-0.6101602";
    final String URL_UNIT = "&units=metric";
    final String URL_API = "&APPID=77a9aa0c33440f409f740a67601c43d5";

    //donnees du rendu graphique
    private ScrollView scrollView;
    private TextView maxTempView;
    private TextView minTempView;
    private TextView statusWeather;
    private TextView cityName;

    //instanciation d'un objet meteo modele auquel sera attribue les donnees parsees par la requete
    private MeteoModele MeteoModele = new MeteoModele();

    // 44.8079982,-0.6101602
    public Meteo() {
        // Required empty public constructor
    }

                                                                                //A L'AFFICHAGE DU FRAGMENT
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {//a l'affichage du fragment
        final View v = inflater.inflate(R.layout.fragment_weather, container, false);//lie le fragment au fichier "fragment_weather.xml" pour le rendu graphique
        final String url = URL_BASE + URL_COORD + URL_UNIT + URL_API;//montage de l'URL pour l'appel reseau
        scrollView = (ScrollView) v.findViewById(R.id.scrollView);/*liaison du widget scroolview (utile en cas de surnombre d'information a l'ecran) contenant les TextViews a son identifiant
         dans le fichier "fragment_heure.xml"*/

        //APPEL RESEAU
        /*Appel utilisant une jsonObjetRequest. Il j'agit d'une methode prennant un lien url en parametre et retourne un objet JSON pouvant contenir n'importe quel type de donnees.*/
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            // parser de l'objet JSON
            public void onResponse(JSONObject response){

                //L'objet JSON obtenu a l'URL donnee est parse
                try {
                    JSONObject jsonObjectCity = response.getJSONObject("city");
                    String nameCity = jsonObjectCity.getString("name");
                    String country = jsonObjectCity.getString("country");

                    JSONArray list = response.getJSONArray("list");
                    JSONObject jsonListIndex = list.getJSONObject(0);
                    JSONObject jsonObjectMain = jsonListIndex.getJSONObject("main");
                    JSONArray weatherArray = jsonListIndex.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);


                    //temperature min et max
                    Double temp_min = jsonObjectMain.getDouble("temp_min");
                    Double temp_max = jsonObjectMain.getDouble("temp_max");

                    String weatherStatus = weather.getString("main");

                    //attribution des valeurs parsees a l'objet "MeteoModele"
                    MeteoModele.setNomVille(nameCity);
                    MeteoModele.setPays(country);
                    MeteoModele.setTemp_min(temp_min.intValue());
                    MeteoModele.setTemp_max(temp_max.intValue());
                    MeteoModele.setWeatherStatus(weatherStatus);

                    //declaration
                    /*liaison des widgets TextViews a leurs identifiants dans le fichier "fragment_heure.xml"*/
                    maxTempView = (TextView) v.findViewById(R.id.maxTempView);
                    minTempView = (TextView) v.findViewById(R.id.minTempView);
                    statusWeather =(TextView) v.findViewById(R.id.weatherStatus);
                    cityName = (TextView) v.findViewById(R.id.cityName);

                    //dynamic data like API
                    //ajout de l'unite aux degres
                    maxTempView.setText("Température maximale : "+String.valueOf(MeteoModele.getTemp_max())+"°C");
                    minTempView.setText("Température minimale : "+String.valueOf(MeteoModele.getTemp_min()+"°C"));
                    //mettre en francais ou metttre une icone ?
                    statusWeather.setText("Temps : "+ MeteoModele.getWeatherStatus());
                    cityName.setText("Lieu : "+ MeteoModele.getNomVille());



                    Log.i("Response City : ", MeteoModele.getNomVille()+" "+ MeteoModele.getPays());
                } catch (JSONException e) {
                    Log.i("Error",e.getMessage());
                }


            }
            //si erreur
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error ",error.getLocalizedMessage());

            }
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);

        //PERMISSION DE SWIPE
        //permet de swiper depuis sur le ScrollView du fragment ver sun autre. Sinon, le swipe n'est pas pris en compte
        scrollView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return getActivity().onTouchEvent(event);
            }
        });
        return v;
    }

}
