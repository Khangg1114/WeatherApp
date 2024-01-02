package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // weather url to get JSON
    private String weather_url1 = "";

    // api id for url
    private String api_id1 = "f4dc045c65f64908b5f225b9ff33707e";

    private TextView textView;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link the textView in which the temperature will be displayed
        textView = findViewById(R.id.textView);

        // create an instance of the Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.e("lat", weather_url1);

        // on clicking this button function to get the coordinates will be called
        findViewById(R.id.btVar1).setOnClickListener(view -> {
            Log.e("lat", "onClick");
            // function to find the coordinates of the last location
            obtainLocation();
        });
    }

    @SuppressLint("MissingPermission")
    private void obtainLocation() {
        Log.e("lat", "function");
        // get the last location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new com.google.android.gms.tasks.OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // get the latitude and longitude and create the http URL
                            weather_url1 = "https://api.weatherbit.io/v2.0/current?" +
                                    "lat=" + location.getLatitude() +
                                    "&lon=" + location.getLongitude() +
                                    "&key=" + api_id1;
                            Log.e("lat", weather_url1);
                            // this function will fetch data from URL
                            getTemp();
                        } else {
                            Log.e("lat", "Location is null");
                            // Handle the case where location is null
                        }
                    }
                });
    }

    private void getTemp() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = weather_url1;
        Log.e("lat", url);

        // Request a string response from the provided URL.
        StringRequest stringReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("lat", response);
                        try {
                            // get the JSON object
                            JSONObject obj = new JSONObject(response);

                            // get the Array from obj of name - "data"
                            JSONArray arr = obj.getJSONArray("data");
                            Log.e("lat obj1", arr.toString());

                            // get the JSON object from the array at index position 0
                            JSONObject obj2 = arr.getJSONObject(0);
                            Log.e("lat obj2", obj2.toString());

                            // set the temperature and the city name using getString() function
                            textView.setText(obj2.getString("temp") + " deg Celsius in " + obj2.getString("city_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("That didn't work!");
                    }
                });
        queue.add(stringReq);
    }
}