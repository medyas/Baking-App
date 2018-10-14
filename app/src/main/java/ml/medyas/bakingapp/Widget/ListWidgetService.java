package ml.medyas.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import ml.medyas.bakingapp.R;

import static ml.medyas.bakingapp.Widget.RecipesWidget.INGRED_LIST;
import static ml.medyas.bakingapp.Widget.RecipesWidget.ingred;

public class ListWidgetService extends RemoteViewsService {


    private String[] mIngred = null;

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        return new ListWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }


    class  ListWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;

        public ListWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
             mContext = applicationContext;
             mIngred = intent.getStringArrayExtra(INGRED_LIST);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            mIngred = ingred;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mIngred.length;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item_layout);
            rv.setTextViewText(R.id.widget_ingred_text, mIngred[i]);

            // Return the remote views object.
            return rv;

        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item_loading_layout);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

    }
}


