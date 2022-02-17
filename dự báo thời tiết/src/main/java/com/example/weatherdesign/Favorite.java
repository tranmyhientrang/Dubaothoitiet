package com.example.weatherdesign;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Favorite extends AppCompatActivity {


    String a;
//    public TextView gpsTime;
//    public TextView gpsCity;
//    public TextView gpsTemp;
    MainActivity main = new MainActivity();
    Search search1 = new Search();
    Search.getWeather getWeather1 = new Search.getWeather();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor);
//        gpsTime = (TextView) findViewById(R.id.gpsTime);
//        gpsCity = (TextView) findViewById(R.id.gpsCity);
//        LocalDateTime current = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//        String formatted = current.format(formatter);
//        String[] time = formatted.split(" ");
//        time = time[1].split("\\:");
//        gpsTime.setText(time[0] + ":" + time[1]);
        //gpsCity.setText(main.get1());


    }

    public void cancelButtonPressed(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }


}