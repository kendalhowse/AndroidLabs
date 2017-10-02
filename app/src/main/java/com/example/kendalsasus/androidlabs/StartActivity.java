package com.example.kendalsasus.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {
    protected static final String ACTIVITY_NAME = "StartActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        setContentView(R.layout.activity_start);

       Button button = findViewById(R.id.button);
       Button startChat = findViewById(R.id.startChat);

       startChat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.i(ACTIVITY_NAME, "User clicked Start Chat");
               Intent chatIntent = new Intent(StartActivity.this, ChatWindow.class);
               startActivityForResult(chatIntent, 10);
           }
       });

       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 10);


            }



        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
                Toast.makeText(getApplicationContext(), "ListItemsActivity responded " + data.getStringExtra("Response"), Toast.LENGTH_LONG).show();
            }
        }
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