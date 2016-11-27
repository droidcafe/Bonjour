package droid.nir.testapp1;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import droid.nir.databaseHelper.Pending;
import droid.nir.databaseHelper.Today;
import droid.nir.testapp1.noveu.Util.FirebaseUtil;
import droid.nir.testapp1.noveu.Util.Log;
import droid.nir.testapp1.noveu.social.share.Master;

public class ShowDecision extends ActionBarActivity {

    TextView title, desp, procon,proos,coons, timemin, remainder, phone, phoneno, web, website, mail, mailid, price, pric, add, address, fax, faxno, shop, shopname;

    CardView titlecard, despcard, proconcard, timecard, extracard;
    toast maketext;
    SQLiteDatabase db;
    String  titletext,timetext,desptext;
    int isdesp;
    Pending pending;
    int oid;
    List<String> prolist,conlist;
    ArrayAdapter<String> mProArrayAdapter,mConArrayAdapter;
    LinearLayout protextid,contextid;
    FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.pendingdecisions);
        setContentView(R.layout.activity_show_decision);

        maketext = new toast(this);
        pending = new Pending(this);
        Intent showintent = getIntent();
        if (showintent != null) {

            oid = showintent.getExtras().getInt("oid");

            // maketext.makeText("The passed id and oid is  "+oid);

            title = (TextView) findViewById(R.id.etitle);
            desp = (TextView) findViewById(R.id.ddesp);
            procon = (TextView) findViewById(R.id.dcomp);
            proos= (TextView) findViewById(R.id.dpros);
            coons= (TextView) findViewById(R.id.dcons);
            timemin = (TextView) findViewById(R.id.dtimemin);
            remainder = (TextView) findViewById(R.id.dremaindertype);
            phone = (TextView) findViewById(R.id.dphone);
            phoneno = (TextView) findViewById(R.id.dphoneno);
            web = (TextView) findViewById(R.id.dweb);
            website = (TextView) findViewById(R.id.dwebsite);
            mail = (TextView) findViewById(R.id.dmail);
            mailid = (TextView) findViewById(R.id.dmailid);
            pric = (TextView) findViewById(R.id.dpric);
            price = (TextView) findViewById(R.id.dprice);
            add = (TextView) findViewById(R.id.dadd);
            address = (TextView) findViewById(R.id.dadrress);
            fax = (TextView) findViewById(R.id.dfax);
            faxno = (TextView) findViewById(R.id.dfaxno);
            shop = (TextView) findViewById(R.id.dshop);
            shopname = (TextView) findViewById(R.id.dshopname);


            protextid = (LinearLayout) findViewById(R.id.protextid);
           contextid = (LinearLayout) findViewById(R.id.contextid);

            titlecard = (CardView) findViewById(R.id.pending_cards);
            timecard = (CardView) findViewById(R.id.time_cards);
            extracard = (CardView) findViewById(R.id.extras_cards);
            despcard = (CardView) findViewById(R.id.description_cards);
            proconcard = (CardView) findViewById(R.id.procons_cards);

            db = pending.settingDatabase();
            prolist = new ArrayList<>();

            conlist = new ArrayList<>();


            setupdecision();
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            FirebaseUtil.recordScreenView(this,"show decision",mFirebaseAnalytics);


        } else {
            maketext.makeText("Sorry!Please Try Again!Failed Pipe");
        }

    }

    private void setupdecision() {
        AsyncFill asyncFill = new AsyncFill();
        asyncFill.execute();
    }

    class AsyncFill extends AsyncTask<Void,String,Void>
    {

        String compare,remaindertext;
        int nopros,nocons,remtype,isextras,ispresent;
        timecorrection timecorrection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            timecorrection = new timecorrection();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String selection = " _id = "+oid;
            String selectionn = " pid = "+oid;
            int column[] = {2};
            int[] columnno = {1,2,5,6,9,11,12,13,14,15};

            Cursor cursor;
            cursor = pending.select(db,0,columnno,selection , null,null,null,null);
           ispresent=cursor.getCount();
            while (cursor.moveToNext()) {
                titletext = cursor.getString(cursor.getColumnIndex("title"));

                nopros = cursor.getInt(cursor.getColumnIndex("nopros"));
                nocons = cursor.getInt(cursor.getColumnIndex("nocons"));
                isdesp = cursor.getInt(cursor.getColumnIndex("desp"));
                remtype = cursor.getInt(cursor.getColumnIndex("notificationtype"));
                isextras = cursor.getInt(cursor.getColumnIndex("extas"));

                switch (remtype)
                {
                    case 0:
                        remaindertext = getResources().getString(R.string.anotonly);
                        break;
                    case 1:
                        remaindertext = getResources().getString(R.string.aalaonly);
                        break;
                    case 2:
                        remaindertext = getResources().getString(R.string.aalarmnot);
                        break;


                }


                if(!((nopros==0)&&(nocons==0)))
                {
                    if(nopros>nocons)
                        compare=  getResources().getString(R.string.pprobig);
                    else if(nopros<nocons)
                        compare= getResources().getString(R.string.pconbig);
                    else
                        compare = getResources().getString(R.string.pprosame);
                }

                if(isdesp==1)
                {
                    Cursor tempcursor =  pending.select(db,1,column,selectionn , null,null,null,null);
                    while (tempcursor.moveToNext())
                    {
                        desptext = tempcursor.getString(tempcursor.getColumnIndex("description"));
                    }
                }
                if(nopros>0)
                {

                    Cursor tempcursor =  pending.select(db,2,column,selectionn , null,null,null,null);
                    Log.d("show decision","no of pros ar "+tempcursor.getCount());
                    while (tempcursor.moveToNext())
                    {
                        String current = tempcursor.getString(tempcursor.getColumnIndex("pro"));
                        Log.d("showdecision",""+current);
                        prolist.add(current);
                    }
                }
                if(nocons>0)
                {
                    Cursor tempcursor =  pending.select(db,3,column,selectionn , null,null,null,null);
                    Log.d("show decision","no of cons ar "+tempcursor.getCount());
                    while (tempcursor.moveToNext())
                    {
                        String current = tempcursor.getString(tempcursor.getColumnIndex("con"));
                        Log.d("showdecision",""+current);
                        conlist.add(current);
                    }
                }

                if(isextras>0)
                {
                    int coll[] = {2,3};
                    Cursor tempcursor =  pending.select(db,5,coll,selectionn , null,null,null,null);
                    while (tempcursor.moveToNext())
                    {
                       String type = tempcursor.getString(tempcursor.getColumnIndex("extratype"));
                        String extra = tempcursor.getString(tempcursor.getColumnIndex("extra"));
                        String ext[] =  { type,extra};
                        publishProgress(ext);

                    }
                }

                 int min = cursor.getInt(cursor.getColumnIndex("timemin"));
                int hr  = cursor.getInt(cursor.getColumnIndex("timehr")) ;

                timetext = timecorrection.formatime(hr,min);


            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);


            extracard.setVisibility(View.VISIBLE);
            Log.d("show decision", "" + values[0] + " is " + values[1]);
            if(values[0].equals("phone"))
            {
                phone.setVisibility(View.VISIBLE);
                phoneno.setVisibility(View.VISIBLE);
                phoneno.setText(values[1]);
            }
            else if(values[0].equals("website"))
            {
                web.setVisibility(View.VISIBLE);
                website.setVisibility(View.VISIBLE);
                website.setText(values[1]);
            }
            else if(values[0].equals("mailid"))
            {
                mail.setVisibility(View.VISIBLE);
                mailid.setVisibility(View.VISIBLE);
                mailid.setText(values[1]);

            }
            else if(values[0].equals("price"))
            {
               pric.setVisibility(View.VISIBLE);
                price.setVisibility(View.VISIBLE);
                price.setText(values[1]);
            }
            else if(values[0].equals("address"))
            {
                add.setVisibility(View.VISIBLE);
                address.setVisibility(View.VISIBLE);
               address.setText(values[1]);
            }
            else if(values[0].equals("fax"))
            {
                fax.setVisibility(View.VISIBLE);
                faxno.setVisibility(View.VISIBLE);
                faxno.setText(values[1]);
            }
            else if(values[0].equals("shopname"))
            {
                shop.setVisibility(View.VISIBLE);
               shopname.setVisibility(View.VISIBLE);
                shopname.setText(values[1]);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(ispresent==0)
            {

                titlecard.setVisibility(View.GONE);
                despcard.setVisibility(View.GONE);
                proconcard.setVisibility(View.GONE);
                timecard.setVisibility(View.GONE);
                extracard.setVisibility(View.GONE);

                TextView hiddentext = (TextView)findViewById(R.id.hiddentext);
                hiddentext.setVisibility(View.VISIBLE);
                hiddentext.setText(R.string.nodecision);


            }
            else {
                title.setText(titletext);
                timemin.setText(timetext);
                remainder.setText(remaindertext);

                if(isdesp==1)
                {
                    despcard.setVisibility(View.VISIBLE);
                    desp.setText(desptext);

                }
                if((nopros>0)||(nocons>0))
                {
                    proconcard.setVisibility(View.VISIBLE);
                    procon.setText(compare);

                    if(nopros>0)
                    {
                        proos.setVisibility(View.VISIBLE);
                        protextid.setVisibility(View.VISIBLE);
                    /*prolists.setVisibility(View.VISIBLE);
                    Log.d("showdecision", "" + prolist.size());
                    mProArrayAdapter.notifyDataSetChanged();*/

                        for(int i=0;i<prolist.size();i++)
                        {
                            TextView textView = new TextView(getApplicationContext());
                            textView.setText(prolist.get(i));
                            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            textView.setLayoutParams(params);

                            textView.setTextAppearance(getApplicationContext(), R.style.inner_text_largee);
                            textView.setTextSize(18);
                            textView.setPadding(35, 5, 10, 3);
                            protextid.addView(textView);
                        }


                    }
                    if(nocons>0)
                    {
                        coons.setVisibility(View.VISIBLE);
                        contextid.setVisibility(View.VISIBLE);
                        for(int i=0;i<conlist.size();i++)
                        {
                            TextView textView = new TextView(getApplicationContext());
                            textView.setText(conlist.get(i));
                            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            textView.setLayoutParams(params);
                            textView.setTextAppearance(getApplicationContext(), R.style.inner_text_largee);
                            textView.setTextSize(18);
                            textView.setPadding(35, 5, 10, 3);
                            contextid.addView(textView);
                        }
                    }


                }


            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_decision, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == R.id.action_share) {
            String list= titletext +"\n" +getResources().getString(R.string.rdecidedby)+" " +timetext;
            if (isdesp == 1) {
                list = list.concat("\n"+desptext);
            }
            if(prolist.size()>0){
                String pro = prolist.toString();
                pro = pro.replace(",","\n");
                list = list.concat("\n "+getResources().getString(R.string.pprosare)+" \n"+pro);
            }
            if(conlist.size()>0)
            {
                String con = conlist.toString();
                con = con.replace(",","\n");
                list = list.concat("\n "+getResources().getString(R.string.pconsare)+" \n"+con);
            }

            Bundle fire_share = new Bundle();
            fire_share.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"decision");
            fire_share.putString(FirebaseAnalytics.Param.ITEM_ID, titletext);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,fire_share);

            Intent shareIntent = Master.share(titletext,list);
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.shareusing)));
        }

        else if (id == R.id.action_delete) {
            DialogFragment alertDialog = MyDialogFragment.newInstance(getResources().getString(R.string.delete)+" " + titletext, 4, getResources().getString(R.string.ddeletewarn)+" ");
            alertDialog.show(getFragmentManager(), "dialogs");
        }


        return super.onOptionsItemSelected(item);
    }

    public void doPositiveClick() {

        int x = deletelistitem();
        if (x > 0) {
            maketext.makeText( getResources().getString(R.string.deletionsucess));
            SharedPreferences sharedPreferences = getSharedPreferences("sharedprefs", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("ischanged", 1);
            editor.apply();

            Bundle fireBundle = new Bundle();
            fireBundle.putString("name",titletext);
            mFirebaseAnalytics.logEvent(FirebaseUtil.decision_delete,fireBundle);

            finish();
        } else {
            maketext.makeText( getResources().getString(R.string.deletionfailed));
        }
    }

    private int deletelistitem() {

        String selection = " _id = " + oid;
        int xx = pending.delete(db, 0, selection, null);

        selection = " pid = " + oid;
        pending.delete(db, 1, selection, null);
        pending.delete(db, 2, selection, null);
        pending.delete(db, 3, selection, null);
        pending.delete(db, 4, selection, null);
        pending.delete(db, 5, selection, null);


        selection = "type = 0 and oid = " + oid;

        Today today = new Today(ShowDecision.this);
        today.settingDatabase();
        today.deleterow(selection, 0, null, db);

        return xx;
    }

    public void doNegativeClick() {
    }
}
