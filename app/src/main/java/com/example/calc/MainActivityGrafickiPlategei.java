package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivityGrafickiPlategei extends AppCompatActivity {

    private ImageButton imageButton_exit;
    private String userId;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_graficki_plategei);
        getSupportActionBar().hide();

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        ListView listView = findViewById(R.id.listViewGraficiPlategei);
        ArrayList<String> theList = new ArrayList<>();

        Intent intent = getIntent();
        String idCredit = intent.getStringExtra("idCredit");

        Cursor data = mDb.rawQuery("SELECT date_plateg, pluteg FROM credit_detail WHERE credit_id = " + idCredit + "", null);

        while(data.moveToNext()){
            theList.add(data.getString(0) + "                 " + data.getString(1));
            ListAdapter listAdapter = new ArrayAdapter<>(this,R.layout.activity_custom_list_view,theList);
            listView.setAdapter(listAdapter);
        }
    }

    public void OnClickExit(View view) {
        Intent intent = new Intent(this, MainActivityDetailCredirVar.class);
        startActivity(intent);
    }
}