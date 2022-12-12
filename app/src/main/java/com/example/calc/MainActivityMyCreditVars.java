package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivityMyCreditVars extends AppCompatActivity {

    private View view2;
    private ImageButton imageButton_exit;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_my_credit_vars);
        getSupportActionBar().hide();

        view2 = (View) findViewById(R.id.view2);
        imageButton_exit = (ImageButton) findViewById(R.id.imageButton_exit);

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

        ListView listView = findViewById(R.id.listViewCredits);
        ArrayList<String> theList = new ArrayList<>();
        ArrayList<Integer> ar_ids = new ArrayList<>();

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        Cursor data = mDb.rawQuery("SELECT id, title, summa FROM credits WHERE user_id = " + userId + "", null);

        while(data.moveToNext()){
            ar_ids.add(data.getInt(0));
            theList.add(data.getString(1) + "                " + data.getString(2));
            ListAdapter listAdapter = new ArrayAdapter<>(this,R.layout.activity_custom_list_view,theList);
            listView.setAdapter(listAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent intent = new Intent(MainActivityMyCreditVars.this, MainActivityGrafickiPlategei.class);
                intent.putExtra("idCredit", String.valueOf(ar_ids.get(position)));
                startActivity(intent);
            }
        });
    }

    public void OnClickTextCredit(View view) {
        Intent intent = new Intent(MainActivityMyCreditVars.this, MainActivityDetailCredirVar.class);
        startActivity(intent);
    }

    public void OnClickExit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}