package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivityDetailCredirVar extends AppCompatActivity {

    private ImageButton imageBtnMenu, imageBtnMenu2;
    private LinearLayout LayoutMenu;
    private String idCredit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail_credir_var);
        getSupportActionBar().hide();

        imageBtnMenu = (ImageButton) findViewById(R.id.imageBtnMenu);
        imageBtnMenu2 = (ImageButton) findViewById(R.id.imageBtnMenu2);
        LayoutMenu = (LinearLayout) findViewById(R.id.LayoutMenu);

        Intent intent = getIntent();
        idCredit = intent.getStringExtra("idCredit");
    }


    public void OnClickBtnGrafikPlategei(View view) {
        Intent intent = new Intent(this, MainActivityGrafickiPlategei.class);
        intent.putExtra("idCredit", String.valueOf(idCredit));
        startActivity(intent);
    }

    public void OnClickBtnMenu(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein_vis);
        LayoutMenu.startAnimation(animation);
        LayoutMenu.setVisibility(View.VISIBLE);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.povorot);
        imageBtnMenu.startAnimation(animation2);
        imageBtnMenu2.startAnimation(animation2);
        imageBtnMenu.setVisibility(View.INVISIBLE);
        imageBtnMenu2.setVisibility(View.VISIBLE);

    }

    public void OnClickBtnMenu2(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein_invis);
        LayoutMenu.startAnimation(animation);
        LayoutMenu.setVisibility(View.INVISIBLE);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.povorot);
        imageBtnMenu2.startAnimation(animation2);
        imageBtnMenu.startAnimation(animation2);
        imageBtnMenu2.setVisibility(View.INVISIBLE);
        imageBtnMenu.setVisibility(View.VISIBLE);
    }
}