package com.example.kendalsasus.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Kendal's Asus on 2017-10-11.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME;
    public static int VERSION_NUM = 1;
    public static String TABLE_NAME = "labTable";
    public final static String KEY_ID = "_id";
    public final static String KEY_MESSAGE = "MESSAGE";

    public ChatDatabaseHelper(Context ctx){

        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_NAME + "( "+ KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE+" varchar);";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP IF TABLE EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
    @Override
    public void onOpen(SQLiteDatabase db)
    {
        Log.i("Database ", "onOpen was called");
    }
}


