package droid.nir.testapp1.noveu.Events.loaders;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import droid.nir.testapp1.noveu.Home.data.dataEvent;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.pickers.DbUtil;
import droid.nir.testapp1.noveu.dB.Events;

/**
 * Created by droidcafe on 11/16/2016.
 */

public class loadEvent {

    private Context context;
    private Activity activity;
    private String[] preSelectionArgs;
    private String preselection;


    public loadEvent(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public loadEvent() {

    }

    /**
     * intialisation any preselection conditions for all events like for a particular project only
     *
     * @param preselection     - the preSelection condition
     * @param preSelectionArgs - the arguments for presSelection
     */
    public void loadPreSelection(String preselection, String[] preSelectionArgs) {
        this.preselection = preselection;
        this.preSelectionArgs = preSelectionArgs;
    }

    public List<dataEvent> laodAllEvents(Context context) {
        List<dataEvent> eventList = new ArrayList<>();
        Cursor eventCursor = loadEvent(context,
                DbUtil.checkSelection(null,preselection),
                DbUtil.checkSelectionArgs(preSelectionArgs,null));

        while (eventCursor.moveToNext()) {

        }
        return eventList;
    }

    public static Cursor loadEvent(Context context, String selection, String[] selectionArgs) {
        return loadEvent(context,selection,selectionArgs,new int[]{0,1,3,4,6,7,9});
    }

    /**
     * load all event for that particular selection and selectionArgs ..return its cursor
     *
     * @param selection     - selection string
     * @param selectionArgs selection arguments
     * @param columnNo      required columns
     * @return
     */
    public static Cursor loadEvent(Context context, String selection, String[] selectionArgs, int[] columnNo) {
        return Events.select(context, 0, columnNo, selection, selectionArgs, null, null, null);
    }
}
