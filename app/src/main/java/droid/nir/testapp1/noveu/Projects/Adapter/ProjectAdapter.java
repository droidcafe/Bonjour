package droid.nir.testapp1.noveu.Projects.Adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Dialogue.DialogueProjectManager;
import droid.nir.testapp1.noveu.Projects.ProjectTask;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.dB.Project;

/**
 * Created by droidcafe on 2/24/2016.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    static ArrayList<String> pronames;
    static ArrayList<Integer> proids;
    static ArrayList<Integer> prosize;
    static Context context;
    static  Activity activity;

    public static int lastClickedPosition;

    public ProjectAdapter(Context context, Activity activity, Cursor cursor) {
        pronames = new ArrayList<>();
        proids = new ArrayList<>();
        prosize = new ArrayList<>();
        this.context = context;
        this.activity = activity;

        lastClickedPosition = 0;
        cursor.moveToPosition(-1);
      //  Log.d("pa","position "+cursor.getPosition());
        while (cursor.moveToNext()) {
            pronames.add(cursor.getString(cursor.getColumnIndex(Project.columnNames[0][1])));
            proids.add(cursor.getInt(cursor.getColumnIndex(Project.columnNames[0][0])));
            prosize.add(cursor.getInt(cursor.getColumnIndex(Project.columnNames[0][2])));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_recycler_layout, parent, false);


        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.edit.setVisibility(View.GONE);
        holder.delete.setVisibility(View.GONE);
        String name = pronames.get(position);
        String size = "" + prosize.get(position) + " items";

        holder.proname.setText(name);
        holder.prosize.setText(size);




    }

    @Override
    public int getItemCount() {
        return pronames.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        TextView proname;
        TextView prosize;
        ImageButton more, edit, delete;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            proname = (TextView) itemView.findViewById(R.id.proname);
            prosize = (TextView) itemView.findViewById(R.id.prosize);
            more = (ImageButton) itemView.findViewById(R.id.more);
            edit = (ImageButton) itemView.findViewById(R.id.proedit);
            delete = (ImageButton) itemView.findViewById(R.id.prodelete);
            view = itemView.findViewById(R.id.prolayout);



            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastClickedPosition = getAdapterPosition();
                    PopupMenu popupMenu = new PopupMenu(context,view);
                    MenuInflater menuInflater = new MenuInflater(context);
                    menuInflater.inflate(R.menu.project_manager_card_more,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new moreMenuClickListener(getAdapterPosition()));
                    popupMenu.show();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("pa",""+getAdapterPosition()+" "+getLayoutPosition());

                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, ProjectTask.class);
                    Bundle bundle = new Bundle(2);
                    Log.d("pa", "position " + position);
                    bundle.putString("proname", pronames.get(position));
                    bundle.putInt("id", proids.get(position));

                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }


    }

    public void addItem(String item, int size, int id) {
        pronames.add(item);
        prosize.add(size);
        proids.add(id);

        notifyItemInserted(getItemCount() - 1);
    }

    public static void changeProjectName(String newProName) {
        pronames.set(lastClickedPosition, newProName);

    }

    public  void deleteProject() {
        Log.d("pa", "last " + lastClickedPosition);
        for (int i = 0; i < pronames.size(); i++)
            Log.d("pa", " " + pronames.get(i) + " " + i);

        pronames.remove(lastClickedPosition);
        proids.remove(lastClickedPosition);
        prosize.remove(lastClickedPosition);


        notifyItemRemoved(lastClickedPosition);

    }

    class moreMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        int position;
            moreMenuClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    DialogFragment alertDialog = DialogueProjectManager.newInstanceUpdate(
                            pronames.get(position), proids.get(position));
                    alertDialog.show(activity.getFragmentManager(), "dialogs");
                    break;
                case R.id.action_delete:
                    Log.d("pa",""+position);

                    DialogFragment deleteDialog = DialogueProjectManager.newInstanceDelete(
                            pronames.get(position), proids.get(position));
                    deleteDialog.show(activity.getFragmentManager(), "dialogs");
                    break;
            }
            return false;
        }
    }

}
