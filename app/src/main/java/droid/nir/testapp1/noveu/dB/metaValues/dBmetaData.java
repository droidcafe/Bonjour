package droid.nir.testapp1.noveu.dB.metaValues;

import java.util.HashMap;

import droid.nir.testapp1.noveu.dB.ParentDb;

/**
 * Created by droidcafe on 2/24/2016.
 */
public class dBmetaData {


    public static final String DATABASE_SCHEMA = "db_bonjour";
    public static int DATABASE_VERSION = 34;
    public static int DATABASE_VERSION_LEGACY = 81;
    /**
     * different minimum db version for doing particular tasks like migrations
     */
    public final static int DB_VERSION_CHANGE_TO_TASKS = 81;


    /**
     * new tables add below. DONT CHANGE ORDER!!!!dependcies are there like
     * in loadTaskHelper the tasks table are taken directly for
     * {@link droid.nir.testapp1.noveu.Util.Import#inttoStringColumn(int[], int)} function
     */
    public static String[][] columnNames = {
          /*1*/{"_id", "proname", "itemsno"},
          /*2*/ {"_id", "name", "pid", "date", "isrem", "isnotes", "issubtask", "done", "hideflag"},
            {"_id", "tid", "date", "istime"},
            {"_id", "rid", "timehr", "timemin", "isalarm", "isrepeat"},
            {"_id", "aid", "mode"},
            {"_id", "tid", "notes"},
            {"_id", "tid", "subtask", "subtaskorder", "done"},
          /*3*/{"_id", "mode", "oid", "timehr", "timemin", "isalarm", "isfired"}
    };

    public static String[][] columnTypes = {

           /*1*/{"INTEGER", "VARCHAR(100)", "INTEGER"},
           /*2*/   {"INTEGER", "VARCHAR(350)", "INTEGER", "VARCHAR(30)", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER"},
            {"INTEGER", "INTEGER", "VARCHAR(30)", "INTEGER"},
            {"INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER"},
            {"INTEGER", "INTEGER", "INTEGER"},
            {"INTEGER", "INTEGER", "VARCHAR(100)"},
            {"INTEGER", "INTEGER", "VARCHAR(100)", "INTEGER", "INTEGER"},
           /*3*/ {"INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "INTEGER"}
    };
    public static int[] tableLenth = {
          /*1*/3,
           /*2*/9, 4, 6, 3, 3, 5,
           /*3*/7
    };


    public static String[] tableNames = {
            /*1*/"project", /*1*/
           /*2*/ "tasks", "reminder", "alarm", "repeat", "notes", "subtasks",/*6*/
           /*3*/"today_notification"      /*7*/

    };

    /**
     * map for putting extra constraint strings for particular column in tables
     * values are set using the function {@link #setExtras}
     */
    public static HashMap<Integer, String> extra_constraints = new HashMap<>();

    /**
     * extras key = index of column which have extra constraints by considering  {@link dBmetaData#columnNames}
     * as single dimension array
     * can be calculated by summing up {@link #tableLenth} till that table -1 (since id is not counted ) + index of this columnn in
     * {@link #columnNames } array - 1 (the j value -1, since id is not counted while creating table)
     * <p><p/>
     * it is called in {@link ParentDb.AsyncCreate#onPreExecute()}
     */
    public static void setExtras() {
        extra_constraints.put(9, "DEFAULT 0");
    }


}
