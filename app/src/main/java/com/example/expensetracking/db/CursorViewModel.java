package com.example.expensetracking.db;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracking.db.ExpensesDatabase;

public class CursorViewModel extends AndroidViewModel {

    ExpensesDatabase db;
    MutableLiveData<Cursor> myCursor = new MutableLiveData<Cursor>();

    public CursorViewModel(@NonNull Application application) {
        super(application);
        db = new ExpensesDatabase(application);
        db.open();
        myCursor.setValue(db.getAllNames());
        db.close();
    }

    public MutableLiveData<Cursor> getData() {
        return myCursor;
    }

    public Cursor getData(int id) {
        db.open();
        Cursor mycursor;
        mycursor = db.get1ID(id);
        return mycursor;
    }

    // this uses the Convenience method to add something to the database and then update the cursor.
    public void add(String name, String category, String date, Double amount, String note) {
        db.open();
        db.insertValues(name, category, date, amount, note);
        myCursor.setValue(db.getAllNames());
        db.close();
    }

    // this uses the Convenience method to update something from the database and then update the cursor.
    public int Update(String TableName, ContentValues values, String selection, String[] selectionArgs) {
        db.open();
        int ret = db.Update(TableName, values, selection, selectionArgs);
        myCursor.setValue(db.getAllNames());
        db.close();
        return ret;
    }

    // this uses the Convenience method to delete something in the database and then update the cursor.
    public int Delete(String TableName, String selection, String[] selectionArgs) {
        db.open();
        int ret = db.Delete(TableName, selection, selectionArgs);
        myCursor.setValue(db.getAllNames());
        db.close();
        return ret;
    }


}