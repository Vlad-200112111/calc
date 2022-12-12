package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivityVhod extends AppCompatActivity {

    private Button button_registr, button_autorizaciya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vhod);
        getSupportActionBar().hide();

        button_registr = (Button) findViewById(R.id.button_registr);
        button_autorizaciya = (Button) findViewById(R.id.button_autorizaciya);
    }

    public void OnClickBtnRegistraciya(View view) {
        Intent intent = new Intent(MainActivityVhod.this, MainActivityRegistraciya.class);
        startActivity(intent);
    }

    public void OnClickBtnAutorizaciya(View view) {
        Intent intent = new Intent(MainActivityVhod.this, MainActivityAutorizaciya.class);
        startActivity(intent);
    }
}