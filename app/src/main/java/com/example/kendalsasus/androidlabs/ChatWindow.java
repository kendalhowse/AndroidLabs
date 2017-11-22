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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    ListView listView = null;
    EditText editText = null;
    Button send = null;
    final ArrayList<String> messages = new ArrayList<>();
    private String ACTIVITY_NAME = "ChatWindow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listView = findViewById(R.id.messageArea);
        editText = findViewById(R.id.text);
        send = findViewById(R.id.send);

        ChatDatabaseHelper cdb = new ChatDatabaseHelper(this);
        final SQLiteDatabase db = cdb.getWritableDatabase();

        Cursor results = db.rawQuery("SELECT * FROM labTable", null);

        int numResults = results.getCount();
        int numColumns = results.getColumnCount();

        int messageIndex = results.getColumnIndex("MESSAGE");

        String returnedMessage;
        int arr [] = new int []{R.id.message_layout};
        //SimpleCursorAdapter adptr = new SimpleCursorAdapter(this, R.layout.activity_chat_window, results, new String[] {"MESSAGE"},arr , 0);




        while(!results.isAfterLast() ) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + results.getColumnCount() );
            messages.add(results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            results.moveToNext();
        }

        results.moveToFirst();
        for(int i = 0; i < numResults; i++) {
            returnedMessage = results.getString(results.getColumnIndex("MESSAGE"));
            Log.i("Results:", returnedMessage);
            results.moveToNext();
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
                newData.put("MESSAGE", newMessage);

                db.insert("labTable", "", newData);

                Cursor results = db.rawQuery("SELECT * FROM labTable", null);

                int numResults = results.getCount();
            }
        });

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
