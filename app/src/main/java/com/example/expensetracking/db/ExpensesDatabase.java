package com.example.expensetracking.db;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_FAIL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.io.IOException;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

public class ExpensesDatabase {
    private final SupportSQLiteOpenHelper helper;
    private SupportSQLiteDatabase db;

    //constructor
    public ExpensesDatabase(Context ctx) {
        SupportSQLiteOpenHelper.Factory factory = new FrameworkSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper.Configuration configuration = SupportSQLiteOpenHelper.Configuration.builder(ctx)
                .name(mySQLiteHelper.DATABASE_NAME)
                .callback(new mySQLiteHelper())
                .build();
        helper = factory.create(configuration);

    }

    //opens the database
    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    //returns true if db is open.
    public boolean isOpen() throws SQLException {
        return db.isOpen();
    }

    //closes the database
    public void close() {
        try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long insertValues(String name, String category, String date, Double amount, String note) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(mySQLiteHelper.KEY_NAME, name);
        initialValues.put(mySQLiteHelper.KEY_CATEGORY, category);
        initialValues.put(mySQLiteHelper.KEY_DATE, date);
        initialValues.put(mySQLiteHelper.KEY_AMOUNT, amount);
        initialValues.put(mySQLiteHelper.KEY_NOTE, note);
        return db.insert(mySQLiteHelper.TABLE_NAME, CONFLICT_FAIL, initialValues);
    }

    //get all the rows.
    public Cursor getAllNames() {
        Cursor mCursor = qbQuery(mySQLiteHelper.TABLE_NAME,   //table name
                new String[]{mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_CATEGORY, mySQLiteHelper.KEY_DATE, mySQLiteHelper.KEY_AMOUNT, mySQLiteHelper.KEY_NOTE},  //projection, ie columns.
                null,  //selection,  we want everything.
                null, // String[] selectionArgs,  again, we want everything.
                mySQLiteHelper.KEY_ROWID// String sortOrder  by name as the sort.
        );
        if (mCursor != null)  //make sure cursor is not empty!
            mCursor.moveToFirst();
        return mCursor;
    }

    //Retrieve one entry
    public Cursor get1ID(int ID) throws SQLException {
        //the query parameter method is not included in supportSQLiteDatabase.
        //So, use the helper function
        Cursor mCursor = qbQuery(mySQLiteHelper.TABLE_NAME,   //table name
                new String[]{mySQLiteHelper.KEY_NAME, mySQLiteHelper.KEY_CATEGORY, mySQLiteHelper.KEY_DATE, mySQLiteHelper.KEY_AMOUNT, mySQLiteHelper.KEY_NOTE},  //projection, ie columns.
                mySQLiteHelper.KEY_ROWID + "=\'" + ID + "\'",  //selection,
                null, // String[] selectionArgs,  not necessary here.
                mySQLiteHelper.KEY_ROWID// String sortOrder  by name as the sort.
        );

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor qbQuery(String TableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SupportSQLiteQueryBuilder qb = SupportSQLiteQueryBuilder.builder(TableName);
        qb.columns(projection);
        qb.selection(selection, selectionArgs);
        qb.orderBy(sortOrder);
        //using the query builder to manage the actual query at this point.
        return db.query(qb.create());
    }

    // this is a generic method to update something from the database, uses the Convenience method.
    public int Update(String TableName, ContentValues values, String selection, String[] selectionArgs) {
        return db.update(TableName, CONFLICT_FAIL, values, selection, selectionArgs);
    }

    //delete method
    public int Delete(String TableName, String selection, String[] selectionArgs) {
        return db.delete(TableName, selection, selectionArgs);
    }

    //remove all entries from the CurrentBoard
    public void emptydb() {
        db.delete(mySQLiteHelper.TABLE_NAME, null, null);
    }
}
