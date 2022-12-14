package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class MainActivityRegistraciya extends AppCompatActivity {

    private Button button_save_registr;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String login;
    private String password;
    private String name;
    private String family;
    private String number;
    private String email;
    private String patronymic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registraciya);
        getSupportActionBar().hide();

        button_save_registr = (Button) findViewById(R.id.button_save_registr);
    }

    public void OnClickBtnRegistraciyaVoiti(View view) {
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


        EditText txtName = (EditText) findViewById(R.id.text_name);
        EditText txtFamily = (EditText) findViewById(R.id.text_family);
        EditText txtPatronymic = (EditText) findViewById(R.id.text_patronymic);
        EditText txtEmail = (EditText) findViewById(R.id.text_pochta);
        EditText txtNumber = (EditText) findViewById(R.id.text_number);
        EditText txtLogin = (EditText) findViewById(R.id.text_loginreg);
        EditText txtPassword = (EditText) findViewById(R.id.text_passwordreg);

        name = txtName.getText().toString();
        family = txtFamily.getText().toString();
        patronymic = txtPatronymic.getText().toString();
        email = txtEmail.getText().toString();
        number = txtNumber.getText().toString();
        login = txtLogin.getText().toString();
        password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(family) || TextUtils.isEmpty(patronymic) || TextUtils.isEmpty(email) || TextUtils.isEmpty(number) || TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            // Создайте новую строку со значениями для вставки.
            ContentValues newValues = new ContentValues();
            // Задайте значения для каждой строки.
            newValues.put("name", name);
            newValues.put("family", family);
            newValues.put("patronumic", patronymic);
            newValues.put("number", number);
            newValues.put("email", email);
            newValues.put("login", login);
            newValues.put("password", password);

            // Вставьте строку в вашу базу данных.
            long data = mDb.insert("users", null, newValues);

            //Проверка
            if (data != -1) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Успешная регистрация!", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Произошла ошибка!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}