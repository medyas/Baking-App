package ml.medyas.bakingapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.medyas.bakingapp.AppSingleton;
import ml.medyas.bakingapp.Classes.NavigationIconClickListener;
import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.Fragments.LoadDataFragment;
import ml.medyas.bakingapp.Fragments.RecipeFragment;
import ml.medyas.bakingapp.R;

import static ml.medyas.bakingapp.Classes.UtilsClass.getNavigationIconView;
import static ml.medyas.bakingapp.Fragments.LoadDataFragment.LOADED_DATA;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnFragmentInteractionListener, LoadDataFragment.onLoadDataFinished{
    @BindView(R.id.toolbar) Toolbar toolbar;

    NavigationIconClickListener nav;

    // singleton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        nav = new NavigationIconClickListener(this,
                findViewById(R.id.container),
                new AccelerateDecelerateInterpolator(),
                getResources().getDrawable(R.drawable.ic_menu_black_24dp),
                getResources().getDrawable(R.drawable.ic_close_black_24dp));
        toolbar.setNavigationOnClickListener(nav);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (savedInstanceState == null) {
            if (!sharedPref.getBoolean(LOADED_DATA, false)) {
                Log.d(getClass().getName(), "Loading load fragment");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new LoadDataFragment())
                        .commit();
            }
            else {
                Log.d(getClass().getName(), "Loading Recipe fragment");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new RecipeFragment())
                        .commit();
            }
        }
        else {
            Log.d(getClass().getName(), "Loading Recipe fragment directly");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new RecipeFragment())
                    .commit();
        }

    }

    @OnClick({R.id.nav_recipe_activity, R.id.nav_settings_activity, R.id.nav_about_activity})
    public void navMenuClicked(MaterialButton btn) {
        int id = btn.getId();
        switch (id) {
            case R.id.nav_recipe_activity:
                nav.onClick(getNavigationIconView(toolbar));
                break;
            case R.id.nav_settings_activity:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.nav_about_activity:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            default:
                return;
        }
    }


    @Override
    public void onFragmentInteraction(RecipeClass recipes) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        AppSingleton.setSelectedRecipe(recipes);
        startActivity(intent);
    }

    @Override
    public void loadFragment(boolean dataLoaded) {
        Log.d(getClass().getName(), "finished data load !");
        if(dataLoaded) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new RecipeFragment())
                    .commit();
        } else {
            Log.d(getClass().getName(), "Failed to load data !");
            /*getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, null)
                    .commit();
            */
            Snackbar.make(findViewById(R.id.toolbar), "Could not Load Data", Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new LoadDataFragment())
                            .commit();
                }
            });
        }
    }
}
