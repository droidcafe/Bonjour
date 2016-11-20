package droid.nir.testapp1.noveu.Tasks.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by droidcafe on 2/28/2016.
 */
public class SharedData {

    public static List<String> list;
    public static List<Integer> subTaskdone;
    public static String notes;


    /**
     * clear all the list and subtask list variables
     * make notes empty again
     */
    public static  void clearAll(){
        list.clear();
        subTaskdone.clear();
        notes="";
    }
    static {
        list = new ArrayList<>();
        subTaskdone = new ArrayList<>();
        notes="";
    }


}
