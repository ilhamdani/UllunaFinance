package com.ulluna.ullunafinance;

/**
 * Created by Tomasz on 1/26/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final float[] cost;

    public MySimpleArrayAdapter(Context context, String[] values, float[] cost) {
        super(context, R.layout.list_adapter, values);
        this.context = context;
        this.values = values;
        this.cost = cost;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_adapter, parent, false);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView valueView = (TextView) rowView.findViewById(R.id.textViewPrice);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        firstLine.setText(values[position]);
        valueView.setText("$" + Stock.roundPrice(cost[position]));

        // change the icon for Windows and iPhone
        String s = values[position];
        if (s.startsWith("iPhone")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }
        return rowView;
    }
}