package ml.medyas.bakingapp.Classes;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;

public class UtilsClass {


    public static View getNavigationIconView(Toolbar toolbar) {

        String previousContentDescription = (String) toolbar.getNavigationContentDescription();
        // Check if contentDescription previously was set
        boolean hadContentDescription = !TextUtils.isEmpty(previousContentDescription);
        String contentDescription = hadContentDescription ?
                previousContentDescription : "navigationIcon";
        toolbar.setNavigationContentDescription(contentDescription);

        ArrayList<View> potentialViews = new ArrayList<>();
        // Find the view based on it's content description, set programmatically or with
        // android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription,
                View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);

        // Nav icon is always instantiated at this point because calling
        // setNavigationContentDescription ensures its existence
        View navIcon = null;
        if (potentialViews.size() > 0) {
            navIcon = potentialViews.get(0); //navigation icon is ImageButton
        }

        // Clear content description if not previously present
        if (!hadContentDescription)
            toolbar.setNavigationContentDescription(previousContentDescription);

        return navIcon;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 250);
        return noOfColumns;
    }
}
