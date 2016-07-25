package droid.nir.testapp1.noveu.Tasks.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import droid.nir.testapp1.noveu.Util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Tasks.data.SharedData;
import droid.nir.testapp1.noveu.Util.Import;

/**
 * Created by droidcafe on 3/30/2016.
 */
public class SublistAdapter extends RecyclerView.Adapter<SublistAdapter.ViewHolder> {

    int indexPosition;
    public SublistAdapter(int index) {

        indexPosition = index;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_subtasks, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int done = SharedData.subTaskdone.get(position);
        if(done==0)
        {
            holder.subheader.setVisibility(View.GONE);
            holder.itemLayout.setVisibility(View.VISIBLE);
            holder.subItem.setText(SharedData.list.get(position));
            holder.subItem.setPaintFlags(0);
            holder.subItem.setTextSize(18);
            holder.checkBox.setChecked(false);

            Import.settypefaces(holder.subItem.getContext(), "SourceSansPro-Regular.otf", holder.subItem);
        }
        else if(done==1)
        {
            holder.subheader.setVisibility(View.GONE);
            holder.itemLayout.setVisibility(View.VISIBLE);

            holder.subItem.setText(SharedData.list.get(position));
            holder.subItem.setPaintFlags(holder.subItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
            holder.subItem.setTextSize(15);

            holder.checkBox.setChecked(true);
        }
        else  if(done==-1)
        {
            holder.subheader.setVisibility(View.VISIBLE);
            holder.itemLayout.setVisibility(View.GONE);
            Import.settypefaces(holder.subItem.getContext(), "Raleway-SemiBold.ttf", holder.subheader);
        }

    }

    @Override
    public int getItemCount() {
        return SharedData.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox checkBox;
        TextView subItem,subheader;
        LinearLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            subItem = (TextView) itemView.findViewById(R.id.todolistitem);
            subheader = (TextView) itemView.findViewById(R.id.todolistheader);
            checkBox = (CheckBox) itemView.findViewById(R.id.done_check);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);

            checkBox.setOnClickListener(this);
            itemLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.done_check:
                    if(checkBox.isChecked())
                    toDoneItem(getAdapterPosition());
                    else
                    toUndoneItem(getAdapterPosition());
                    break;
            }
        }
    }

    private void toUndoneItem(int adapterPosition) {
        Log.d("sa","undone "+adapterPosition);
        String toChangeItem =removeItem(adapterPosition);
        addItem(toChangeItem,0);
    }


    public void toDoneItem(int adapterPosition) {

        String doneItem = removeItem(adapterPosition);
        int position = getChangedPosition();
        Log.d("sa", "position  " + position + " index " + indexPosition + "adapter " + adapterPosition);
        add(position,doneItem,1);
        notifyItemInserted(position);
    }

    public void addItem(String item,int done) {

        int position = getInsertPosition();
        Log.d("sa", "position " + position + " index " + indexPosition + " " + item);
        add(position, item, done);
        notifyItemInserted(position);
    }

    public String removeItem(int position)
    {
        String Item = SharedData.list.get(position);
        remove(position);
        notifyItemRemoved(position);

        return Item;
    }

    private int getInsertPosition() {
        if (indexPosition == -1)
            return SharedData.list.size();
        else {
            return indexPosition++;
        }
    }

    private int getChangedPosition() {
        if (indexPosition == -1) {
            addItem("Completed",-1);
            indexPosition = SharedData.list.size() - 1;
            Log.d("sa", "" + indexPosition);

        } else {
            indexPosition--;
        }

        return SharedData.list.size();
    }



    private void add(int position,String dataitem, int done)
    {
        SharedData.list.add(position, dataitem);
        SharedData.subTaskdone.add(position,done);
    }

    private void remove(int position)
    {
        SharedData.list.remove(position);
        SharedData.subTaskdone.remove(position);
    }

}
