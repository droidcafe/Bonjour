package droid.nir.testapp1.todolist.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import droid.nir.testapp1.R;

/**
 * Created by user on 12/22/2015.
 */
public class Alladapters extends RecyclerView.Adapter<Alladapters.ViewHold> {

    List<custom_Data> passDatas;
    Context context;
    public  Alladapters(Context context, List<custom_Data> passDatas)
    {
        this.passDatas = passDatas;
        this.context = context;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.card_alllists,parent, false);

        return (new ViewHold(v));

    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {

        holder.tasktitle.setText(passDatas.get(position).tasktitle);
        holder.taskproject.setText(passDatas.get(position).taskproject);
    }

    @Override
    public int getItemCount() {
        return passDatas.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {

        TextView tasktitle , taskproject;
        CardView taskcard;

        public ViewHold(View itemView) {
            super(itemView);


            tasktitle = (TextView)itemView.findViewById(R.id.tasktitle);
            taskproject = (TextView)itemView.findViewById(R.id.taskproject);

            taskcard = (CardView) itemView.findViewById(R.id.task_card);
        }
    }
}
