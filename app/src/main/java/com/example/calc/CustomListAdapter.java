package com.example.calc;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;

    public CustomListAdapter(Activity context, String[] maintitle, String[] subtitle) {
        super(context, R.layout.activity_custom_list_view, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_custom_list_view, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.titlelistview);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitlelistview);

        titleText.setText(maintitle[position]);
        subtitleText.setText(subtitle[position]);

        return rowView;

    }
}
