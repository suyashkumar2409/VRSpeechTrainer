package com.example.suyashkumar.vrspeechtrainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SUYASH KUMAR on 1/17/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "vrspeechtrainer";
    private static final int DB_VERSION = 1;

    public static final String word_table = "WORDTABLE";
    public static final String word_table_word = "WORD";

    public DatabaseHelper(Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        runFun(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        runFun(db, oldVersion, newVersion);
    }

    public void runFun(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 1)
        {
            db.execSQL("CREATE TABLE "+word_table + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "WORD TEXT);"
            );

            insertWord(db, "Hello");
            insertWord(db, "Tourist");
            insertWord(db, "Champagne");
        }
    }

    public void insertWord(SQLiteDatabase db, String word)
    {
        ContentValues values = new ContentValues();
        values.put("WORD", word);

        db.insert(word_table, null, values);
    }
}
