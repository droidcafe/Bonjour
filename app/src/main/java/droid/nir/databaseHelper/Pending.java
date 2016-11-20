package droid.nir.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import droid.nir.testapp1.noveu.Util.Log;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import droid.nir.testapp1.R;
import droid.nir.testapp1.toast;

/**
 * Created by user on 7/4/2015.
 */
public class Pending {


   DatabaseCreater databaseCreater;
    Context context;
    Today today;
    SQLiteDatabase db;
    String[][] columnNames,columnTypes;
    String[] tableName;
    List<String> prolist,conlist,extralist;
   toast maketext;
    public Pending(Context context)
    {
      this.context =  context ;

        databaseCreater = new DatabaseCreater(context);
        prolist = Collections.EMPTY_LIST;
        conlist = Collections.EMPTY_LIST;
        extralist = Collections.EMPTY_LIST;
        maketext = new toast(context);
        today = new Today(context);

    }

    public SQLiteDatabase settingDatabase()
    {

        String columnName[][] ={
                {"_id" ,"title" ,"desp","leaning","decide","nopros","nocons","decisiontype","custom","extas","implevel","date","timehr","timemin","notificationtype","done"},
                {"_id","pid","description"},
                {"_id","pid","pro"},
                {"_id","pid","con"},
                {"_id","pid","custom"},
                {"_id","pid","extratype","extra"}
        };
        columnNames = columnName;
        String columnTypes[][] ={
                {"INTEGER","VARCHAR(255)","INTEGER","INTEGER","INTEGER","INTEGER","INTEGER","VARCHAR(255)","INTEGER","INTEGER","INTEGER","VARCHAR(255)","INTEGER","INTEGER","INTEGER","INTEGER"},
                {"INTEGER","INTEGER","VARCHAR(5000)"},
                {"INTEGER","INTEGER","VARCHAR(1000)"},
                {"INTEGER","INTEGER","VARCHAR(1000)"},
                {"INTEGER","INTEGER","VARCHAR(255)"},
                {"INTEGER","INTEGER","VARCHAR(255)","VARCHAR(1000)"}
        };
        this.columnTypes = columnTypes;
        int columnNo[]={16,3,3,3,3,4};
        String tableName[]={"pending","desp","pros","cons","customs","extras"};
        this.tableName = tableName;


        databaseCreater.intialise(tableName,columnNo,columnName,columnTypes,5);
        db =databaseCreater.getWritableDatabase();
        return db;
    }

    public int delete(SQLiteDatabase db, int tableno,String selection ,String[] whereargs)
    {
        if(db!=null)
            return databaseCreater.deleterow(db,tableName[tableno],selection,whereargs);

        else
            return -1;
    }


    public Cursor select(SQLiteDatabase db, int tableno, int[] columnno,String selection, String[] selectionArgs, String groupby,String having,String orderby)
        {
            String[] reqcolumns = new String[columnno.length];
          //  reqcolumns = null;
            for (int i = 0; i < columnno.length; i++) {
                reqcolumns[i] = columnNames[tableno][columnno[i]];
            }

            Cursor tempcursor = databaseCreater.select(db,tableName[tableno],reqcolumns,selection,selectionArgs,groupby,having,orderby);
            return tempcursor;

        }

    public  void insert(String values[],int intvalues[],List<String> prolist,List<String> conlist,List<String> extralist, SQLiteDatabase db, int[] extrasArray,Calendar calendar)
    {
        Asyncinsert asyncinsert = new Asyncinsert(values,intvalues,prolist,conlist,extralist,db,extrasArray,calendar);
        asyncinsert.execute();
    }

    public class Asyncinsert extends AsyncTask<Void,Void,Void>
    {
        String[] values;
        int[] intvalues,extrasArray;
        List<String> conlist,prolist,extralist;
        SQLiteDatabase db;
        Calendar calendar;

        Asyncinsert(String values[],int intvalues[],List<String> prolist,List<String> conlist,List<String> extralist, SQLiteDatabase db, int[] extrasArray,Calendar calendar)
        {
            this.values = values;
            this.intvalues = intvalues;
            this.conlist = conlist;
            this.prolist = prolist;
            this.extralist = extralist;
            this.db = db;
            this.extrasArray = extrasArray;
            this.calendar = calendar;
        }

        @Override
        protected Void doInBackground(Void... params) {

            asyncinsert(values,intvalues,prolist,conlist,extralist,db,extrasArray,calendar);
            return null;
        }
    }

    public  void asyncinsert(String values[],int intvalues[],List<String> prolist,List<String> conlist,List<String> extralist, SQLiteDatabase db, int[] extrasArray,Calendar calendar)
    {
        int temp =0;

        Calendar tempcalendar2 = Calendar.getInstance();
        int month =tempcalendar2.get(Calendar.MONTH) +1;
        String todaydate = tempcalendar2.get(Calendar.DAY_OF_MONTH)+"/"+month+"/"+tempcalendar2.get(Calendar.YEAR);

        // boolean issamedate = DateUtils.isToday(fromcalendar.getTimeInMillis());
        boolean issamedate = todaydate.equals(values[2]);
        Log.d("todolist", "is same date " + issamedate);
        if(issamedate)
        {
            //add to today table
            intvalues[11]=1;
           temp = 1;

        }
        ContentValues contentValues= new ContentValues();
        int j,k,l;
        j=0;k=0;
        for(int i=1;i<16;i++)
        {
            if(columnTypes[0][i].equals("INTEGER"))
            {
                contentValues.put(columnNames[0][i],intvalues[j++]);

                if(i==15)
                {
                    Log.d("events","done actually inserted" +intvalues[j-1]+" and j is "+j);
                }
            }
            else
            {
                contentValues.put(columnNames[0][i],values[k++]);
            }
        }
        Log.d("pending", "main table" + databaseCreater.insert(db, contentValues, tableName[0]));


        String[] reqcolumnNames = {columnNames[0][0]};
        String selection = ""+columnNames[0][7] + " = ? and "+ columnNames[0][1] + " = ? and "+columnNames[0][11] +" = ? " ;
        String selectionArgs[] = {values[1],values[0],values[2]};
        Cursor tempcursor = databaseCreater.select(db,tableName[0],reqcolumnNames,selection,selectionArgs,null,null,null);
        int pid = 0;
        while(tempcursor.moveToNext())
        {
            pid = tempcursor.getInt(tempcursor.getColumnIndex(columnNames[0][0]));
        }
        Log.d("pending","pid is "+ pid);

        if(temp==1)
        {
            String statement="";
            int found = 0;

            if(values[1].equals("Custom"))
            {
                if(intvalues[5]==1)
                {
                    statement =context.getResources().getString(R.string.shouldi)+" " +values[3] +" ?";
                    found= 1;
                }
            }

            else
            {
                String[] tempar = context.getResources().getStringArray(R.array.decision_arrays);
                String[] tempar1 = context.getResources().getStringArray(R.array.decision_arrays1);

                int yy=0;
                while((yy<tempar1.length)&&!(values[1].equals(tempar1[yy])))
                {
                    yy++;
                }
                if (yy==tempar1.length)
                    statement = context.getResources().getString(R.string.shouldi) + " "+values[1] +" ?";
                else {
                    statement = context.getResources().getString(R.string.shouldi) + " "+tempar[yy] +" ?";
                }

                found=1;
            }

            if(found!=1)
            {
                if(intvalues[0]==1)
                {
                    statement = values[4];
                    found=1;
                }
                else if((prolist.size()>0)||(conlist.size()>0)) {
                    if(intvalues[3]>intvalues[4])
                    {
                        statement = "Pros outnumber the cons!";
                        found=1;
                    }
                    else if(intvalues[3]<intvalues[4]){
                        statement =  "Sorry!Cons outnumber the pros";
                    }
                    else{
                        statement = "Equal number of pros and cons!";
                    }

                }
                else
                {
                    statement = "No pros and cons available";
                }
            }
            int isalarm=0,isnotify =0;

            if(intvalues[10]==1||intvalues[10]==2)
                isalarm =1;
            else
            isalarm=0;
            if(intvalues[10]==0||intvalues[10]==2)
                isnotify =1;
            else
            isnotify=0;
            Log.d("addpending","notif and alarm val"+isalarm +" "+isnotify);

            String stringval[] = {values[0],statement};
            int insertval[] = {0,pid,isalarm,isnotify,intvalues[8],intvalues[9]};
            Log.d("pending","inserting to today via today ");
            Log.d("pending", "id of pending  s " +  pid);
            today.settingDatabase();
            today.insert(stringval,insertval);

        }

        if(intvalues[0]==1)
        {
            ContentValues tempcontentvalues = new ContentValues();
            tempcontentvalues.put(columnNames[1][1],pid);
            tempcontentvalues.put(columnNames[1][2],values[4]);

            Log.d("pending", "desp table" + databaseCreater.insert(db, tempcontentvalues, tableName[1]));
        }
        if(intvalues[3]>0) {

            for (int i = 0; i < prolist.size(); i++) {
                ContentValues tempcontentvalues = new ContentValues();
                tempcontentvalues.put(columnNames[2][1], pid);
                tempcontentvalues.put(columnNames[2][2], prolist.get(i));

                Log.d("pending", "pro table" + prolist.get(i) + " " + databaseCreater.insert(db, tempcontentvalues, tableName[2]));
            }

            //there are pros

            //insert pros here;
        }
        if(intvalues[4]>0)
        {
            for (int i = 0; i < conlist.size(); i++) {
                ContentValues tempcontentvalues = new ContentValues();
                tempcontentvalues.put(columnNames[3][1], pid);
                tempcontentvalues.put(columnNames[3][2], conlist.get(i));

                Log.d("pending", "con table" + conlist.get(i) + " " + databaseCreater.insert(db, tempcontentvalues, tableName[3]));
            }

        }

       if(intvalues[5]==1)
       {
           ContentValues tempcontentvalues = new ContentValues();
           tempcontentvalues.put(columnNames[4][1], pid);
           tempcontentvalues.put(columnNames[4][2], values[3]);

           Log.d("pending", "custom table " + values[4] + " " + databaseCreater.insert(db, tempcontentvalues, tableName[4]));

       }
        if(intvalues[6]>0)
        {

            int kk=0;
            for (int i = 0; i <7; i++) {

                String extratype = null;
                Log.d("pending", "aray " + i + "  " + extrasArray[i]);
                if (extrasArray[i] == 1) {
                    switch (i) {
                        case 0:
                            extratype = "phone";
                            break;
                        case 1:
                            extratype = "website";
                            break;
                        case 2:
                            extratype = "mailid";
                            break;
                        case 3:
                            extratype = "price";
                            break;
                        case 4:
                            extratype = "address";
                            break;
                        case 5:
                            extratype = "fax";
                            break;
                        case 6:
                            extratype = "shopname";
                            break;

                    }
                    ContentValues tempcontentvalues = new ContentValues();
                    tempcontentvalues.put(columnNames[5][1], pid);
                    tempcontentvalues.put(columnNames[5][2], extratype);
                    tempcontentvalues.put(columnNames[5][3],extralist.get(kk++));
                    Log.d("pending", "extra table " + extratype + "  " + extralist.get(kk - 1) + "  " + databaseCreater.insert(db, tempcontentvalues, tableName[5]));

                }

            }
        }

    }

}
