package droid.nir.testapp1;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.Attributes;

import droid.nir.alarmManager.*;


public class trialActivity{}/* extends ActionBarActivity implements View.OnClickListener,DialogueCreator.DialogListener {

    List<String> arraylist;
    List<pro_data> pro_dataList;
    EditText editText;
    Button btn,startservice,stopservice;
    TextRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout protextid,hh;
    TextView dialogcreator;
    NotificationManager notificationManager;
    int height;
    String array[];
    toast maketext;
    Context context;
    Button calendr1,calendar2,calander3,calendar4,calendar5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        dialogcreator = (TextView) findViewById(R.id.dialogselector);
        maketext = new toast(this);
     //   XmlPullParser parser = resources.getXml(myResouce);
       // AttributeSet attributes = Xml.asAttributeSet(parser);
       calendr1= (Button)findViewById(R.id.calender1);
        calendar2= (Button)findViewById(R.id.calender2);
        calander3= (Button)findViewById(R.id.calender3);
        calendar4 = (Button) findViewById(R.id.calender4);
        calendar5 = (Button) findViewById(R.id.calender5);
        startservice = (Button)findViewById(R.id.servicestart);
        stopservice = (Button)findViewById(R.id.stpservice);

        calendr1.setOnClickListener(this);
        startservice.setOnClickListener(this);
        stopservice.setOnClickListener(this);
        calendar2.setOnClickListener(this);
        calander3.setOnClickListener(this);
        dialogcreator.setOnClickListener(this);
        calendar4.setOnClickListener(this);
        calendar5.setOnClickListener(this);
        array = getResources().getStringArray(R.array.extras_arrays);



    }

    public void setUplist()
    {

    }

    public void setUpRecycler()
    {
       /* layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new TextRecyclerAdapter(this,pro_dataList);
        recyclerView.setAdapter(recyclerAdapter);
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trial, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.calender1:
                startActivity(new Intent(this,Add_Pending.class));
                break;

            case R.id.calender2:

                startActivity(new Intent(this, Add_Event2.class));


                break;
            case R.id.calender3:


                break;


            case R.id.calender4:
                startActivity(new Intent(this,Add_Todo_list.class));

                break;

            case R.id.calender5:
                startActivity(new Intent(this,floating_trials.class));

                break;
            case R.id.dialogselector:

                DialogFragment dialogFragment = DialogueCreator.newInstance(R.string.dialogtitle,"My Dialogue",R.array.extras_arrays);
               dialogFragment.show(getFragmentManager(), "dialog");
                break;
            case R.id.servicestart:
                Intent i = new Intent(this, droid.nir.alarmManager.AlarmService.class);

                Log.d("trial activity","startin service");
                i.putExtra("name", "SurvivingwithAndroid");

                this.startService(i);
                break;

            case R.id.stpservice:

                Intent ii = new Intent(this, droid.nir.alarmManager.AlarmService.class);

               this.stopService(ii);

                break;

                /*DialogueCreator dialogueCreator = new DialogueCreator();

                Bundle bundle = new Bundle();
                bundle.putString("dialoguetitle","Dialogue chooser");
                bundle.putInt("arrayid",R.array.extras_arrays);

                dialogueCreator.setArguments(bundle);
                dialogueCreator.show(getFragmentManager(),"dialog");


        }
    }


    @Override
    public void userClicked(DialogFragment dialogFragment,int m) {
        maketext.makeText( " "+ array[m]);
    }*/

