package com.example.expensetracking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;


import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracking.db.CursorViewModel;
import com.example.expensetracking.db.mySQLiteHelper;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private int rowLayout;
    private Context mContext;
    private Cursor mCursor;
    private CursorViewModel Cviewmodel;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ID;
        public TextView Name;
        public TextView Category;
        public TextView Date;
        public TextView Amount;
        public TextView Note;


        public ViewHolder(View itemView) {
            super(itemView);
            ID = (TextView) itemView.findViewById(R.id.ID);
            Name = (TextView) itemView.findViewById(R.id.Name);
            Category = (TextView) itemView.findViewById(R.id.Category);
            Date = (TextView) itemView.findViewById(R.id.Date);
            Amount = (TextView) itemView.findViewById(R.id.Amount);
            Note = (TextView) itemView.findViewById(R.id.Note);
        }
    }

    public Adapter(int rowLayout, Context context, Cursor cursor, CursorViewModel cvm) {
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.mCursor = cursor;
        this.Cviewmodel = cvm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        //this assumes it's not called with a null mCursor, since i means there is a data.
        mCursor.moveToPosition(i);

        viewHolder.ID.setText(          //creates each row and fills it with the data
                mCursor.getString(mCursor.getColumnIndex(mySQLiteHelper.KEY_ROWID))
        );

        viewHolder.Name.setText(
                mCursor.getString(mCursor.getColumnIndex(mySQLiteHelper.KEY_NAME))
        );

        viewHolder.Name.setText(
                mCursor.getString(mCursor.getColumnIndex(mySQLiteHelper.KEY_NAME))
        );

        viewHolder.Category.setText(
                mCursor.getString(mCursor.getColumnIndex(mySQLiteHelper.KEY_CATEGORY))
        );

        viewHolder.Date.setText(
                mCursor.getString(mCursor.getColumnIndex(mySQLiteHelper.KEY_DATE))
        );

        viewHolder.Amount.setText(
                mCursor.getString(mCursor.getColumnIndex(mySQLiteHelper.KEY_AMOUNT))
        );

        viewHolder.Note.setText(
                mCursor.getString(mCursor.getColumnIndex(mySQLiteHelper.KEY_NOTE))
        );

        viewHolder.ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView ID = v.findViewById(R.id.ID);
                String id = ID.getText().toString();
                Integer Id = Integer.parseInt(id);

                Cursor mycursor;
                mycursor = Cviewmodel.getData(Id);

                int index;

                index = mycursor.getColumnIndexOrThrow("Name");         //gets all the data from the row that was clicked on
                String Name = mycursor.getString(index);

                index = mycursor.getColumnIndexOrThrow("Category");
                String Category = mycursor.getString(index);

                index = mycursor.getColumnIndexOrThrow("Date");
                String Date = mycursor.getString(index);

                index = mycursor.getColumnIndexOrThrow("Amount");
                String amount = mycursor.getString(index);
                Double Amount = Double.parseDouble(amount);

                index = mycursor.getColumnIndexOrThrow("Note");
                String Note = mycursor.getString(index);

                Bundle args = new Bundle();         //puts data into a bundle, to pass into the dialog box
                args.putInt("MyID", Id);
                args.putString("MyName", Name);
                args.putString("MyCategory", Category);
                args.putString("MyDate", Date);
                args.putDouble("MyAmount", Amount);
                args.putString("MyNote", Note);

                DialogBox dialogbox = new DialogBox(args);          //creates and shows the dialog box
                FragmentManager manager = ((MainActivity)mContext).getSupportFragmentManager();
                dialogbox.show(manager, "dialogbox");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    //change the cursor as needed and have the system redraw the data.
    public void setCursor(Cursor c) {
        mCursor = c;
        notifyDataSetChanged();
    }
}
