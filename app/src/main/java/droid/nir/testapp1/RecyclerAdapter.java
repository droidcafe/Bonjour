package droid.nir.testapp1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Collections;
import java.util.List;

/**
 * Created by user on 6/15/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHold>{

    LayoutInflater layoutInflater;
    Context context;

    List<nav_data> nav_data_list = Collections.emptyList();

    public  RecyclerAdapter(Context context,List<nav_data> nav_data_list)
    {
        this.context = context;
        this.nav_data_list = nav_data_list;
    }
    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.nav_recycler_layout,parent,false);

        ViewHold viewHold = new ViewHold(v);

        return viewHold;
    }



    @Override
    public void onBindViewHolder(ViewHold holder, int position) {

        nav_data current = nav_data_list.get(position);
        holder.nav_text.setText(current.name);
        holder.nav_image.setImageResource(current.id);
    }

    @Override
    public int getItemCount() {

        return nav_data_list.size();
    }

    public class ViewHold extends  RecyclerView.ViewHolder
    {

        TextView nav_text;
        ImageView nav_image;
        public ViewHold(View itemView) {
            super(itemView);
            nav_image = (ImageView)itemView.findViewById(R.id.nav_icon);
            nav_text = (TextView) itemView.findViewById(R.id.nav_text);

        }
    }



}
