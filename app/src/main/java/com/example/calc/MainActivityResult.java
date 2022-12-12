package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivityResult extends AppCompatActivity {

    private ImageButton imageButton_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_result);
        getSupportActionBar().hide();


        imageButton_exit = (ImageButton) findViewById(R.id.imageButton_exit);
    }

    public void OnClickExit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}