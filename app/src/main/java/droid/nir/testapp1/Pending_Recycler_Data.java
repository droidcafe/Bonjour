package droid.nir.testapp1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import droid.nir.animation.Recycleranims;
import droid.nir.testapp1.noveu.Util.Log;


/**
 * Created by user on 6/27/2015.
 */
public class Pending_Recycler_Data extends RecyclerView.Adapter<Pending_Recycler_Data.ViewHold> {

    Context context;
    LayoutInflater layoutInflater;
    List<pending_data> pending_items = Collections.EMPTY_LIST;
    List<custom_data> custom_items = Collections.EMPTY_LIST;
    List<custom_data2> custom_items2 = Collections.EMPTY_LIST;
    int choice, type, postion;
    Activity activity;
    toast maketext;
    private int currentpositionn = -1;
    Recycleranims recycleranims;

    public Pending_Recycler_Data(Context context, List<pending_data> pending_items, Activity activity) {
        this.context = context;
        this.pending_items = pending_items;
        choice = 1;
        maketext = new toast(context);
        recycleranims = new Recycleranims();
        this.activity = activity;

    }

    public Pending_Recycler_Data(Context context, List<custom_data> items, int m) {
        this.context = context;
        this.custom_items = items;
        choice = m;
        recycleranims = new Recycleranims();
    }

    public Pending_Recycler_Data(Context context, List<custom_data2> items, int m, int nouse) {
        this.context = context;
        this.custom_items2 = items;
        choice = m;
        recycleranims = new Recycleranims();
    }

    Pending_Recycler_Data(Context context, List<pending_data> pending_items, int m, String nouse) {

        this.context = context;
        this.pending_items = pending_items;
        choice = m;
        Log.d("pending", "called using " + choice);
        recycleranims = new Recycleranims();
    }


    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(context);
        View v;
        Log.d("pending", "called using oncreate " + choice);
        if (choice == 1) {
            // v = layoutInflater.inflate(R.layout.activity_floating_trials,parent,false);
            v = layoutInflater.inflate(R.layout.pending_cards, parent, false);
            ViewHold viewHold;
            viewHold = new ViewHold(v);
            return viewHold;
        } else if (choice == 2) {
            v = layoutInflater.inflate(R.layout.textlist, parent, false);
            ViewHold viewHold;
            viewHold = new ViewHold(v);
            return viewHold;

        } else if (choice == 3) {

            v = layoutInflater.inflate(R.layout.decision_card, parent, false);
            ViewHold viewHold;
            viewHold = new ViewHold(v);
            return viewHold;
        } else if ((choice == 5)) {
            v = layoutInflater.inflate(R.layout.todolistcard, parent, false);
            ViewHold viewHold;
            viewHold = new ViewHold(v);
            return viewHold;
        } else if ((choice == 6)) {
            v = layoutInflater.inflate(R.layout.remaider_card, parent, false);
            ViewHold viewHold;
            viewHold = new ViewHold(v);
            return viewHold;
        } else if (choice == 4) {
            v = layoutInflater.inflate(R.layout.event_card, parent, false);
            ViewHold viewHold;
            viewHold = new ViewHold(v);
            return viewHold;
        } else if (choice == 7) {

            v = layoutInflater.inflate(R.layout.custom_date_card, parent, false);
            ViewHold viewHold;
            viewHold = new ViewHold(v);
            return viewHold;
        }


        return null;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, final int position) {


        Log.d("pending", "the choice from outside is " + choice);
        Log.d("pending_recyclerdata", "orginal position is " + position);
        float width = context.getResources().getDimension(R.dimen.circlepaddingtop);
        int widt = (int) width;
        int right = (int) context.getResources().getDimension(R.dimen.circlepaddingright);
        postion = position;
        if (choice == 1) {

            String ff;
            holder.titletext.setText(pending_items.get(position).title);

            ff = pending_items.get(position).time;
            //ff =  ff.replace('.', ':');

            final int type = pending_items.get(position).type;
            if (type == 0) {

                Log.d("bind", "putitng clor");
                holder.timetext.setVisibility(View.GONE);

                holder.timetext2.setVisibility(View.GONE);
                holder.timetext3.setVisibility(View.GONE);

                holder.date1.setVisibility(View.GONE);
                holder.date2.setVisibility(View.GONE);
                holder.date3.setVisibility(View.GONE);


                holder.timetext1.setVisibility(View.VISIBLE);
                holder.timetext1.setText(ff);
                holder.timetext1.setPadding(pending_items.get(position).paddingleft, widt, right, 0);
                holder.fromtime.setVisibility(View.GONE);
                holder.fromdate.setVisibility(View.GONE);
                holder.fromm.setVisibility(View.GONE);
                holder.too.setVisibility(View.GONE);
                holder.todate.setVisibility(View.GONE);
                holder.totime.setVisibility(View.GONE);
                holder.time2right.setVisibility(View.GONE);
                holder.timetoright.setVisibility(View.GONE);

                holder.date.setVisibility(View.VISIBLE);
                holder.date.setText(pending_items.get(position).customstatement1);
            } else if (type == 1) {
                holder.timetext.setVisibility(View.GONE);
                holder.timetext1.setVisibility(View.GONE);

                holder.timetext3.setVisibility(View.GONE);
                holder.date.setVisibility(View.GONE);

                holder.date2.setVisibility(View.GONE);
                holder.date3.setVisibility(View.GONE);


                holder.timetext2.setVisibility(View.VISIBLE);
                holder.timetext2.setText(ff);
                holder.timetext2.setPadding(pending_items.get(position).paddingleft, widt, right, 0);
                int temp = pending_items.get(position).temp1;
                holder.titletext.setTextColor(context.getResources().getColor(R.color.eaccent));
                if (temp == 1)

                {
                    holder.fromtime.setVisibility(View.GONE);
                    holder.fromdate.setVisibility(View.GONE);
                    holder.fromm.setVisibility(View.GONE);
                    holder.too.setVisibility(View.GONE);
                    holder.todate.setVisibility(View.GONE);
                    holder.totime.setVisibility(View.GONE);
                    holder.time2right.setVisibility(View.GONE);
                    holder.timetoright.setVisibility(View.GONE);


                    holder.date1.setVisibility(View.VISIBLE);
                    holder.date1.setText(pending_items.get(position).customstatement1);
                } else {


                    String fromdatee = pending_items.get(position).customstatement1;

                    String todatee = pending_items.get(position).customstatement2;
                    if (fromdatee.equals(todatee)) {

                        holder.fromtime.setVisibility(View.GONE);

                        holder.too.setVisibility(View.GONE);
                        holder.todate.setVisibility(View.GONE);
                        holder.totime.setVisibility(View.GONE);

                        holder.time2right.setVisibility(View.VISIBLE);
                        holder.timetoright.setVisibility(View.VISIBLE);
                        holder.fromdate.setVisibility(View.VISIBLE);
                        holder.fromm.setVisibility(View.VISIBLE);

                        ff = pending_items.get(position).customstatement3;
                        ff = ff.replace('.', ':');
                        holder.fromdate.setText(ff);

                        ff = pending_items.get(position).customstatement4;
                        ff = ff.replace('.', ':');
                        holder.time2right.setText(ff);

                    } else {

                        holder.time2right.setVisibility(View.GONE);
                        holder.timetoright.setVisibility(View.GONE);


                        holder.fromtime.setVisibility(View.VISIBLE);
                        holder.fromdate.setVisibility(View.VISIBLE);
                        holder.fromm.setVisibility(View.VISIBLE);
                        holder.too.setVisibility(View.VISIBLE);
                        holder.todate.setVisibility(View.VISIBLE);
                        holder.totime.setVisibility(View.VISIBLE);

                        holder.fromdate.setText(fromdatee);
                        Log.d("pending_recycler_data", "" + pending_items.get(position).customstatement1 + " " + pending_items.get(position).customstatement2 + " " + pending_items.get(position).customstatement3 + " " + pending_items.get(position).customstatement4);
                        ff = pending_items.get(position).customstatement3;
                        ff = ff.replace(".", ":");
                        holder.fromtime.setText(ff);
                        holder.todate.setText(todatee);
                        ff = pending_items.get(position).customstatement4;
                        ff = ff.replace('.', ':');
                        holder.totime.setText(ff);

                    }


                }
            } else if (type == 2) {

                holder.timetext.setVisibility(View.GONE);
                holder.timetext1.setVisibility(View.GONE);
                holder.timetext2.setVisibility(View.GONE);

                holder.date.setVisibility(View.GONE);
                holder.date1.setVisibility(View.GONE);

                holder.date3.setVisibility(View.GONE);

                holder.timetext3.setVisibility(View.VISIBLE);
                holder.timetext3.setText(ff);

                holder.timetext3.setPadding(pending_items.get(position).paddingleft, widt, right, 0);
                holder.titletext.setTextColor(R.color.laccent);
                holder.fromtime.setVisibility(View.GONE);
                holder.fromdate.setVisibility(View.GONE);
                holder.fromm.setVisibility(View.GONE);
                holder.too.setVisibility(View.GONE);
                holder.todate.setVisibility(View.GONE);
                holder.totime.setVisibility(View.GONE);
                holder.time2right.setVisibility(View.GONE);
                holder.timetoright.setVisibility(View.GONE);

                holder.date2.setVisibility(View.VISIBLE);
                holder.date2.setText(pending_items.get(position).customstatement1);


            } else if (type == 3) {

                holder.timetext1.setVisibility(View.GONE);
                holder.timetext2.setVisibility(View.GONE);
                holder.timetext3.setVisibility(View.GONE);
                holder.date.setVisibility(View.GONE);
                holder.date1.setVisibility(View.GONE);
                holder.date2.setVisibility(View.GONE);


                holder.timetext.setVisibility(View.VISIBLE);
                holder.timetext.setText(ff);
                holder.timetext.setPadding(pending_items.get(position).paddingleft, widt, right, 0);
                holder.titletext.setTextColor(R.color.raccent);
                holder.fromtime.setVisibility(View.GONE);
                holder.fromdate.setVisibility(View.GONE);
                holder.fromm.setVisibility(View.GONE);
                holder.too.setVisibility(View.GONE);
                holder.todate.setVisibility(View.GONE);
                holder.totime.setVisibility(View.GONE);
                holder.time2right.setVisibility(View.GONE);
                holder.timetoright.setVisibility(View.GONE);

                holder.date3.setVisibility(View.VISIBLE);
                holder.date3.setText(pending_items.get(position).customstatement1);
            }

            holder.pending_cards.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // maketext.makeText("clicked of positionn "+position + " of type "+type);
                    switch (type) {
                        case 0:
                            Intent showintent = new Intent(context, ShowDecision.class);

                            showintent.putExtra("oid", pending_items.get(position).oid);
                            context.startActivity(showintent);
                            break;
                        case 1:

                            Intent showintent1 = new Intent(context, ShowEvent.class);

                            showintent1.putExtra("oid", pending_items.get(position).oid);
                            context.startActivity(showintent1);
                            break;

                        case 2:
                            Intent showintent2 = new Intent(context, ShowList.class);

                            showintent2.putExtra("oid", pending_items.get(position).oid);
                            context.startActivity(showintent2);
                            break;

                        case 3:
                            Intent showintent3 = new Intent(context, ShowRemainder.class);

                            showintent3.putExtra("oid", pending_items.get(position).oid);
                            Log.d("oid", "oid is");
                            context.startActivity(showintent3);
                            break;
                    }
                }
            });


            holder.pending_cards.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    ActionMode actionMode = (activity).startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            mode.setTitle("Selected");

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
                    //  v.setSelected(true);
                    //v.setClickable(false);


                    return true;
                }
            });

            Log.d("current position", "" + currentpositionn);
            if (position > currentpositionn) {
                setAnimator(holder.pending_cards);

                //recycleranims.animate(holder,true);
            } else {
                // recycleranims.animate(holder,false);
                setAnimator2(holder.pending_cards);
            }
            currentpositionn = position;


        } else if (choice == 7) {

            Log.d("pending ", "the choice is " + choice);
            String ff;
            holder.titletext.setText(pending_items.get(position).title);

            ff = pending_items.get(position).time;
            holder.timetext.setText(ff);

            final int type = pending_items.get(position).type;
            if (type == 0) {


            }

            if (position > currentpositionn) {
                setAnimator(holder.pending_cards);

                //recycleranims.animate(holder,true);
            } else {
                // recycleranims.animate(holder,false);
                setAnimator2(holder.pending_cards);
            }
            currentpositionn = position;


        } else if ((choice == 3) || (choice == 5) || (choice == 6)) {
            //all pending decision choice
            String ff;
            holder.titletext.setText(custom_items.get(position).title);
            ff = custom_items.get(position).time;
            if (ff != null)
                ff = ff.replace('.', ':');

            holder.timetext.setText(ff);
            holder.timetext.setPadding(custom_items.get(position).paddingleft, widt, right, 0);
            holder.customstatement.setText(custom_items.get(position).customstatement1);
            holder.fromdate.setText(custom_items.get(position).date);

            holder.pending_cards.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (choice == 3) {
                        Intent showintent = new Intent(context, ShowDecision.class);

                        showintent.putExtra("oid", custom_items.get(position).id);
                        context.startActivity(showintent);

                    } else if (choice == 5) {
                        Intent showintent2 = new Intent(context, ShowList.class);

                        showintent2.putExtra("oid", custom_items.get(position).id);
                        context.startActivity(showintent2);
                    } else if (choice == 6) {
                        Intent showintent2 = new Intent(context, ShowRemainder.class);

                        showintent2.putExtra("oid", custom_items.get(position).id);
                        context.startActivity(showintent2);
                    }
                }
            });


            if (position > currentpositionn) {
                setAnimator(holder.pending_cards);

                //recycleranims.animate(holder,true);
            } else {
                // recycleranims.animate(holder,false);
                setAnimator2(holder.pending_cards);
            }
            currentpositionn = position;


        } else if (choice == 4) {

            String ff;
            holder.titletext.setText(custom_items2.get(position).title);
            ff = custom_items2.get(position).time;
//            ff = ff.replace('.', ':');
            holder.timetext.setText(ff);
            holder.timetext.setPadding(custom_items2.get(position).paddingleft, widt, right, 0);


            int temp = custom_items2.get(position).temp1;
            if (temp == 1) {
                holder.fromtime.setVisibility(View.GONE);
                holder.fromdate.setVisibility(View.GONE);
                holder.fromm.setVisibility(View.GONE);
                holder.too.setVisibility(View.GONE);
                holder.todate.setVisibility(View.GONE);
                holder.totime.setVisibility(View.GONE);
                holder.time2right.setVisibility(View.GONE);
                holder.timetoright.setVisibility(View.GONE);


                holder.customstatement.setVisibility(View.VISIBLE);
                holder.customstatement.setText(custom_items2.get(position).customstatement1);

                holder.date.setVisibility(View.VISIBLE);
                holder.date1.setVisibility(View.GONE);
                holder.date2.setVisibility(View.GONE);
                holder.date.setText(custom_items2.get(position).date);
                Log.d("recycler_data", "whole day date is " + custom_items2.get(position).date + " of positon " + position);
            } else {


                String fromdatee = custom_items2.get(position).customstatement1;

                String todatee = custom_items2.get(position).customstatement2;
                if (fromdatee.equals(todatee)) {
                    holder.customstatement.setVisibility(View.GONE);
                    holder.fromtime.setVisibility(View.GONE);

                    holder.too.setVisibility(View.GONE);
                    holder.todate.setVisibility(View.GONE);
                    holder.totime.setVisibility(View.GONE);

                    holder.time2right.setVisibility(View.VISIBLE);
                    holder.timetoright.setVisibility(View.VISIBLE);
                    holder.fromdate.setVisibility(View.VISIBLE);
                    holder.fromm.setVisibility(View.VISIBLE);

                    ff = custom_items2.get(position).customstatement3;
                 //   ff = ff.replace('.', ':');
                    holder.fromdate.setText(ff);

                    ff = custom_items2.get(position).customstatement4;
              //      ff = ff.replace('.', ':');
                    holder.time2right.setText(ff);

                    holder.date2.setVisibility(View.VISIBLE);
                    holder.date1.setVisibility(View.GONE);
                    holder.date.setVisibility(View.GONE);
                    holder.date2.setText(custom_items2.get(position).date);


                } else {

                    holder.time2right.setVisibility(View.GONE);
                    holder.timetoright.setVisibility(View.GONE);


                    holder.customstatement.setVisibility(View.GONE);

                    holder.fromtime.setVisibility(View.VISIBLE);
                    holder.fromdate.setVisibility(View.VISIBLE);
                    holder.fromm.setVisibility(View.VISIBLE);
                    holder.too.setVisibility(View.VISIBLE);
                    holder.todate.setVisibility(View.VISIBLE);
                    holder.totime.setVisibility(View.VISIBLE);

                    holder.fromdate.setText(fromdatee);
                    //                    Log.d("pending_recycler_data", "" + custom_items2.get(position).customstatement1 + " " + custom_items2.get(position).customstatement2 + " " + pending_items.get(position).customstatement3 + " " + pending_items.get(position).customstatement4);
                    ff = custom_items2.get(position).customstatement3;
             //       ff = ff.replace(".", ":");
                    holder.fromtime.setText(ff);
                    holder.todate.setText(todatee);
                    ff = custom_items2.get(position).customstatement4;
              //      ff = ff.replace('.', ':');
                    holder.totime.setText(ff);

                    holder.date1.setVisibility(View.VISIBLE);
                    holder.date.setVisibility(View.GONE);
                    holder.date2.setVisibility(View.GONE);
                    holder.date1.setText(custom_items2.get(position).date);

                }


            }

            holder.pending_cards.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent showintent2 = new Intent(context, ShowEvent.class);

                    showintent2.putExtra("oid", custom_items2.get(position).id);
                    context.startActivity(showintent2);
                }
            });

            if (position > currentpositionn) {
                setAnimator(holder.pending_cards);

                //recycleranims.animate(holder,true);
            } else {
                // recycleranims.animate(holder,false);
                setAnimator2(holder.pending_cards);
            }
            currentpositionn = position;

        }


    }

    public void setAnimator(View view) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.recyclerslide);

        view.startAnimation(anim);
    }

    public void setAnimator2(View view) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.recyclerslidedown);

//        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        if (choice == 1)
            return pending_items.size();
        else if (choice == 2)
            return custom_items.size();
        else if ((choice == 3) || (choice == 5) || (choice == 6))
            return custom_items.size();
        else if (choice == 4)
            return custom_items2.size();
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void remove(int position) {
        //if(choice==1)
        {
            pending_items.remove(position);
            notifyItemRemoved(position);
        }

    }


    public class ViewHold extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public CardView pending_cards;
        TextView titletext, customstatement, fromm, too, fromdate, todate, totime, fromtime, time2right, timetoright;
        TextView timetext, date, date1, date2, timetext1, timetext2, timetext3, date3;


        ViewHold(View itemView) {

            super(itemView);
            Log.d("pending", "called using viewhold " + choice);
            if (choice == 1) {
                titletext = (TextView) itemView.findViewById(R.id.pending_title);
                timetext = (TextView) itemView.findViewById(R.id.time);
                timetext1 = (TextView) itemView.findViewById(R.id.time1);
                timetext2 = (TextView) itemView.findViewById(R.id.time2);
                timetext3 = (TextView) itemView.findViewById(R.id.time3);
                customstatement = (TextView) itemView.findViewById(R.id.pendingstatement);
                fromm = (TextView) itemView.findViewById(R.id.ttimefrom);
                fromdate = (TextView) itemView.findViewById(R.id.tfromdate);
                fromtime = (TextView) itemView.findViewById(R.id.tfromtime);
                too = (TextView) itemView.findViewById(R.id.ttimeto);
                todate = (TextView) itemView.findViewById(R.id.ttodate);
                totime = (TextView) itemView.findViewById(R.id.ttotime);
                time2right = (TextView) itemView.findViewById(R.id.ttime2right);
                timetoright = (TextView) itemView.findViewById(R.id.ttimetoright);
                date = (TextView) itemView.findViewById(R.id.pendingstatement);
                date1 = (TextView) itemView.findViewById(R.id.pendingstatement1);
                date2 = (TextView) itemView.findViewById(R.id.pendingstatement2);
                date3 = (TextView) itemView.findViewById(R.id.pendingstatement3);

                pending_cards = (CardView) itemView.findViewById(R.id.pending_cards);

            } else if (choice == 7) {

                Log.d("pending", "the choice from outside viewholder is " + choice);
                titletext = (TextView) itemView.findViewById(R.id.pending_title);
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

                pending_cards = (CardView) itemView.findViewById(R.id.pending_cards);
            } else if (choice == 2) {
                titletext = (TextView) itemView.findViewById(R.id.textlistitem);
            } else if ((choice == 3) || (choice == 5) || (choice == 6)) {
                titletext = (TextView) itemView.findViewById(R.id.pending_title);
                timetext = (TextView) itemView.findViewById(R.id.time);
                customstatement = (TextView) itemView.findViewById(R.id.pendingstatement);
                fromdate = (TextView) itemView.findViewById(R.id.date);
                pending_cards = (CardView) itemView.findViewById(R.id.pending_cards);
            } else if (choice == 4) {
                titletext = (TextView) itemView.findViewById(R.id.pending_title);
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


                pending_cards = (CardView) itemView.findViewById(R.id.pending_cards);
            }

            // itemView.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.setHeaderTitle("Select The Action");
            int id = 0;
            String temp;
            int place = getLayoutPosition();
            Log.d("pending_recyclerdata", "calling context " + getLayoutPosition() + " " + postion);
            if (choice == 1) {

                temp = Integer.toString(pending_items.get(place).type).concat(Integer.toString(pending_items.get(place).oid));
                id = Integer.parseInt(temp);
            } else if (choice == 3 || choice == 5 || choice == 6) {

                temp = Integer.toString(choice).concat(Integer.toString(custom_items.get(place).id));
                id = Integer.parseInt(temp);
            } else if (choice == 4) {
                temp = Integer.toString(choice).concat(Integer.toString(custom_items2.get(place).id));
                id = Integer.parseInt(temp);
            }
            if (choice == 5) {
                menu.add(id, 3, 1, R.string.edit);
            } else if (choice == 1) {
                if (pending_items.get(place).type == 2) {
                    menu.add(id, 3, 1, R.string.edit);
                }
            }
            menu.add(id, 1, 1, R.string.share);//groupId, itemId, order, title
            menu.add(id, 2, 2, R.string.delete);


        }


    }
}


