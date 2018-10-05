package ml.medyas.bakingapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateDecelerateInterpolator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.medyas.bakingapp.Classes.NavigationIconClickListener;
import ml.medyas.bakingapp.R;

import static ml.medyas.bakingapp.Classes.UtilsClass.getNavigationIconView;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    NavigationIconClickListener nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        nav = new NavigationIconClickListener(this,
                findViewById(R.id.product_grid),
                new AccelerateDecelerateInterpolator(),
                getResources().getDrawable(R.drawable.ic_menu_black_24dp),
                getResources().getDrawable(R.drawable.ic_close_black_24dp));
        toolbar.setNavigationOnClickListener(nav);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.product_grid).setBackground(getDrawable(R.drawable.background_shape));
        }
    }


    @OnClick({R.id.nav_recipe_activity, R.id.nav_settings_activity, R.id.nav_about_activity})
    public void navMenuClicked(MaterialButton btn) {
        int id = btn.getId();
        switch (id) {
            case R.id.nav_recipe_activity:
                startActivity(new Intent(getApplicationContext(), RecipeActivity.class));
                break;
            case R.id.nav_settings_activity:
                nav.onClick(getNavigationIconView(toolbar));
                break;
            case R.id.nav_about_activity:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            default:
                return;
        }
    }
}
