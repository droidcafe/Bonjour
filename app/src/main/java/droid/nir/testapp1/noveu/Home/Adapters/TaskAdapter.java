package droid.nir.testapp1.noveu.Home.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.data.dataHome;
import droid.nir.testapp1.noveu.Tasks.Add_Expand;
import droid.nir.testapp1.noveu.Tasks.Loaders.DeleteTask;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.recycler.Interfaces.ItemTouchHelperAdapter;
import droid.nir.testapp1.noveu.recycler.Interfaces.ItemTouchHelperViewHolder;

/**
 * Created by droidcafe on 2/27/2016.
 *
 * Adapter for task recycler list in home
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements ItemTouchHelperAdapter{

    public static List<dataHome> taskList;
    Activity activity;
    SparseBooleanArray selectedItems;

    public TaskAdapter(Context context,Activity activity, List<dataHome> list) {
        taskList = list;
        this.activity = activity;

        selectedItems = new SparseBooleanArray();
        Log.d("ta","size "+list.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(taskList.get(position).taskid!=-1)
        {
            holder.taskHeader.setVisibility(View.GONE);
            holder.taskName.setVisibility(View.VISIBLE);
            holder.proName.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);


            holder.taskName.setText(taskList.get(position).tasktitle);
            holder.proName.setText(taskList.get(position).projectName);
           Import.settypefaces(holder.taskName.getContext(), "SourceSansPro-Regular.otf", holder.taskName);



         /*   holder.task.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    ActionMode actionMode = (activity).startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            mode.setTitle(holder.task.getContext().getString(R.string.selected_count, getSelectedItemCount()));

                            MenuInflater inflater = mode.getMenuInflater();
                            inflater.inflate(R.menu.menu_action_mode, menu);


                            return true;
                        }



                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            return false;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {

                        }


                    });
                    return true;
                }
            });*/

        }
        /**
         * the header task items
         */
        else {

            //title

            holder.taskHeader.setVisibility(View.VISIBLE);

            holder.taskHeader.setText(taskList.get(position).tasktitle);
            holder.proName.setVisibility(View.GONE);
            holder.taskName.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);

            Import.settypefaces(holder.taskHeader.getContext(),"Raleway-SemiBold.ttf", holder.taskHeader);
        }
    }



    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void toggleSelection(int pos)
    {
        if(selectedItems.get(pos,false))
            selectedItems.delete(pos);
        else
            selectedItems.append(pos,true);

        notifyItemChanged(pos);
    }

    private void clearSelection()
    {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    @Override
    public void itemSwipeDismiss(int position, int direction) {

        Log.d("ta","position "+position +" "+taskList.get(position).taskid);
        int tid =+taskList.get(position).taskid ;
        if(tid !=-1)
        {
            DeleteTask.setDelayedDelete(activity,tid, 5000);
            taskList.remove(position);
            notifyItemRemoved(position);


        //    notifyItemRangeChanged(position,getItemCount());
//            Log.d("ta","removed "+taskList.get(position).tasktitle);
        }
    }




    public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView proName;
        TextView taskName;
        TextView taskHeader;
        View view ;
        View task;
        public ViewHolder(View itemView) {
            super(itemView);

            proName = (TextView) itemView.findViewById(R.id.proname);
            taskName = (TextView) itemView.findViewById(R.id.thetask);
            taskHeader = (TextView) itemView.findViewById(R.id.taskhead);
            view = itemView.findViewById(R.id.titleline);
            task = itemView.findViewById(R.id.task);

            task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_expand = new Intent(task.getContext(), Add_Expand.class);

                        Bundle bundle_expand = new Bundle();
                        bundle_expand.putInt("taskid", taskList.get(getAdapterPosition()).taskid);
                        bundle_expand.putInt("choice", 1);

                        intent_expand.putExtras(bundle_expand);
                        task.getContext().startActivity(intent_expand);
                    }
                });

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    /* @Override
    public long getHeaderId(int position) {

        Log.d("ta","id "+getItemId(position));
        if (getItemid(position) == -1)
            return getItemName(position).charAt(1);
        else
            return -1;

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("home", "binding header");

        TextView textView = (TextView) holder.itemView;
        textView.setText(String.valueOf(getItemName(position)));

    }*/

    public int getItemid(int position) {
        return taskList.get(position).taskid;
    }

    public String getItemName(int position){
        return taskList.get(position).tasktitle;
    }
}
