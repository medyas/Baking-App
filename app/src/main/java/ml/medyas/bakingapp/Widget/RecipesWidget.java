package ml.medyas.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import java.util.List;

import ml.medyas.bakingapp.Activities.RecipeDetailActivity;
import ml.medyas.bakingapp.AppSingleton;
import ml.medyas.bakingapp.Classes.IngredientsClass;
import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.Database.RecipesRoomDB;
import ml.medyas.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipesWidget extends AppWidgetProvider {

    public static final String ACTION_UPDATE_SHOW_NEXT = "ACTION_UPDATE_SHOW_NEXT";
    public static final String ACTION_UPDATE_SHOW_PREV = "ACTION_UPDATE_SHOW_PREV";
    public static final String INGRED_LIST = "INGRED_LIST";
    public static final String INGRED_LIST_POS = "INGRED_LIST_POS";

    public static int position = 0;
    public static String[] ingred;
    private static int size = 0;
    private static List<RecipeClass> recipes = null;

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Intent intent = null;

                if(recipes == null) {
                    recipes = RecipesRoomDB.getDatabase(context).recipeDao().getRecipesList();
                    size = recipes.size();
                }

                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipes_widget);
                views.setTextViewText(R.id.widget_recipe_title, recipes.get(position).getName());

                Intent intentPrev = new Intent(context, RecipesWidget.class);
                intentPrev.setAction(ACTION_UPDATE_SHOW_PREV);
                PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(context, 0, intentPrev, 0);
                views.setOnClickPendingIntent(R.id.widget_prev_recipe, pendingIntentPrev);

                Intent intentNext = new Intent(context, RecipesWidget.class);
                intentNext.setAction(ACTION_UPDATE_SHOW_NEXT);
                PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, 0);
                views.setOnClickPendingIntent(R.id.widget_next_recipe, pendingIntentNext);

                ingred = new String[recipes.get(position).getIngredients().size()];
                for (int i = 0; i <= recipes.get(position).getIngredients().size()-1 ; i++) {
                    IngredientsClass ing = recipes.get(position).getIngredients().get(i);
                    ingred[i] = ing.getQuantity()+" "+ing.getMeasure()+(ing.getQuantity()>1?"'s":"")+" of "+ing.getIngredient();
                }

                intent = new Intent(context, ListWidgetService.class);
                intent.putExtra(INGRED_LIST, ingred);
                views.setRemoteAdapter(R.id.widget_ingred_list, intent);

                Intent intentViews = new Intent(context, RecipeDetailActivity.class);
                AppSingleton.setSelectedRecipe(recipes.get(position));
                PendingIntent pendingIntentViews = PendingIntent.getActivity(context, 0, intentViews, 0);
                views.setOnClickPendingIntent(R.id.widget_layout, pendingIntentViews);

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingred_list);
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);

                return null;
            }
        }.execute();


    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, RecipesWidget.class));
            onUpdate(context, widgetManager, ids);

        } else if(intent.getAction().equals(ACTION_UPDATE_SHOW_NEXT)) {
            if(position+1 >= size) {
                position = 0;
            } else {
                position++ ;
            }

            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, RecipesWidget.class));
            onUpdate(context, widgetManager, ids);

        } else if(intent.getAction().equals(ACTION_UPDATE_SHOW_PREV)) {
            if(position-1 > -1) {
                position--;
            } else {
                position = size-1;
            }

            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, RecipesWidget.class));
            onUpdate(context, widgetManager, ids);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

