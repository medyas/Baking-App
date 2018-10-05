package ml.medyas.bakingapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.github.kittinunf.result.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Triple;
import ml.medyas.bakingapp.Classes.NavigationIconClickListener;
import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.Fragments.LoadDataFragment;
import ml.medyas.bakingapp.Fragments.RecipeFragment;
import ml.medyas.bakingapp.R;

import static ml.medyas.bakingapp.Activities.RecipeDetailActivity.RECIPE_ITEM;
import static ml.medyas.bakingapp.Classes.UtilsClass.getNavigationIconView;
import static ml.medyas.bakingapp.Fragments.LoadDataFragment.LOADED_DATA;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnFragmentInteractionListener, LoadDataFragment.onLoadDataFinished{
    @BindView(R.id.toolbar) Toolbar toolbar;

    NavigationIconClickListener nav;

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
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new LoadDataFragment())
                        .commit();
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new RecipeFragment())
                        .commit();
            }
        }
        else {
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
    public void loadFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new RecipeFragment())
                .commit();
    }

    @Override
    public void onFragmentInteraction(RecipeClass recipes) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_ITEM, recipes);
        startActivity(intent);
    }
}
