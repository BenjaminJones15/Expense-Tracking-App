package com.example.expensetracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import com.example.expensetracking.db.CursorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogBox.DialogBoxListener{
    RecyclerView mRecyclerView;
    Adapter adapter;
    FloatingActionButton EditButton;
    CursorViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(CursorViewModel.class);

        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new Adapter(R.layout.row_layout, this, null, mViewModel);
        mRecyclerView.setAdapter(adapter);

        mViewModel.getData().observe(this, new Observer<Cursor>() {
            @Override
            public void onChanged(Cursor data) {
                adapter.setCursor(data);
            }
        });


        EditButton = findViewById(R.id.floatingActionButton);
        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBox dialogbox = new DialogBox();
                dialogbox.show(getSupportFragmentManager(), "dialogbox");
            }
        });


    }

    @Override
    public void onFinishEditDialog(String inputText) {

    }
}