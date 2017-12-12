package com.example.kendalsasus.androidlabs;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MessageDetails extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle info = getIntent().getExtras();

        FragmentTransaction ft =  getFragmentManager().beginTransaction();
        MessageFragment mf = new MessageFragment();
        mf.setArguments(info);
        ft.add(R.id.frame, mf );

        ft.commit();



    }


}
