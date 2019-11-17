package com.example.yallasyara.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yallasyara.R;
import com.example.yallasyara.app.AppConfig;
import com.example.yallasyara.app.AppController;
import com.example.yallasyara.helper.SQLiteHandler;
import com.example.yallasyara.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private Button Map;
    Location mLastLocation;
    private Button Send;
    TextView ModelName, Year, FuelLevel, Distance;
    private double Latitude;
    private double Longitude;
    EditText review;
    TextView allreviews;

    private SessionManager session;
    private SQLiteHandler db;
    private String r = "";
    private int current_car_id;
    private String car_name, user_id;
    String all_reviews_php = "";
    double clat;
    double clng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Map = (Button) findViewById(R.id.map_button);
        Send = (Button) findViewById(R.id.send_button);
        ModelName = findViewById(R.id.Model_Name);
        Year = findViewById(R.id.Year);
       // Distance = findViewById(R.id.Distance);
       // FuelLevel = findViewById(R.id.Fuel_Level);
        review = findViewById(R.id.review);
        allreviews = findViewById(R.id.reviews);


        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        Intent receiveIntent = this.getIntent();
        current_car_id = receiveIntent.getIntExtra("car_id", 0);
        user_id = receiveIntent.getStringExtra("user_id");
        ModelName.setText(receiveIntent.getStringExtra("Model_Name"));
        car_name = receiveIntent.getStringExtra("Model_Name");
        Latitude = receiveIntent.getDoubleExtra("Latitude", 0.0);
        Longitude = receiveIntent.getDoubleExtra("Longitude", 0.0);
        Year.setText(String.valueOf(receiveIntent.getIntExtra("Year", 0)));
        //Distance.setText(String.valueOf(receiveIntent.getDoubleExtra("Distance", 0.0)));
        //FuelLevel.setText(String.valueOf(receiveIntent.getIntExtra("Fuel_Level", 0)));
        clat=receiveIntent.getDoubleExtra("clat",0.0);
        Log.d("clatttt",String.valueOf(clat));
        clng=receiveIntent.getDoubleExtra("clng",0.0);
        getReviews();




        Map.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Intent i = new Intent(getApplicationContext(),
                       // MapsActivity.class);
               // startActivity(i);
                //i.putExtra("Latitude", Latitude);
                //i.putExtra("Longitude", Longitude);
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+clat+","+clng+"&daddr="+Latitude+","+Longitude;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "Select an application"));
                finish();
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String storeReview = review.getText().toString().trim();
                Log.d("rev", storeReview);
                if (!storeReview.isEmpty()) {
                    storeReviews(current_car_id,user_id,car_name,storeReview);
                } else {

                }

            }
        });

    }




    private void getReviews() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_REVIEWS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            all_reviews_php = "";


                            //traversing through all the object
                            for ( int i = 0; i < array.length(); i++ ) {
                                //getting product object from json array
                                JSONObject rev = array.getJSONObject(i);
                                String car_id = rev.getString("car_id");
                                // Log.d("user_id",id);
                                String review = rev.getString("review");
                                if (car_id.equals(String.valueOf(current_car_id))) {
                                    all_reviews_php = all_reviews_php + "\n" + "User:" + i + " " + review;
                                    Log.d("rev", all_reviews_php);

                                }

                            }
                            allreviews.setText(all_reviews_php);


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


    private void storeReviews(final int current_car_id, final String user_id, final String car_name, final String review) {
        String tag_string_req = "req_review";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REVIEWS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("response from url",response);
//                try {
//                    JSONObject jObj = new JSONObject(response);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volly", "Registration Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("car_id", String.valueOf(current_car_id));
                params.put("user_id", user_id);
                params.put("car_name", car_name);
                params.put("review", review);


                return params;
            }

        };

        // Adding request to request queue
       AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}


