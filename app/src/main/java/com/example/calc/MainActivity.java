package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnMyCreditVars, btnSpravka,btnCreateVar;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        btnMyCreditVars = (ImageButton) findViewById(R.id.btnMyCreditVars);
        btnSpravka = (ImageButton) findViewById(R.id.btnSpravka);
        btnCreateVar = (ImageButton) findViewById(R.id.btnCreateVar);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
    }

    public void OnClickMyCreditVars(View view) {
        Intent intent = new Intent( MainActivity.this, MainActivityMyCreditVars.class);
        intent.putExtra("userId", String.valueOf(userId));
        startActivity(intent);
    }


    public void OnClickCreateCreditVar(View view) {
        Intent intent = new Intent( MainActivity.this, MainActivityCreateCreditVar.class);
        intent.putExtra("userId", String.valueOf(userId));
        startActivity(intent);
    }


    public void OnClickSpravka(View view) {
        Intent intent = new Intent( MainActivity.this, MainActivitySpravka.class);
        intent.putExtra("userId", String.valueOf(userId));
        startActivity(intent);
    }
}