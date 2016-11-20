package droid.nir.testapp1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 6/30/2015.
 */
public class TextRecyclerAdapter extends RecyclerView.Adapter<TextRecyclerAdapter.ViewHold> {

    List<pro_data> pro_datas = Collections.EMPTY_LIST;
    Context context;
    LayoutInflater layoutInflater;

    TextRecyclerAdapter(Context context, List<pro_data> pro_datas)
    {
        this.context = context;
        this.pro_datas = pro_datas;
    }
    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewHold viewHold ;
        layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.textlist,parent,false);
        viewHold = new ViewHold(v);
        return viewHold;
    }


    @Override
    public void onBindViewHolder(ViewHold holder, int position) {

        holder.textview.setText(pro_datas.get(position).pro);

    }

    @Override
    public int getItemCount() {
        return pro_datas.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder{

        TextView textview;
        ViewHold(View view)
        {
            super(view);
            textview = (TextView) view.findViewById(R.id.textlistitem);

        }
    }
}
