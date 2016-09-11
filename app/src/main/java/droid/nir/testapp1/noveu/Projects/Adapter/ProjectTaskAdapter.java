package droid.nir.testapp1.noveu.Projects.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.Home;
import droid.nir.testapp1.noveu.Home.data.dataHome;
import droid.nir.testapp1.noveu.Projects.ProjectTask;
import droid.nir.testapp1.noveu.Tasks.Add_Expand;
import droid.nir.testapp1.noveu.Tasks.Loaders.DeleteTask;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.recycler.Interfaces.ItemTouchHelperAdapter;

/**
 * Created by droidcafe on 3/15/2016.
 */
public class ProjectTaskAdapter extends RecyclerView.Adapter<ProjectTaskAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    List<dataHome> taskList;
    Context context;
    Import anImport;

    public ProjectTaskAdapter(Context context, List<dataHome> list) {
        this.context = context;
        taskList = list;
        anImport = new Import(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (taskList.get(position).taskid != -1) {
            holder.taskHeader.setVisibility(View.GONE);
            holder.taskName.setVisibility(View.VISIBLE);
            holder.proName.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);


            holder.taskName.setText(taskList.get(position).tasktitle);
            holder.proName.setText(taskList.get(position).projectName);
            anImport.settypefaces("SourceSansPro-Regular.otf", holder.taskName);

            holder.task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_expand = new Intent(context, Add_Expand.class);

                    Bundle bundle_expand = new Bundle();
                    bundle_expand.putInt("taskid", taskList.get(position).taskid);
                    bundle_expand.putInt("choice", 1);

                    intent_expand.putExtras(bundle_expand);
                    context.startActivity(intent_expand);
                }
            });

        } else {

            //title

            holder.taskHeader.setVisibility(View.VISIBLE);

            holder.taskHeader.setText(taskList.get(position).tasktitle);
            holder.proName.setVisibility(View.GONE);
            holder.taskName.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);

            anImport.settypefaces("Raleway-SemiBold.ttf", holder.taskHeader);
        }
    }




    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public void itemSwipeDismiss(int position, int direction) {


            Log.d("ta", "position " + position + " " + taskList.get(position).taskid);
            if(taskList.get(position).taskid !=-1)
            {
                new DeleteTask.AsyncDelete(0).execute(taskList.get(position).taskid);
                taskList.remove(position);
                notifyItemRemoved(position);
                new ProjectTask.AsyncLoad().execute();

            }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView proName;
        TextView taskName;
        TextView taskHeader;
        View view;
        View task;

        public ViewHolder(View itemView) {
            super(itemView);

            proName = (TextView) itemView.findViewById(R.id.proname);
            taskName = (TextView) itemView.findViewById(R.id.thetask);
            taskHeader = (TextView) itemView.findViewById(R.id.taskhead);
            view = itemView.findViewById(R.id.titleline);
            task = itemView.findViewById(R.id.task);


        }
    }


}

