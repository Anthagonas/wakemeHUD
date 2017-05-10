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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Meteo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Meteo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    final String URL_BASE = "http://api.openweathermap.org/data/2.5/forecast";
    final String URL_COORD = "?lat=44.8079982&lon=-0.6101602";
    final String URL_UNIT = "&units=metric";
    final String URL_API = "&APPID=77a9aa0c33440f409f740a67601c43d5";

    //initialisation view
    private ScrollView scrollView;
    private TextView maxTempView;
    private TextView minTempView;
    private TextView statusWeather;
    private TextView cityName;

    private MeteoModele MeteoModele = new MeteoModele();

    // 44.8079982,-0.6101602
    public Meteo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Meteo newInstance(String param1, String param2) {
        Meteo fragment = new Meteo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_weather, container, false);
        final String url = URL_BASE + URL_COORD + URL_UNIT + URL_API;
        scrollView = (ScrollView) v.findViewById(R.id.scrollView);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response){

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

                    // Set Value
                    MeteoModele.setNomVille(nameCity);
                    MeteoModele.setPays(country);
                    MeteoModele.setTemp_min(temp_min.intValue());
                    MeteoModele.setTemp_max(temp_max.intValue());
                    MeteoModele.setWeatherStatus(weatherStatus);

                    //declaration

                    maxTempView = (TextView) v.findViewById(R.id.maxTempView);
                    minTempView = (TextView) v.findViewById(R.id.minTempView);
                    statusWeather =(TextView) v.findViewById(R.id.weatherStatus);
                    cityName = (TextView) v.findViewById(R.id.cityName);

                    //dynamic data like API

                    maxTempView.setText("Température maximale : "+String.valueOf(MeteoModele.getTemp_max())+"°");
                    minTempView.setText("Température minimale : "+String.valueOf(MeteoModele.getTemp_min()+"°"));
                    //mettre en francais ou metttre une icone ?
                    statusWeather.setText("Temps : "+ MeteoModele.getWeatherStatus());
                    cityName.setText("Lieu : "+ MeteoModele.getNomVille());



                    Log.i("Response City : ", MeteoModele.getNomVille()+" "+ MeteoModele.getPays());
                } catch (JSONException e) {
                    Log.i("Error",e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error ",error.getLocalizedMessage());

            }
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
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
