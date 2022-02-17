package com.example.weatherdesign;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "dd7c47eafd554c518e88d644137dae40";
    DecimalFormat df = new DecimalFormat("#.##");

    public TextView Range;
    public int countCity = 0;
    public TextView day1;
    public TextView day2;
    public TextView day3;
    public TextView day4;
    public TextView day5;
    public TextView day6;
    public TextView day7;
    public TextView range1,range2, range3, range4, range5, range6, range7;
    public ImageView icon1, icon2, icon3, icon4, icon5, icon6, icon7;

    public TextView[] hour = new TextView[26];
    public TextView[] temp = new TextView[26];
    public ImageView[] img = new ImageView[26];
    public double latitude = 0, longtitude = 0;
    //    public TextView Locations;
    //public TextView Template;
    private LocationManager locationManager;
    private String provider;
    double lat, lng;
    String country;
    String city;

    Search search=new Search();
    Search.getWeather getWeather=new Search.getWeather();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
//        searchButton = (ImageButton) findViewById(R.id.searchButton);
//        Today = (TextView) findViewById(R.id.Today);
//        Template = (TextView) findViewById(R.id.Template);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        onResume();
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
            if(search.dem==0) {
                // w.execute("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&units=metric&appid=" + API);
                getWeather.execute("https://api.weatherbit.io/v2.0/current?lat=" + lat + "&lon=" + lng +"&key="+ appid);
//
            }
            else{
                getWeather.execute("https://api.weatherbit.io/v2.0/current?city=" + search.cityfind +"&key="+ appid);
                city = search.cityfind;

            }

        }

        //getLocation();
        List<Address> addresses;
        Geocoder test = new Geocoder(this, Locale.getDefault());
        try {
            addresses = test.getFromLocationName(city, 1);
            latitude = addresses.get(0).getLatitude();
            longtitude = addresses.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Log.i("test","SNSDDD"+ city);
        get24hours();
        Intent intent = new Intent(this, WeatherService.class);
        startService(intent);
        get7day();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    double a =0,b=0;
    @Override
    public void onLocationChanged(Location location) {
        lat = (location.getLatitude());
        lng = (location.getLongitude());


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String[] tem = address.split(",");
            country = addresses.get(0).getCountryName();
            city = addresses.get(0).getAdminArea();


        } catch (IOException e) {
            e.printStackTrace();
        }

        // Only if available else return NULL
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public void favorButtonPressed(View view) {
        startActivity(new Intent(this, Favorite.class));

    }

    public void searchButtonPressed(View view) {
        startActivity(new Intent(this, Search.class));

    }
    public void addButtonPressed(View view) {
        countCity++;
        search.count++;
    }

    public void anhxa() {
        getWeather.Locations = (TextView) findViewById(R.id.Location);
        getWeather.Template = (TextView) findViewById(R.id.Template);
        getWeather.imageView = (ImageView) findViewById(R.id.ImageView);
        getWeather.Weather = (TextView) findViewById(R.id.Weather);

        Range = (TextView) findViewById(R.id.Range);

        day1=(TextView) findViewById(R.id.day1);
        day2=(TextView) findViewById(R.id.day2);
        day3=(TextView) findViewById(R.id.day3);
        day4=(TextView) findViewById(R.id.day4);
        day5=(TextView) findViewById(R.id.day5);
        day6=(TextView) findViewById(R.id.day6);
        day7=(TextView) findViewById(R.id.day7);

        range1 = (TextView) findViewById(R.id.range1);
        range2 = (TextView) findViewById(R.id.range2);
        range3 = (TextView) findViewById(R.id.range3);
        range4 = (TextView) findViewById(R.id.range4);
        range5 = (TextView) findViewById(R.id.range5);
        range6 = (TextView) findViewById(R.id.range6);
        range7 = (TextView) findViewById(R.id.range7);

        icon1 = (ImageView) findViewById(R.id.icon1);
        icon2 = (ImageView) findViewById(R.id.icon2);
        icon3 = (ImageView) findViewById(R.id.icon3);
        icon4 = (ImageView) findViewById(R.id.icon4);
        icon5 = (ImageView) findViewById(R.id.icon5);
        icon6 = (ImageView) findViewById(R.id.icon6);
        icon7 = (ImageView) findViewById(R.id.icon7);

        getWeather.aqi = (ProgressBar) findViewById(R.id.aqi);
        getWeather.indexAir = (TextView) findViewById(R.id.indexAir);

        getWeather.des1 = (TextView) findViewById(R.id.des1);
        getWeather.des2 = (TextView) findViewById(R.id.des2);
        getWeather.des3 = (TextView) findViewById(R.id.des3);
        getWeather.des4 = (TextView) findViewById(R.id.des4);
        getWeather.des5 = (TextView) findViewById(R.id.des5);
        getWeather.des6 = (TextView) findViewById(R.id.des6);
        getWeather.des7 = (TextView) findViewById(R.id.des7);
        getWeather.des8 = (TextView) findViewById(R.id.des8);
        getWeather.des9 = (TextView) findViewById(R.id.des9);

        temp[1] = (TextView) findViewById(R.id.temp1);
        temp[2] = (TextView) findViewById(R.id.temp2);
        temp[3] = (TextView) findViewById(R.id.temp3);
        temp[4] = (TextView) findViewById(R.id.temp4);
        temp[5] = (TextView) findViewById(R.id.temp5);
        temp[6] = (TextView) findViewById(R.id.temp6);
        temp[7] = (TextView) findViewById(R.id.temp7);
        temp[8] = (TextView) findViewById(R.id.temp8);
        temp[9] = (TextView) findViewById(R.id.temp9);
        temp[10] = (TextView) findViewById(R.id.temp10);
        temp[11] = (TextView) findViewById(R.id.temp11);
        temp[12] = (TextView) findViewById(R.id.temp12);
        temp[13] = (TextView) findViewById(R.id.temp13);
        temp[14] = (TextView) findViewById(R.id.temp14);
        temp[15] = (TextView) findViewById(R.id.temp15);
        temp[16] = (TextView) findViewById(R.id.temp16);
        temp[17] = (TextView) findViewById(R.id.temp17);
        temp[18] = (TextView) findViewById(R.id.temp18);
        temp[19] = (TextView) findViewById(R.id.temp19);
        temp[20] = (TextView) findViewById(R.id.temp20);
        temp[21] = (TextView) findViewById(R.id.temp21);
        temp[22] = (TextView) findViewById(R.id.temp22);
        temp[23] = (TextView) findViewById(R.id.temp23);
        temp[24] = (TextView) findViewById(R.id.temp24);
        temp[25] = (TextView) findViewById(R.id.temp25);

        hour[1] = (TextView) findViewById(R.id.hour1);
        hour[2] = (TextView) findViewById(R.id.hour2);
        hour[3] = (TextView) findViewById(R.id.hour3);
        hour[4] = (TextView) findViewById(R.id.hour4);
        hour[5] = (TextView) findViewById(R.id.hour5);
        hour[6] = (TextView) findViewById(R.id.hour6);
        hour[7] = (TextView) findViewById(R.id.hour7);
        hour[8] = (TextView) findViewById(R.id.hour8);
        hour[9] = (TextView) findViewById(R.id.hour9);
        hour[10] = (TextView) findViewById(R.id.hour10);
        hour[11] = (TextView) findViewById(R.id.hour11);
        hour[12] = (TextView) findViewById(R.id.hour12);
        hour[13] = (TextView) findViewById(R.id.hour13);
        hour[14] = (TextView) findViewById(R.id.hour14);
        hour[15] = (TextView) findViewById(R.id.hour15);
        hour[16] = (TextView) findViewById(R.id.hour16);
        hour[17] = (TextView) findViewById(R.id.hour17);
        hour[18] = (TextView) findViewById(R.id.hour18);
        hour[19] = (TextView) findViewById(R.id.hour19);
        hour[20] = (TextView) findViewById(R.id.hour20);
        hour[21] = (TextView) findViewById(R.id.hour21);
        hour[22] = (TextView) findViewById(R.id.hour22);
        hour[23] = (TextView) findViewById(R.id.hour23);
        hour[24] = (TextView) findViewById(R.id.hour24);
        hour[25] = (TextView) findViewById(R.id.hour25);

        img[1] = (ImageView) findViewById(R.id.img1);
        img[2] = (ImageView) findViewById(R.id.img2);
        img[3] = (ImageView) findViewById(R.id.img3);
        img[4] = (ImageView) findViewById(R.id.img4);
        img[5] = (ImageView) findViewById(R.id.img5);
        img[6] = (ImageView) findViewById(R.id.img6);
        img[7] = (ImageView) findViewById(R.id.img7);
        img[8] = (ImageView) findViewById(R.id.img8);
        img[9] = (ImageView) findViewById(R.id.img9);
        img[10] = (ImageView) findViewById(R.id.img10);
        img[11] = (ImageView) findViewById(R.id.img11);
        img[12] = (ImageView) findViewById(R.id.img12);
        img[13] = (ImageView) findViewById(R.id.img13);
        img[14] = (ImageView) findViewById(R.id.img14);
        img[15] = (ImageView) findViewById(R.id.img15);
        img[16] = (ImageView) findViewById(R.id.img16);
        img[17] = (ImageView) findViewById(R.id.img17);
        img[18] = (ImageView) findViewById(R.id.img18);
        img[19] = (ImageView) findViewById(R.id.img19);
        img[20] = (ImageView) findViewById(R.id.img20);
        img[21] = (ImageView) findViewById(R.id.img21);
        img[22] = (ImageView) findViewById(R.id.img22);
        img[23] = (ImageView) findViewById(R.id.img23);
        img[24] = (ImageView) findViewById(R.id.img24);
        img[25] = (ImageView) findViewById(R.id.img25);





    }

    public void get7day(){
        String url = "https://api.weatherbit.io/v2.0/forecast/daily?city=" + city + "&key=dd7c47eafd554c518e88d644137dae40";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String jsonObjectcity = jsonObject.getString("city_name");
                            String list = jsonObject.getString("data");
                            JSONArray arr = new JSONArray(list);
                            JSONObject x=null;
                            String status="";
                            String[] ngay=new String[8];
                            String[] maxtemp = new String[8];
                            String[] mintemp = new String[8];
                            String[] icon = new String[8];
                            JSONObject get=null;
                            String[] iconUrl = new String[8];

                            for( int i=0 ; i<8;i++){
                                JSONObject jsonPart = arr.getJSONObject(i);
                                maxtemp[i]=jsonPart.getString("max_temp");
                                String[] split = maxtemp[i].split("\\.",0);
                                maxtemp[i] = split[0];
                                mintemp[i]=jsonPart.getString("min_temp");
                                split = mintemp[i].split("\\.",0);
                                mintemp[i] = split[0];
                                ngay[i] = jsonPart.getString("datetime");
                                ngay[i] = ngay[i].substring(8);
                                get = jsonPart.getJSONObject("weather");
                                icon[i] = get.getString("icon");
                                iconUrl[i]="https://www.weatherbit.io/static/img/icons/" + icon[i] + ".png";

                            }
                            Range.setText("H:" + maxtemp[0] + "℃  " + "L:" + mintemp[0] + "℃");

                            String[] dayWeek = new String[8];
                            LocalDate date = LocalDate.now();
                            for (int i = 0; i < 8; i++) {
                                DayOfWeek day = date.getDayOfWeek();
                                dayWeek[i] = day.getDisplayName(TextStyle.FULL, Locale.getDefault());
                                date = date.plusDays(1);

                            }

                            day1.setText(dayWeek[1] + ", " + ngay[1]);
                            day2.setText(dayWeek[2] + ", " + ngay[2]);
                            day3.setText(dayWeek[3] + ", " + ngay[3]);
                            day4.setText(dayWeek[4] + ", " + ngay[4]);
                            day5.setText(dayWeek[5] + ", " + ngay[5]);
                            day6.setText(dayWeek[6] + ", " + ngay[6]);
                            day7.setText(dayWeek[7] + ", " + ngay[7]);

                            range1.setText(maxtemp[1] + "℃\t\t\t " + mintemp[1] + "℃");
                            range2.setText(maxtemp[2] + "℃\t\t\t " + mintemp[2] + "℃");
                            range3.setText(maxtemp[3] + "℃\t\t\t " + mintemp[3] + "℃");
                            range4.setText(maxtemp[4] + "℃\t\t\t " + mintemp[4] + "℃");
                            range5.setText(maxtemp[5] + "℃\t\t\t " + mintemp[5] + "℃");
                            range6.setText(maxtemp[6] + "℃\t\t\t " + mintemp[6] + "℃");
                            range7.setText(maxtemp[7] + "℃\t\t\t " + mintemp[7] + "℃");

                            Picasso.with(icon1.getContext()).load(iconUrl[1]).into(icon1);
                            Picasso.with(icon2.getContext()).load(iconUrl[2]).into(icon2);
                            Picasso.with(icon3.getContext()).load(iconUrl[3]).into(icon3);
                            Picasso.with(icon4.getContext()).load(iconUrl[4]).into(icon4);
                            Picasso.with(icon5.getContext()).load(iconUrl[5]).into(icon5);
                            Picasso.with(icon6.getContext()).load(iconUrl[6]).into(icon6);
                            Picasso.with(icon7.getContext()).load(iconUrl[7]).into(icon7);






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

    public void get24hours(){

        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude + "&lon=" + longtitude +"&exclude=daily&appid=e00a536f2c682e8a53dfb57f774569ca";
        Log.d("111111111111","OANH"+latitude);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String timezone = jsonObject.getString("timezone");

                            String hourly = jsonObject.getString("hourly");
                            JSONArray arr = new JSONArray(hourly);
                            String[] tempF = new String[25];
                            double[] tempC = new double[25];
                            String[] image = new String[26];
                            JSONObject get=null;
                            String[] imgUrl = new String[26];
                            for( int i=0 ; i<25;i++){
                                JSONObject jsonPart = arr.getJSONObject(i);
                                tempF[i] = jsonPart.getString("temp");
                                tempC[i] = Double.parseDouble(tempF[i]) - 273.15;
                                String[] split = Double.toString(tempC[i]).split("\\.");
                                tempF[i] = split[0];
                                temp[i+1].setText(tempF[i]);

                                String str = jsonPart.getString("dt");
                                long unixSeconds = Long.parseLong(str);
                                Date date = new java.util.Date(unixSeconds*1000L);
                                SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                                sdf.setTimeZone(java.util.TimeZone.getTimeZone(ZoneId.of(timezone)));
                                //sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+7"));
                                String formattedDate = sdf.format(date);
                                String[] time = formattedDate.split(" ");
                                time = time[1].split("\\:");
                                hour[i+1].setText(time[0]);
                                hour[1].setText("Now");


                                try {
                                    String weather = jsonPart.getString("weather");
                                    JSONArray array = new JSONArray(weather);
                                    Log.i("test", "+++++======" + weather);
                                    for( int j=0 ; j < array.length();j++) {
                                        JSONObject jsonPart1 = array.getJSONObject(j);
                                        image[i + 1] = jsonPart1.getString("icon");
                                        imgUrl[i + 1] = "https://openweathermap.org/img/w/" + image[i + 1] + ".png";
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            Picasso.with(img[1].getContext()).load(imgUrl[1]).into(img[1]);
                            Picasso.with(img[2].getContext()).load(imgUrl[2]).into(img[2]);
                            Picasso.with(img[3].getContext()).load(imgUrl[3]).into(img[3]);
                            Picasso.with(img[4].getContext()).load(imgUrl[4]).into(img[4]);
                            Picasso.with(img[5].getContext()).load(imgUrl[5]).into(img[5]);
                            Picasso.with(img[6].getContext()).load(imgUrl[6]).into(img[6]);
                            Picasso.with(img[7].getContext()).load(imgUrl[7]).into(img[7]);
                            Picasso.with(img[8].getContext()).load(imgUrl[8]).into(img[8]);
                            Picasso.with(img[9].getContext()).load(imgUrl[9]).into(img[9]);
                            Picasso.with(img[10].getContext()).load(imgUrl[10]).into(img[10]);
                            Picasso.with(img[11].getContext()).load(imgUrl[11]).into(img[11]);
                            Picasso.with(img[12].getContext()).load(imgUrl[12]).into(img[12]);
                            Picasso.with(img[13].getContext()).load(imgUrl[13]).into(img[13]);
                            Picasso.with(img[14].getContext()).load(imgUrl[14]).into(img[14]);
                            Picasso.with(img[15].getContext()).load(imgUrl[15]).into(img[15]);
                            Picasso.with(img[16].getContext()).load(imgUrl[16]).into(img[16]);
                            Picasso.with(img[17].getContext()).load(imgUrl[17]).into(img[17]);
                            Picasso.with(img[18].getContext()).load(imgUrl[18]).into(img[18]);
                            Picasso.with(img[19].getContext()).load(imgUrl[19]).into(img[19]);
                            Picasso.with(img[20].getContext()).load(imgUrl[20]).into(img[20]);
                            Picasso.with(img[21].getContext()).load(imgUrl[21]).into(img[21]);
                            Picasso.with(img[22].getContext()).load(imgUrl[22]).into(img[22]);
                            Picasso.with(img[23].getContext()).load(imgUrl[23]).into(img[23]);
                            Picasso.with(img[24].getContext()).load(imgUrl[24]).into(img[24]);
                            Picasso.with(img[25].getContext()).load(imgUrl[25]).into(img[25]);






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

    public String get1() {
        onResume();
        String a = city;
        return a;
    }

}


