package com.example.yallasyara.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yallasyara.R;
import com.example.yallasyara.helper.SQLiteHandler;
import com.example.yallasyara.helper.SessionManager;

import java.util.HashMap;

public class MainActivity extends Activity {
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private TextView txtAge;
    private TextView txtNationality;
    private TextView txtPhone;
    private Button currentLocation;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
      //  txtAge = (TextView) findViewById(R.id.age);
       // txtNationality = (TextView) findViewById(R.id.nationality);
       // txtPhone = (TextView) findViewById(R.id.phone_number);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        currentLocation=(Button) findViewById(R.id.current_location);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String > user = db.getUserDetails();


        String name = user.get("name");
        String email = user.get("email");
       // String age = user.get("age");
       // String nationality = user.get("nationality");
      // String phone_number = user.get("phone_number");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);
       // txtAge.setText(age);
       // txtNationality.setText(nationality);
       // txtPhone.setText(phone_number);

        currentLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        MapsActivity.class);
                startActivity(i);
                finish();
            }
        });
        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
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
}
