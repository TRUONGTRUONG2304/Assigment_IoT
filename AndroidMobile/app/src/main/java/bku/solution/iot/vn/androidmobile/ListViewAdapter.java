package bku.solution.iot.vn.androidmobile;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PHITRUONG on 10/13/2021.
 */

public class ListViewAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<MainActivity.Items> items;

    public ListViewAdapter(ArrayList <MainActivity.Items> items, Activity activity){
        this.items = items;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        convertView = inflater.inflate(R.layout.items, null);

        TextView txtObject = convertView.findViewById(R.id.txtObject);
        TextView txtTime = convertView.findViewById(R.id.txtTime);
        TextView txtNumber = convertView.findViewById(R.id.txtNumber);
        TextView background = convertView.findViewById(R.id.tv_item);
        if(items.get(position).getType() == "TEMP"){
            background.setBackgroundColor(Color.rgb(255,255,255));
            txtObject.setText(items.get(position).getType());
            txtObject.setTextColor(Color.RED);
            txtTime.setText(items.get(position).getTime());
            txtNumber.setText(items.get(position).getNumber() + "°C");
        }
        if(items.get(position).getType() == "HUMI"){
            background.setBackgroundColor(Color.rgb(198,226,255));
            txtObject.setText(items.get(position).getType());
            txtObject.setTextColor(Color.GREEN);
            txtTime.setText(items.get(position).getTime());
            txtNumber.setText(items.get(position).getNumber() + "%");
        }

        return convertView;
    }
}
