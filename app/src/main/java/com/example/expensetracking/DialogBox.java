package com.example.expensetracking;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetracking.db.CursorViewModel;
import com.example.expensetracking.db.mySQLiteHelper;


public class DialogBox extends DialogFragment {

    private DialogBoxListener listener;

    private TextView editid;
    private EditText editname;
    private EditText editcategory;
    private EditText editdate;
    private EditText editamount;
    private EditText editnote;
    private CursorViewModel mViewModel;

    private Integer GlobalID;
    private String GlobalName;
    private String GlobalCategory;
    private String GlobalDate;
    private Double GlobalAmount;
    private String GlobalNote;
    private Boolean CheckArg = false;


    public DialogBox() {
        // Empty constructor required for DialogFragment
    }

    public DialogBox(Bundle args){      //gets values if a record is clicked on
        GlobalID = args.getInt("MyID");
        GlobalName = args.getString("MyName");
        GlobalCategory = args.getString("MyCategory");
        GlobalDate = args.getString("MyDate");
        GlobalAmount = args.getDouble("MyAmount");
        GlobalNote = args.getString("MyNote");
        CheckArg = true;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CursorViewModel mViewModel = new ViewModelProvider(getActivity()).get(CursorViewModel.class);

        LayoutInflater inflater = LayoutInflater.from(requireActivity());
        View myView = inflater.inflate(R.layout.fragment_dialog_box, null);

        editid = (TextView) myView.findViewById(R.id.ID);
        editname = (EditText) myView.findViewById(R.id.NameField);
        editcategory = (EditText) myView.findViewById(R.id.CategoryField);
        editdate = (EditText) myView.findViewById(R.id.DateField);
        editamount = (EditText) myView.findViewById(R.id.AmountField);
        editnote = (EditText) myView.findViewById(R.id.NoteField);
        editname.requestFocus();
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(requireActivity(), androidx.appcompat.R.style.ThemeOverlay_AppCompat_Dialog));
        builder.setView(myView).setTitle("Expense");

        if (CheckArg == true){              //if the record is being edited, autofill the rows of the dialog box
            editname.setText(GlobalName);
            editcategory.setText(GlobalCategory);
            editdate.setText(GlobalDate);
            String amount = GlobalAmount.toString();
            editamount.setText(amount);
            editnote.setText(GlobalNote);
        }

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String Name = editname.getText().toString();            //gets dialog box values
                String Category = editcategory.getText().toString();
                String Date = editdate.getText().toString();
                Double Amount = Double.parseDouble(editamount.getText().toString());
                String Note = editnote.getText().toString();

                if (CheckArg == true){          //if editing, then use this, as it updates using the ID
                    String selection = "_id = ?";
                    String[] selectionArgs = {GlobalID.toString()};
                    ContentValues values = new ContentValues();
                    values.put("Name", Name);
                    values.put("Category", Category);
                    values.put("Date", Date);
                    values.put("Amount", Amount);
                    values.put("Note", Note);

                    mViewModel.Update(mySQLiteHelper.TABLE_NAME, values, selection, selectionArgs);
                } else{         //if not editing, but creating new row
                    mViewModel.add(Name, Category, Date, Amount, Note);
                }

                dismiss();
            }
        });
        builder.setCancelable(true);

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {      //when the delete button is pressed to delete the record
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String selection = "Name=? and Category=? and Date=? and Amount=? and Note=?";
                String[] selectionArgs = {GlobalName, GlobalCategory, GlobalDate, GlobalAmount.toString(), GlobalNote};
                mViewModel.Delete(mySQLiteHelper.TABLE_NAME, selection, selectionArgs);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {         //when cancel button is pressed to stop editing
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return dialog;
    }


    public interface DialogBoxListener {
        void onFinishEditDialog(String inputText);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogBoxListener) requireActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(requireActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}