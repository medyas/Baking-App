package ml.medyas.bakingapp.Activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.Classes.StepsClass;
import ml.medyas.bakingapp.Fragments.RecipeDetailViewFragment;
import ml.medyas.bakingapp.R;

import static ml.medyas.bakingapp.Activities.RecipeDetailActivity.STEPS_ITEM;

public class RecipeDetailViewActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    private RecipeClass recipe;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_view);
        ButterKnife.bind(this);

        if (savedInstanceState == null ) {
            recipe = getIntent().getExtras().getParcelable(STEPS_ITEM);
            position = getIntent().getExtras().getInt("item_position");
        } else {
            recipe = savedInstanceState.getParcelable(STEPS_ITEM);
            position = savedInstanceState.getInt("item_position");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Steps");

        if (savedInstanceState != null) {
            Fragment frag = new RecipeDetailViewFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEPS_ITEM, recipe.getSteps().get(position));
            frag.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEPS_ITEM, recipe);
        outState.putInt("item_position", position);
    }
}
