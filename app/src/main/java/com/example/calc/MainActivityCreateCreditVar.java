package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.Date;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import java.io.IOException;
import java.text.DecimalFormat;

public class MainActivityCreateCreditVar extends AppCompatActivity {

    private ImageButton imageButton_exit;
    private String userId;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private EditText dateEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_create_credit_var);
        getSupportActionBar().hide();


        imageButton_exit = (ImageButton) findViewById(R.id.imageButton_exit);

        // Получаем экземпляр элемента Spinner
        Spinner spinner = findViewById(R.id.spinner);

        // Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.repaymentProcedure,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Вызываем адаптер
        spinner.setAdapter(adapter);

        dateEdt = findViewById(R.id.date_vidachi);

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
                        MainActivityCreateCreditVar.this,
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


    public void OnClickExit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void OnClickBtnSave(View view) throws ParseException {

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

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        EditText _title = (EditText) findViewById(R.id.credit_title);
        EditText _summ = (EditText) findViewById(R.id.summ_credit);
        EditText _percent = (EditText) findViewById(R.id.percent);
        EditText _count_date = (EditText) findViewById(R.id.count_date);
        EditText _date = (EditText) findViewById(R.id.date_vidachi);
        Spinner _spinner = (Spinner) findViewById(R.id.spinner);

        String title = _title.getText().toString();
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
            newValuesForTableCredits.put("title", title);
            newValuesForTableCredits.put("summa", summ);
            newValuesForTableCredits.put("persent", roundAvoid(Double.parseDouble(percent), 2));
            newValuesForTableCredits.put("count_date", countDate);
            newValuesForTableCredits.put("date_vidachi", date);
            newValuesForTableCredits.put("pereplata",roundAvoid(mainSum - Double.parseDouble(summ), 2));
            newValuesForTableCredits.put("poryadoc_platega", valueSpinner);
            newValuesForTableCredits.put("main_summ", roundAvoid(mainSum, 2));
            newValuesForTableCredits.put("user_id", Integer.valueOf(userId));

            // Вставьте строку в вашу базу данных.
            long idCredit = mDb.insert("credits", null, newValuesForTableCredits);

            //Проверка
            if (idCredit != -1) {
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
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Произошла ошибка!", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (valueSpinner == "Дифференцированный платеж") {

        } else {
            Toast toaxsst = Toast.makeText(getApplicationContext(),
                    "ERROR", Toast.LENGTH_SHORT);
            toaxsst.show();
        }
    }

    private static String convertDateToString(Date date)
    {
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        date=new Date();
        String strDate=dateFormat.format(date);
        return strDate;
    }

    private static Calendar getCalendarFromString(String str_date) throws ParseException
    {
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

    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }


}