package droid.nir.testapp1.noveu.Home.Adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.noveu.Home.data.dataEvent;

/**
 * Created by droidcafe on 11/23/2016.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    static List<dataEvent> eventList;
    Activity activity;

    public EventAdapter(List<dataEvent> eventlist,Activity activity) {
        eventList = eventlist;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View eventCard = layoutInflater.inflate(R.layout.event_card,parent,false);
        return new ViewHolder(eventCard);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle , customstatement, fromm, too, fromdate, todate, totime, fromtime, time2right, timetoright;
        TextView timetext, date, date1, date2;
        CardView eventCard;
        public ViewHolder(View itemView) {
            super(itemView);

            eventCard = (CardView) itemView.findViewById(R.id.pending_cards);
            eventTitle = (TextView) itemView.findViewById(R.id.pending_title);
            timetext = (TextView) itemView.findViewById(R.id.time);


            customstatement = (TextView) itemView.findViewById(R.id.pendingstatement);
            fromm = (TextView) itemView.findViewById(R.id.ttimefrom);
            fromdate = (TextView) itemView.findViewById(R.id.tfromdate);
            fromtime = (TextView) itemView.findViewById(R.id.tfromtime);
            too = (TextView) itemView.findViewById(R.id.ttimeto);
            todate = (TextView) itemView.findViewById(R.id.ttodate);
            totime = (TextView) itemView.findViewById(R.id.ttotime);
            time2right = (TextView) itemView.findViewById(R.id.ttime2right);
            timetoright = (TextView) itemView.findViewById(R.id.ttimetoright);
            date = (TextView) itemView.findViewById(R.id.date);
            date1 = (TextView) itemView.findViewById(R.id.date1);
            date2 = (TextView) itemView.findViewById(R.id.date2);
        }
    }
}
