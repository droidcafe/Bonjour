package droid.nir.testapp1.noveu.sync.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import droid.nir.testapp1.noveu.Util.Log;

import java.util.ArrayList;

import droid.nir.testapp1.noveu.Tasks.Loaders.LoadTask;
import droid.nir.testapp1.noveu.Tasks.TaskUtil;
import droid.nir.testapp1.noveu.Util.Import;
import droid.nir.testapp1.noveu.Util.TimeUtil;
import droid.nir.testapp1.noveu.constants.IntentActions;
import droid.nir.testapp1.noveu.dB.Today;
import droid.nir.testapp1.noveu.notifications.handlers.NotificationHandler;
import droid.nir.testapp1.noveu.sync.alarms.DailySyncAlarm;
import droid.nir.testapp1.noveu.today.TodayNotificationHelper;


public class DailySyncService extends IntentService {

    public DailySyncService() {
        super("DailySnycService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (IntentActions.ACTION_DAILY_SYNC.equals(action)) {
                NotificationHandler.cancelAllPendingAlarm(getApplicationContext());
                Today.deleteAll(getApplicationContext(), 0);
                String date = TimeUtil.getTodayDate();
                String selection = "date = ? ";
                int columnNo[] = {0,3,4,5,6,7};
                Cursor task_cursor = LoadTask.loadTasks(getApplicationContext(),
                                                selection, new String[]{date}, columnNo);

                while (Import.isGoodCursor(task_cursor) &&
                        TodayNotificationHelper.isGoodTask(task_cursor))
                {
                    TaskChangeService.startTaskChange(getApplicationContext(),
                            Import.getIntFromCursor(task_cursor, "_id"));
                }

                DailySyncAlarm.setSyncAlarm(getApplicationContext());
            }
        }
    }
}
