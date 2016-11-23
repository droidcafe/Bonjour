package droid.nir.testapp1.noveu.Util;

/**
 * Created by droidcafe on 11/18/2016.
 */

import droid.nir.testapp1.noveu.Events.loaders.loadEvent;

public class DbUtil {

    /**
     * checks if any preselction present and if present then concats the selection string with preselction
     * used in {@link loadEvent#preselection}
     *
     * @param selection    - the selection condition for the particular query
     * @param preselection - the already present preselection condition for all queryies
     * @return - the new selection statement with preselection concated to it
     */
    public static String checkSelection(String selection, String preselection) {

        if (preselection != null) {
            if (selection == null) {
                return preselection;
            }
            selection = preselection.concat(" and " + selection);
        }


        return selection;
    }

    /**
     * checks if any preselection args present and add the new argument accordingly - Used in
     * {@link loadEvent#preselection}
     *
     * @param preSelectionArgs - the already present preSelectionArgs  for preSelection condition
     *                         Used in {@link loadEvent#preSelectionArgs}
     * @param newSelectionArg  the new selection arg
     * @return - the new arguments in correct order,, first preselection args then new
     * selection argument
     */

    public static String[] checkSelectionArgs(String[] preSelectionArgs, String newSelectionArg) {
        if (preSelectionArgs != null && newSelectionArg != null) {
            String[] selectionArgs = new String[preSelectionArgs.length + 1];
            int i;
            for (i = 0; i < preSelectionArgs.length; i++)
                selectionArgs[i] = preSelectionArgs[i];
            selectionArgs[i] = newSelectionArg;
            return selectionArgs;
        } else if (preSelectionArgs != null)
            return preSelectionArgs;
        else if (newSelectionArg != null)
            return new String[]{newSelectionArg};


        return null;

    }

}
