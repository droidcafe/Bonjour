package droid.nir.testapp1;

import android.content.Context;

import droid.nir.testapp1.noveu.Util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by user on 7/21/2015.
 */
public class TodolistAdapter extends ArrayAdapter<MyObj> {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private static String TAG = "RecycleAdapter";

    public TodolistAdapter(Context context, int resource) {
        super(context, resource);

        mLayoutInflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    static class ViewHolder {
        TextView list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "position=" + position);

       ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.todo_list_item, parent, false);
            holder = new ViewHolder();
            holder.list = (TextView) convertView.findViewById(R.id.todolistitem);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyObj data = getItem(position);
        holder.list.setText(data.name);


        return convertView;

    }
}
