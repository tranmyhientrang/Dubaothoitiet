package com.example.weatherdesign;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Search extends AppCompatActivity {

    private ImageButton cancelButton;
    public ImageButton searchButton;
    private Button goButton;
    public EditText etCity;
    public ImageButton listen;
    static TextView[] timeCity = new TextView[3];
    static TextView[] favorCityy = new TextView[3];
    static TextView[] tempCity = new TextView[3];
    static ImageButton reset;
    TextToSpeech textToSpeech;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Context context = Search.this;
    static int count = 0;
    static String cityfind;
    static int dem = 0;
    static String[] favorCity = new String[3];
    private final String url1 = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "dd7c47eafd554c518e88d644137dae40";
    DecimalFormat df = new DecimalFormat("#.##");


    public static class getWeather extends AsyncTask<String , Void , String>  {
        public ImageView imageView;
        public TextView Locations;
        public TextView Template;
        public TextView Weather;
        public ProgressBar aqi;
        public TextView indexAir;
        public TextView des1, des2, des3, des4, des5, des6, des7, des8, des9;




        public String icon="",temp,city,description,iconUrl, airqua;

        private MainActivity mainActivity;
        private Favorite favorite;
        int option = 0;
        @Override


        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;


            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);


                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                // Toast.makeText(mainActivity.this, "Could not find weather", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("data");
                JSONArray arr = new JSONArray(weatherInfo);
                JSONObject a=null;
                String[] des = new String[10];


                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String[] split = jsonPart.getString("temp").split("\\.", 0);
                    temp = split[0] + "℃";

                    city = jsonPart.getString("city_name");
                    airqua = jsonPart.getString("aqi");
                    des[1] = jsonPart.getString("sunrise");
                    des[2] = jsonPart.getString("sunset");
                    des[3] = jsonPart.getString("rh") + "%";
                    des[4] = jsonPart.getString("wind_cdir") + " " + jsonPart.getString("wind_spd") + " m/s";
                    des[5] = jsonPart.getString("app_temp") + "℃";
                    des[6] = jsonPart.getString("pres") + " mbar";
                    des[7] = jsonPart.getString("precip") + " mm/hr";
                    des[8] = jsonPart.getString("vis") + " km";
                    des[9] = jsonPart.getString("uv");
                    a= jsonPart.getJSONObject("weather");
                    String ngay = jsonPart.getString("datetime");

                }
                icon = a.getString("icon");
                description = a.getString("description");
                Weather.setText(description);
                Template.setText(temp);
                Locations.setText(city);
                iconUrl="https://www.weatherbit.io/static/img/icons/" + icon + ".png";
                Picasso.with(imageView.getContext()).load(iconUrl).into(imageView);
                int index = Integer.parseInt(airqua);
                aqi.setProgress(index);
                String stt = "";
                if (index >= 0 && index <=50) {
                    stt = "GOOD";
                    aqi.setProgressTintList(ColorStateList.valueOf(Color.rgb(50, 205, 50)));
                }
                else if (index > 50 && index <= 100) {
                    stt = "MODERATE";
                    aqi.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                }
                else if (index > 100 && index <= 150) {
                    stt = "Unhealthy for Sensitive Groups";
                    aqi.setProgressTintList(ColorStateList.valueOf(Color.rgb(205, 102, 29)));
                }
                else if (index > 150 && index <= 200) {
                    stt = "Unhealthy";
                    aqi.setProgressTintList(ColorStateList.valueOf(Color.RED));
                }
                else if (index > 200 && index <= 300) {
                    stt = "Very Unhealthy";
                    aqi.setProgressTintList(ColorStateList.valueOf(Color.rgb(205, 0,205)));
                }
                else {
                    stt = "HAZARDOUS";
                    aqi.setProgressTintList(ColorStateList.valueOf(Color.rgb(136, 0,0)));
                }
                indexAir.setText(airqua + " - " +stt);

                des1.setText(des[1]);
                des2.setText(des[2]);
                des3.setText(des[3]);
                des4.setText(des[4]);
                des5.setText(des[5]);
                des6.setText(des[6]);
                des7.setText(des[7]);
                des8.setText(des[8]);
                des9.setText(des[9]);


            }catch (JSONException e) {
                Toast.makeText(mainActivity, "Could not find weather", Toast.LENGTH_LONG).show();
                Toast.makeText(favorite, "abcd", Toast.LENGTH_LONG).show();
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        etCity = (EditText) findViewById(R.id.cityName);
        searchButton = (ImageButton) findViewById(R.id.searchButton) ;
        goButton = (Button) findViewById(R.id.goButton);
        cancelButton = (ImageButton) findViewById(R.id.cancelButton);
        listen = (ImageButton) findViewById(R.id.listen);

        listen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                speech_TO_TEXT();
            }
        });
        favorCityy[0] = (TextView) findViewById(R.id.favorCity1);
        favorCityy[1] = (TextView) findViewById(R.id.favorCity2);
        favorCityy[2] = (TextView) findViewById(R.id.favorCity3);

        timeCity[0] = (TextView) findViewById(R.id.timeCity1);
        timeCity[1] = (TextView) findViewById(R.id.timeCity2);
        timeCity[2] = (TextView) findViewById(R.id.timeCity3);

        tempCity[0] = (TextView) findViewById(R.id.tempCity1);
        tempCity[1] = (TextView) findViewById(R.id.tempCity2);
        tempCity[2] = (TextView) findViewById(R.id.tempCity3);

        reset = (ImageButton) findViewById(R.id.reset);
        reset.setVisibility(View.INVISIBLE);

        if(count == 1 ) {
            Log.i("test","SNSDDD"+ favorCity[0]);
            getFavor1(favorCity[0]);
        }
        if(count == 2) {
            getFavor1(favorCity[0]);
            getFavor2(favorCity[1]);

        }
        if(count == 3) {
            getFavor1(favorCity[0]);
            getFavor2(favorCity[1]);
            getFavor3(favorCity[2]);

        }

    }
    public void goButtonPressed(View view) {
        Getweather();
        dem=1;

        startActivity(new Intent(this, MainActivity.class));
    }
    public void cancelButtonPressed(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
    public void resetButtonPressed(View view) {
        for(int i = 0; i < 3; i++) {
            favorCity[i] = "";

        }
        startActivity(new Intent(this, Search.class));
    }

    static int k = 0;
    public String Getweather(){
        String place=etCity.getText().toString();
        String[] temp = place.split(",");
        cityfind = temp[0];
        favorCity[k] = cityfind;
        k++;
        return cityfind;
    }
    static String[] city = new String[3];
    static String[] temp = new String[3];
    static String[] time = new String[3];
    static String[] timezone = new String[3];

 public void getFavor1(String s1) {
         if (s1 != null) {
             reset.setVisibility(View.VISIBLE);
             String url = "https://api.weatherbit.io/v2.0/current?city=" + s1 + "&key=dd7c47eafd554c518e88d644137dae40";
             RequestQueue requestQueue = Volley.newRequestQueue(Search.this);
             StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                     new Response.Listener<String>() {
                         @RequiresApi(api = Build.VERSION_CODES.O)
                         @Override
                         public void onResponse(String response) {
                             try {
                                 JSONObject jsonObject = new JSONObject(response);
                                 String list = jsonObject.getString("data");
                                 JSONArray arr = new JSONArray(list);
                                 JSONObject get = null;
                                 String[] iconUrl = new String[8];
                                 String[] city1 = new String[4];


                                 for (int i = 0; i < arr.length(); i++) {
                                     JSONObject jsonPart = arr.getJSONObject(i);
                                     city1[0] = jsonPart.getString("city_name");
                                     city1[1] = jsonPart.getString("temp");
                                     city1[2] = jsonPart.getString("ts");
                                     city1[3] = jsonPart.getString("timezone");

                                 }
                                 String[] split = city1[1].split("\\.");
                                 favorCityy[0].setText(city1[0]);
                                 tempCity[0].setText(split[0] + "℃");

                                 long unixSeconds = Long.parseLong(city1[2]);
                                 Date date = new java.util.Date(unixSeconds*1000L);
                                 SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                                 sdf.setTimeZone(java.util.TimeZone.getTimeZone(ZoneId.of(city1[3])));
                                 //sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
                                 String formattedDate = sdf.format(date);
                                 String[] time = formattedDate.split(" ");
                                 time = time[1].split("\\:");
                                 timeCity[0].setText(time[0] + ":" + time[1]);



                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }
                         }
                     },
                     new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {


                         }

                     });
             requestQueue.add(stringRequest);
         }
 }

 public void getFavor2(String s1) {
     if (s1 != null) {
         String url = "https://api.weatherbit.io/v2.0/current?city=" + s1 + "&key=dd7c47eafd554c518e88d644137dae40";
         RequestQueue requestQueue = Volley.newRequestQueue(Search.this);
         StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                 new Response.Listener<String>() {
                     @RequiresApi(api = Build.VERSION_CODES.O)
                     @Override
                     public void onResponse(String response) {
                         try {
                             JSONObject jsonObject = new JSONObject(response);
                             String list = jsonObject.getString("data");
                             JSONArray arr = new JSONArray(list);
                             JSONObject get = null;
                             String[] iconUrl = new String[8];
                             String[] city1 = new String[4];


                             for (int i = 0; i < arr.length(); i++) {
                                 JSONObject jsonPart = arr.getJSONObject(i);
                                 city1[0] = jsonPart.getString("city_name");
                                 city1[1] = jsonPart.getString("temp");
                                 city1[2] = jsonPart.getString("ts");
                                 city1[3] = jsonPart.getString("timezone");

                             }
                             String[] split = city1[1].split("\\.");
                             favorCityy[1].setText(city1[0]);
                             tempCity[1].setText(split[0] + "℃");

                             long unixSeconds = Long.parseLong(city1[2]);
                             Date date = new java.util.Date(unixSeconds * 1000L);
                             SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                             sdf.setTimeZone(java.util.TimeZone.getTimeZone(ZoneId.of(city1[3])));
                             //sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
                             String formattedDate = sdf.format(date);
                             String[] time = formattedDate.split(" ");
                             time = time[1].split("\\:");
                             timeCity[1].setText(time[0] + ":" + time[1]);


                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {


                     }

                 });
         requestQueue.add(stringRequest);
     }
 }

 public void getFavor3(String s1) {
     if (s1 != null) {
         String url = "https://api.weatherbit.io/v2.0/current?city=" + s1 + "&key=dd7c47eafd554c518e88d644137dae40";
         RequestQueue requestQueue = Volley.newRequestQueue(Search.this);
         StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                 new Response.Listener<String>() {
                     @RequiresApi(api = Build.VERSION_CODES.O)
                     @Override
                     public void onResponse(String response) {
                         try {
                             JSONObject jsonObject = new JSONObject(response);
                             String list = jsonObject.getString("data");
                             JSONArray arr = new JSONArray(list);
                             JSONObject get = null;
                             String[] iconUrl = new String[8];
                             String[] city1 = new String[4];


                             for (int i = 0; i < arr.length(); i++) {
                                 JSONObject jsonPart = arr.getJSONObject(i);
                                 city1[0] = jsonPart.getString("city_name");
                                 city1[1] = jsonPart.getString("temp");
                                 city1[2] = jsonPart.getString("ts");
                                 city1[3] = jsonPart.getString("timezone");

                             }
                             String[] split = city1[1].split("\\.");
                             favorCityy[2].setText(city1[0]);
                             tempCity[2].setText(split[0] + "℃");

                             long unixSeconds = Long.parseLong(city1[2]);
                             Date date = new java.util.Date(unixSeconds * 1000L);
                             SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                             sdf.setTimeZone(java.util.TimeZone.getTimeZone(ZoneId.of(city1[3])));
                             //sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
                             String formattedDate = sdf.format(date);
                             String[] time = formattedDate.split(" ");
                             time = time[1].split("\\:");
                             timeCity[2].setText(time[0] + ":" + time[1]);


                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {


                     }

                 });
         requestQueue.add(stringRequest);
     }
 }
    public void speech_TO_TEXT(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() );

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported) ,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void  tts(String text){
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int status) {
                if ( status != TextToSpeech.ERROR ){
                    textToSpeech.setLanguage( new Locale("vi_VN") );
                    textToSpeech.setSpeechRate((float) 1.5);
                    textToSpeech.speak(text , TextToSpeech.QUEUE_FLUSH , null);

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {

                    List<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    etCity.setText(text);

                }
                break;
            }

        }
    }

}