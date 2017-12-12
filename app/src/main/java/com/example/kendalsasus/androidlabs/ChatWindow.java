package com.example.kendalsasus.androidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    Cursor results;
    SQLiteDatabase db;
    ChatAdapter messageAdapter;
    static MessageFragment messageFragment;

    public ChatWindow(){

    }

    public ChatWindow(ChatDatabaseHelper cdb, SQLiteDatabase db, ChatAdapter chatAdapter, ListView listView){
        this.cdb = cdb;
        this.db = db;
        messageAdapter = chatAdapter;
        this.listView = listView;


        /*cdb = new ChatDatabaseHelper(this);
        db = cdb.getWritableDatabase();*/

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        final boolean checkScreen = findViewById(R.id.frameLayout) != null;

        if(checkScreen){
            Log.i("check", "Tablet");
        }
        else {
            Log.i("Check", "Phone");
        }

        listView = findViewById(R.id.messageArea);
        editText = findViewById(R.id.text);
        send = findViewById(R.id.send);

 /*       cdb = new ChatDatabaseHelper(this);
        db = cdb.getWritableDatabase();*/

        setCdb(new ChatDatabaseHelper(this));
        setDb(cdb.getWritableDatabase());

        results = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);


        results.moveToFirst();

        while(!results.isAfterLast() ) {

            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            messages.add(results.getString(results.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            results.moveToNext();
        }

        messageAdapter = new ChatAdapter(this);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int position, long id){
                    //if its a tablet
                    //set back to the boolean value

                Bundle args = new Bundle();
                Cursor cursor =  db.rawQuery("SELECT " + ChatDatabaseHelper.KEY_MESSAGE + " FROM " + ChatDatabaseHelper.TABLE_NAME + " WHERE " + ChatDatabaseHelper.KEY_ID + " is " + "'" +  id + "'", null);


                cursor.moveToFirst();
                String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                args.putLong("ID", id);
                args.putString("MESSAGE", message);
                    if(checkScreen){


                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        messageFragment = new MessageFragment(new ChatWindow(cdb, db, messageAdapter, listView));
                        messageFragment.setArguments(args);
                        ft.replace(R.id.frameLayout, messageFragment);
                        ft.addToBackStack("");
                        ft.commit();

                    }

                    //its a phone
                    //copy wrd for word from his lab
                    //
                    else {
                        Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                        intent.putExtras(args);
                        startActivityForResult(intent, 10);
                    }
            }
        });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int deleteMe;
        String deleteMessage;

        if(requestCode == 10 && resultCode == 10){
           Bundle extras = data.getExtras();


            deleteMe = (int) extras.getLong("deleteThis");
            deleteMessage = extras.getString("Message");


            db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID +"=?", new String[]{Integer.toString(deleteMe)});
            messages.remove(deleteMessage);

            messageAdapter.notifyDataSetChanged();

        }
    }
    protected void onDestroy(){
        super.onDestroy();
        cdb.close();
    }



    public class ChatAdapter extends ArrayAdapter<String> {

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

        public long getItemId(int position){

                results = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);


                    results.moveToPosition(position);

                    return results.getLong(results.getColumnIndex(ChatDatabaseHelper.KEY_ID));




        }

    }


    public void deleteID(long id, String deleteMessage, SQLiteDatabase db, ChatAdapter messageAdapter, ListView listView) {

        this.db = db;
        this.listView = listView;
        this.messageAdapter = messageAdapter;
        this.listView = listView;

        db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID +"=?", new String[]{Long.toString(id)});
        messages.remove(deleteMessage);


    }

    public void update(long id){
        messages.clear();
               Cursor cursor = db.rawQuery("SELECT " + ChatDatabaseHelper.KEY_MESSAGE + " FROM " + ChatDatabaseHelper.TABLE_NAME + " WHERE " + ChatDatabaseHelper.KEY_ID + " is " + "'" +  id + "'", null);

        while(!cursor.isAfterLast() ) {

           // Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            messages.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            results.moveToNext();
        }


        messageAdapter.notifyDataSetChanged();
    }

    public ChatDatabaseHelper getCdb(){
        return cdb;
    }

    public void setCdb(ChatDatabaseHelper cdb){
        this.cdb = cdb;

    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public void setDb(SQLiteDatabase db){
        this.db = db;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public ChatAdapter getMessageAdapter() {
        return messageAdapter;
    }

    public ListView getListView() {
        return listView;
    }
}
