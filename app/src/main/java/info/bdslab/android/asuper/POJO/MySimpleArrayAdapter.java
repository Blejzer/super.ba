package info.bdslab.android.asuper.POJO;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import info.bdslab.android.asuper.R;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;
    public SharedPreferences sharedPreferences;

    public MySimpleArrayAdapter(Context context, String[] values) {
        super(context, android.R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = (TextView) rowView.findViewById(android.R.id.text1);
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);
        // Change the icon for Windows and iPhone
//        Log.w("MySimpleArrayAdapter", values[position] + " "+ sharedPreferences.getString(values[position], "TEST"));
        try {
//            Log.w("MySimple String", values[position] + " "+ sharedPreferences.getString(values[position], "TEST"));
            boolean test = sharedPreferences.getBoolean(values[position], true);
            if (test){
                rowView.setBackgroundColor(Color.TRANSPARENT);
            }else {
                rowView.setBackgroundColor(Color.GRAY);
            }
        }
        catch (Exception e){
//            Log.w("MySimpleArray Bool", values[position] + " "+ sharedPreferences.getBoolean(values[position], true));
        }

        return rowView;
    }
}
