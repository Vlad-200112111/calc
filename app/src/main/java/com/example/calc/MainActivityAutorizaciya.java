package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivityAutorizaciya extends AppCompatActivity {

    private Button button_auto_vhod;
    private ImageButton imageButton_exit;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String login;
    private String password;
    private Integer idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_autorizaciya);
        getSupportActionBar().hide();

        button_auto_vhod = (Button) findViewById(R.id.button_auto_vhod);
        imageButton_exit = (ImageButton) findViewById(R.id.imageButton_exit);
    }

    public void OnClickBtnAutorizaciyaVoiti(View view) {
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

        EditText txtLogin = (EditText) findViewById(R.id.text_login);
        EditText txtPassword = (EditText) findViewById(R.id.text_password);
        login = txtLogin.getText().toString();
        password = txtPassword.getText().toString();

        Cursor data = mDb.rawQuery("SELECT * FROM users WHERE login = '" + login + "' AND password = '" + password + "'", null);

        if (data.moveToNext()) {
            idUser = data.getInt(0);
            Intent intent = new Intent(MainActivityAutorizaciya.this, MainActivity.class);
            intent.putExtra("userId", String.valueOf(idUser));
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Неверный логин или пароль!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void OnClickExit(View view) {
        Intent intent = new Intent(this, MainActivityVhod.class);
        startActivity(intent);
    }
}