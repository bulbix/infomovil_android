package com.infomovil.infomovil.gui.adapter;


import java.util.List;

import com.infomovil.infomovil.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/***
 * Clase adaptadora para cualquier lista
 * @author BAZ
 *
 * @param <E>
 */
public class CustomArrayAdapter<E> extends ArrayAdapter<E> {
    private final Context context;
    private final List<E> values;
    
    public CustomArrayAdapter(Context context, List<E> values) {
        super(context, android.R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.celda_cuenta, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.labelCeldaConfiguracion);
        textView.setClickable(false);

        // Customization to your textView here
        textView.setText(values.get(position).toString());
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir LT 45 Book.ttf");
        textView.setTypeface(myTypeface);

        return rowView;
    }
}