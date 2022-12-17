package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivityGrafickiPlategei extends AppCompatActivity {

    private ImageButton imageButton_exit;
    private String userId;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private LinearLayout pdf_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_graficki_plategei);
        getSupportActionBar().hide();

        mDBHelper = new DatabaseHelper(this);
        pdf_layout = findViewById(R.id.ll_pdflayout);
        Button generatePdfButton = (Button) findViewById(R.id.generatePdfButton);

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
        TextView gfName = findViewById(R.id.gfName);
        TextView gfSum = findViewById(R.id.gfSum);
        TextView gfDate = findViewById(R.id.gfDate);
        TextView gfAllVp = findViewById(R.id.gfAllVp);
        TextView gfPereplata = findViewById(R.id.gfPereplata);
        ArrayList<String> theList = new ArrayList<>();

        Intent intent = getIntent();
        String idCredit = intent.getStringExtra("idCredit");

        Cursor data = mDb.rawQuery("SELECT date_plateg, pluteg FROM credit_detail WHERE credit_id = " + idCredit + "", null);
        Cursor cursorData = mDb.rawQuery("SELECT title, summa, date_vidachi, main_summ, pereplata FROM credits WHERE id = " + idCredit + "", null);


        if (cursorData.moveToNext()) {
            gfName.setText(cursorData.getString(0));
            gfSum.setText(cursorData.getString(1));
            gfDate.setText(cursorData.getString(2));
            gfAllVp.setText(cursorData.getString(3));
            gfPereplata.setText(cursorData.getString(4));
        }

        String[] theListDateDetailCredit = new String[data.getCount()];
        String[] theListPlatezhDetailCredit = new String[data.getCount()];

        int i = 0;
        while (data.moveToNext()) {
            theListDateDetailCredit[i] = data.getString(0);
            theListPlatezhDetailCredit[i] = data.getString(1);
            i++;
        }
        CustomListAdapter listAdapter = new CustomListAdapter(this, theListDateDetailCredit, theListPlatezhDetailCredit);
        listView.setAdapter(listAdapter);

        generatePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePdf(idCredit);
            }
        });

    }

    private void generatePdf(String idCredit) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float height = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHeight = (int) height, convertWidth = (int) width;

        // создаем документ
        PdfDocument document = new PdfDocument();
        // определяем размер страницы
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(convertWidth, convertHeight, 1).create();
        // получаем страницу, на котором будем генерировать контент
        PdfDocument.Page page = document.startPage(pageInfo);

        // получаем холст (Canvas) страницы
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        // получаем контент, который нужно добавить в PDF, и загружаем его в Bitmap
        Bitmap bitmap = loadBitmapFromView(pdf_layout, pdf_layout.getWidth(), pdf_layout.getHeight());
        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);

        // рисуем содержимое и закрываем страницу
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // сохраняем записанный контент
        String targetPdf = dir.getAbsolutePath() + "/Кредит-" + idCredit + ".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getApplicationContext(), "PDF сохранён в " + filePath.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();
            // обновляем список
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Что-то пошло не так: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // закрываем документ
        document.close();
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    public void OnClickExit(View view) {
        Intent intent = new Intent(this, MainActivityDetailCredirVar.class);
        startActivity(intent);
    }
}