package droid.nir.testapp1.noveu.dB.metaValues;

/**
 * Created by droidcafe on 2/24/2016.
 */
public class dBmetaData {


    public static final String DATABASE_SCHEMA = "db_bonjour";
    public static int DATABASE_VERSION = 25;

    /**
     * new tables add below. DONT CHANGE ORDER!!!!dependcies are there like
     * in loadTaskHelper the tasks table are taken directly for Import.inttostringcolumn function
     */
  public static String[][] columnNames = {
          /*1*/{"_id", "proname", "itemsno"},
          /*2*/ {"_id","name","pid","date","isrem","isnotes","issubtask","done"},
          {"_id","tid","date","istime"},
          {"_id","rid","timehr","timemin","isalarm","isrepeat"},
          {"_id","aid","mode"},
          {"_id","tid","notes"},
          {"_id","tid","subtask","subtaskorder","done"},
          /*3*/{"_id","mode","oid","timehr","timemin","isalarm","isfired"}
    };

   public static String[][] columnTypes = {

           /*1*/{"INTEGER", "VARCHAR(100)", "INTEGER"},
           /*2*/   {"INTEGER","VARCHAR(350)","INTEGER","VARCHAR(30)","INTEGER","INTEGER","INTEGER","INTEGER"},
           {"INTEGER","INTEGER","VARCHAR(30)","INTEGER"},
           {"INTEGER","INTEGER","INTEGER","INTEGER","INTEGER","INTEGER"},
           {"INTEGER","INTEGER","INTEGER"},
           {"INTEGER","INTEGER","VARCHAR(100)"},
           {"INTEGER","INTEGER","VARCHAR(100)","INTEGER","INTEGER"},
           /*3*/ {"INTEGER","INTEGER","INTEGER","INTEGER","INTEGER","INTEGER","INTEGER"}
    };
   public static int[] tableLenth = {
          /*1*/3,
           /*2*/8,4,6,3,3,5,
           /*3*/7
   };


   public static String[] tableNames = {
            /*1*/"project", /*1*/
           /*2*/ "tasks","reminder","alarm","repeat","notes","subtasks",/*6*/
           /*3*/"today_notification"      /*7*/

   };

    /**
     * different minimum db version for doing particular tasks like migrations
     */
    public final static int DB_VERSION_CHANGE_TO_TASKS = 79;


}
