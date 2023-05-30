package com.example.expensetracking.db;

import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class mySQLiteHelper extends SupportSQLiteOpenHelper.Callback {

    private static final String TAG = "mySQLiteHelper";

    //column names for the table.
    public static final String KEY_NAME = "Name";
    public static final String KEY_CATEGORY = "Category";
    public static final String KEY_DATE = "Date";
    public static final String KEY_AMOUNT = "Amount";
    public static final String KEY_NOTE = "Note";
    public static final String KEY_ROWID = "_id";   //required field for the cursorAdapter

    public static final String DATABASE_NAME = "ExpensesList.db";
    public static final String TABLE_NAME = "myExpenses";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +  //this line is required for the cursorAdapter.
                    KEY_NAME + " TEXT, " +
                    KEY_CATEGORY + " TEXT, " +
                    KEY_DATE + " TEXT, " +
                    KEY_AMOUNT + " INTEGER, " +
                    KEY_NOTE+ " TEXT);";

    //required constructor with the super.
    public mySQLiteHelper() {
        super(DATABASE_VERSION);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
        //NOTE only called when the database is initial created!
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
        //Called when the database version changes, Remember the constant from above.
        Log.w(TAG, "Upgrading database from version " + oldVersion
                + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}