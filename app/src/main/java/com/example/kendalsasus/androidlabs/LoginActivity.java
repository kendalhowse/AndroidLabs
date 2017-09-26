package com.example.kendalsasus.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;
import java.util.Set;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        setContentView(R.layout.activity_login);
         Button loginButton = (Button) findViewById(R.id.loginButton);

        final SharedPreferences sharedPref = getSharedPreferences("DefaultEmail", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.editText1);
                editor.putString("Default Email", email.getText() + "");
                editor.commit();
                String fromFile = sharedPref.getString("Default Email", "email@domain.com");
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

    }


    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
