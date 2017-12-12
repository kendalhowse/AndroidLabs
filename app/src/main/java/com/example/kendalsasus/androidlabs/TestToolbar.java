package com.example.kendalsasus.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import junit.framework.Test;

public class TestToolbar extends AppCompatActivity {
    String message = "You selected item 1";
    //EditText newMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.action_one:
                Snackbar.make(findViewById(R.id.action_one), message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.d("Toolbar", "Option 1 selected");
                break;
            case R.id.action_two:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Do you want to go back?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                Log.d("Toolbar", "Option 2 selected");
                break;
            case R.id.action_three:
                AlertDialog.Builder custom = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
               // custom.setView(inflater.inflate(R.layout.dialog_box, null));
                LinearLayout rootTag = (LinearLayout)inflater.inflate(R.layout.dialog_box, null);
                final EditText et = (EditText)rootTag.findViewById(R.id.snackText);
                //newMessage =  new EditText(this);
                //newMessage = findViewById(R.id.snackText);
               // custom.setView(newMessage);
                custom.setView(rootTag);
                custom.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        message = et.getText().toString();
                    }
                });
                custom.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing
                    }
                });
                AlertDialog alert = custom.create();
                alert.show();
                Log.d("Toolbar", "Option 3 selected");
                break;
            case R.id.action_four:
                Toast toast = Toast.makeText(this, "Version 1.0, by Kendal Howse", Toast.LENGTH_SHORT);
                toast.show();
                Log.d("Toolbar", "About");
                break;

        }

        return true;
    }

}
