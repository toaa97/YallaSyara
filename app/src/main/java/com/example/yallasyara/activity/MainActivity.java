package com.example.yallasyara.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yallasyara.R;
import com.example.yallasyara.helper.SQLiteHandler;
import com.example.yallasyara.helper.SessionManager;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends MapsActivity implements LocationListener {

    private Button btnLogout;
//    private Button currentLocation;

    private SQLiteHandler db;
    private SessionManager session;

    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private static final String URL_PRODUCTS = "http://192.168.1.115/android_login_api/cars.php";

    //a list to store all the products
    List<Car> carList;

    //the recyclerview
    RecyclerView recyclerView;
    carsAdapter adapter;
    public static double cLat;
    public static double cLng;
    private static int MY_LOCATION_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;}

        btnLogout = (Button) findViewById(R.id.btnLogout);
  //      currentLocation=(Button) findViewById(R.id.current_location);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
 //       currentLocation.setOnClickListener(new View.OnClickListener() {

 //           @Override
 //           public void onClick(View v) {
 //               Intent i = new Intent(getApplicationContext(),
 //                       MapsActivity.class);
 //               startActivity(i);
 //               finish();
 //           }
 //       });
        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //initializing the productlist
        carList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadCars();




    }
    private void loadCars() {/*
     * Creating a String Request
     * The request type is GET defined by first parameter
     * The URL is defined in the second parameter
     * Then we have a Response Listener and a Error Listener
     * In response listener we will get the JSON response as a String
     * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject car = array.getJSONObject(i);

                                //adding the product to product list
                                carList.add(new Car(
                                        car.getInt("id"),
                                        car.getString("model_name"),
                                        car.getInt("production_year"),
                                        car.getDouble("latitude"),
                                        car.getDouble("longitude"),
                                        car.getString("image_path"),
                                        car.getInt("fuel_level")
                                ));
                            }
                            Collections.sort(carList);
                            Collections.reverse(carList);
                            //creating adapter object and setting it to recyclerview
                            adapter = new carsAdapter(MainActivity.this, carList);
                            recyclerView.setAdapter(adapter);
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

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //current
        cLat=location.getLatitude();
        cLng=location.getLongitude();

    }
}
