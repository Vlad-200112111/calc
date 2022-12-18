package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivityDetailCredirVar extends AppCompatActivity {

    private ImageButton imageBtnMenu, imageBtnMenu2;
    private LinearLayout LayoutMenu;
    private String idCredit;
    private String userId;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private EditText dateEdt;

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
        userId = intent.getStringExtra("idUser");

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


        Cursor cursorData = mDb.rawQuery("SELECT title, summa, persent, count_date, date_vidachi FROM credits WHERE id = " + idCredit + "", null);

        TextView name_for_change = (TextView) findViewById(R.id.name_for_change);
        EditText summ_for_change = (EditText) findViewById(R.id.summ_for_change);
        EditText persent_for_change = (EditText) findViewById(R.id.percent_for_change);
        EditText count_date_for_change = (EditText) findViewById(R.id.count_date_for_change);
        EditText date_vidachi_for_change = (EditText) findViewById(R.id.date_vidachi_for_change);

        if (cursorData.moveToNext()) {
            name_for_change.setText(cursorData.getString(0));
            summ_for_change.setText(cursorData.getString(1));
            persent_for_change.setText(cursorData.getString(2));
            count_date_for_change.setText(cursorData.getString(3));
            date_vidachi_for_change.setText(cursorData.getString(4));
        }

        // Получаем экземпляр элемента Spinner
        Spinner spinner = findViewById(R.id.spinner_for_change);

        // Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.repaymentProcedure,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Вызываем адаптер
        spinner.setAdapter(adapter);

        dateEdt = findViewById(R.id.date_vidachi_for_change);

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        MainActivityDetailCredirVar.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                dateEdt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
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

    private static String convertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = new Date();
        String strDate = dateFormat.format(date);
        return strDate;
    }

    private static Calendar getCalendarFromString(String str_date) throws ParseException {
        DateFormat formatter;
        Date date;

        if (str_date.contains(" "))
            str_date = str_date.substring(0, str_date.indexOf(" "));

        formatter = new SimpleDateFormat("dd-MM-yyyy");
        date = (Date) formatter.parse(str_date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private double annuityPayment(String a, String b, String c) {
        int summ = Integer.parseInt(a);
        double percent = Float.parseFloat(b);
        int countDate = Integer.parseInt(c);
        double m = percent / 100 / 12;
        double k = (m * Math.pow((1 + m), countDate)) / ((Math.pow((1 + m), countDate)) - 1);
        return summ * k;
    }

    private Double[] differentiatedPayment(String a, String b, String c) {
        int summ = Integer.parseInt(a);
        double percent = Float.parseFloat(b) / 100;
        int countDate = Integer.parseInt(c);
        int ezhemesVuplata = summ / countDate;
        int Qt = summ;
        Double[] res = new Double[countDate];
        for (int i = 0; i < countDate; i++) {
            res[i] = roundAvoid(ezhemesVuplata + (Qt * percent * 30 / 365), 2);
            Qt = Qt - ezhemesVuplata;
        }
        return res;
    }

    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public void handleSaveChange(View view) {

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

        EditText _summ = (EditText) findViewById(R.id.summ_for_change);
        EditText _percent = (EditText) findViewById(R.id.percent_for_change);
        EditText _count_date = (EditText) findViewById(R.id.count_date_for_change);
        EditText _date = (EditText) findViewById(R.id.date_vidachi_for_change);
        Spinner _spinner = (Spinner) findViewById(R.id.spinner_for_change);

        String summ = _summ.getText().toString();
        String percent = _percent.getText().toString();
        String countDate = _count_date.getText().toString();
        String date = _date.getText().toString();
        String valueSpinner = _spinner.getSelectedItem().toString();


        if (valueSpinner.length() == 18) {
            double s_0 = annuityPayment(summ, percent, countDate);
            double mainSum = s_0 * Integer.parseInt(countDate);

            // Создайте новую строку со значениями для вставки.
            ContentValues newValuesForTableCredits = new ContentValues();
            // Задайте значения для каждой строки.
            newValuesForTableCredits.put("summa", summ);
            newValuesForTableCredits.put("persent", roundAvoid(Double.parseDouble(percent), 2));
            newValuesForTableCredits.put("count_date", countDate);
            newValuesForTableCredits.put("date_vidachi", date);
            newValuesForTableCredits.put("pereplata", roundAvoid(mainSum - Double.parseDouble(summ), 2));
            newValuesForTableCredits.put("poryadoc_platega", valueSpinner);
            newValuesForTableCredits.put("main_summ", roundAvoid(mainSum, 2));

            mDb.update("credits", newValuesForTableCredits, "id = ?",
                    new String[]{String.valueOf(idCredit)});

            mDb.delete("credit_detail", "credit_id=?", new String[]{idCredit});
            for (int i = 0; i < Integer.parseInt(countDate); i++) {
                try {
                    Calendar dateRes = getCalendarFromString(date);
                    dateRes.add(Calendar.MONTH, i + 1);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = simpleDateFormat.format(dateRes.getTime());


                    ContentValues newValuesForTableDetailsCredit = new ContentValues();
                    newValuesForTableDetailsCredit.put("date_plateg", formattedDate);
                    newValuesForTableDetailsCredit.put("pluteg", roundAvoid(s_0, 2));
                    newValuesForTableDetailsCredit.put("credit_id", idCredit);

                    mDb.insert("credit_detail", null, newValuesForTableDetailsCredit);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Успешно!", Toast.LENGTH_SHORT);
            toast.show();

        } else if (valueSpinner.length() == 25) {
            Double[] viplati = differentiatedPayment(summ, percent, countDate);
            double mainSum = 0;
            for (Double d : viplati)
                mainSum += d;

            // Создайте новую строку со значениями для вставки.
            ContentValues newValuesForTableCredits = new ContentValues();
            // Задайте значения для каждой строки.
            newValuesForTableCredits.put("summa", summ);
            newValuesForTableCredits.put("persent", roundAvoid(Double.parseDouble(percent), 2));
            newValuesForTableCredits.put("count_date", countDate);
            newValuesForTableCredits.put("date_vidachi", date);
            newValuesForTableCredits.put("pereplata", roundAvoid(mainSum - Double.parseDouble(summ), 2));
            newValuesForTableCredits.put("poryadoc_platega", valueSpinner);
            newValuesForTableCredits.put("main_summ", roundAvoid(mainSum, 2));

            mDb.update("credits", newValuesForTableCredits, "id = ?",
                    new String[]{String.valueOf(idCredit)});

            mDb.delete("credit_detail", "credit_id=?", new String[]{idCredit});
            for (int i = 0; i < Integer.parseInt(countDate); i++) {
                try {
                    Calendar dateRes = getCalendarFromString(date);
                    dateRes.add(Calendar.MONTH, i + 1);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = simpleDateFormat.format(dateRes.getTime());


                    ContentValues newValuesForTableDetailsCredit = new ContentValues();
                    newValuesForTableDetailsCredit.put("date_plateg", formattedDate);
                    newValuesForTableDetailsCredit.put("pluteg", viplati[i]);
                    newValuesForTableDetailsCredit.put("credit_id", idCredit);

                    mDb.insert("credit_detail", null, newValuesForTableDetailsCredit);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Успешно!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toaxsst = Toast.makeText(getApplicationContext(),
                    "ERROR", Toast.LENGTH_SHORT);
            toaxsst.show();
        }
    }

    public void openEditText(View view) {
        EditText _summ = (EditText) findViewById(R.id.summ_for_change);
        EditText _percent = (EditText) findViewById(R.id.percent_for_change);
        EditText _count_date = (EditText) findViewById(R.id.count_date_for_change);
        EditText _date = (EditText) findViewById(R.id.date_vidachi_for_change);

        _summ.setEnabled(false);
        _percent.setEnabled(false);
        _count_date.setEnabled(false);
        _date.setEnabled(false);
    }

    public void deleteCredit(View view) {
        mDb.delete("credit_detail", "credit_id=?", new String[]{idCredit});
        mDb.delete("credits", "id=?", new String[]{idCredit});
        Intent intent = new Intent(this, MainActivityMyCreditVars.class);
        intent.putExtra("userId", String.valueOf(userId));
        startActivity(intent);
    }
}