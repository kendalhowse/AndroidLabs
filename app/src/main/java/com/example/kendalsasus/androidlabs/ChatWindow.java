package com.example.kendalsasus.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    ListView listView = null;
    EditText editText = null;
    Button send = null;
    final ArrayList<String> messages = new ArrayList<>();
    private String ACTIVITY_NAME = "ChatWindow";
    ChatDatabaseHelper cdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listView = findViewById(R.id.messageArea);
        editText = findViewById(R.id.text);
        send = findViewById(R.id.send);

        cdb = new ChatDatabaseHelper(this);
        final SQLiteDatabase db = cdb.getWritableDatabase();

        Cursor results = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);


        int numResults = results.getCount();
        String returnedMessage;

        results.moveToFirst();

        while(!results.isAfterLast() ) {

            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            messages.add(results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            results.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor's total column count = " + results.getColumnCount());

        for(int i = 0; i < numResults; i++) {
            Log.i(ACTIVITY_NAME, "Cursor's column count= " + results.getColumnIndex(results.getColumnName(i)));
            Log.i(ACTIVITY_NAME, "Cursor's column name= " + results.getColumnName(i));

        }



        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newMessage = editText.getText().toString();
                editText.setText("");
                messages.add(newMessage);
                messageAdapter.notifyDataSetChanged();
                ContentValues newData = new ContentValues();
                newData.put(ChatDatabaseHelper.KEY_MESSAGE, newMessage);

                db.insert(ChatDatabaseHelper.TABLE_NAME, "", newData);

            }
        });





    }
    protected void onDestroy(){
        super.onDestroy();
        cdb.close();
    }



    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }
        public int getCount() {
            return messages.size();
        }

        public String getItem(int position){
            return messages.get(position);
        }


        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();



            View result;
            if(position%2 == 0){
                result = inflater.inflate(R.layout.chat_row_incoming, null);

            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }


    }




}
