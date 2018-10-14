package ml.medyas.bakingapp.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.bakingapp.Adapters.OnSwipeTouchListener;
import ml.medyas.bakingapp.AppSingleton;
import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.Fragments.RecipeDetailViewFragment;
import ml.medyas.bakingapp.R;

import static ml.medyas.bakingapp.Activities.RecipeDetailActivity.STEPS_ITEM;

public class RecipeDetailViewActivity extends AppCompatActivity implements RecipeDetailViewFragment.OnSlideListener{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recipe_detail_view_container) FrameLayout container;

    private RecipeClass recipe;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_view);
        ButterKnife.bind(this);

        if (savedInstanceState == null ) {
            recipe = AppSingleton.getSelectedRecipe();
            position = getIntent().getExtras().getInt("item_position");
        } else {
            recipe = savedInstanceState.getParcelable(STEPS_ITEM);
            position = savedInstanceState.getInt("item_position");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Steps");

        if (savedInstanceState == null) {
            displayFragment(position);
        }

        container.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                if(0<position--) {
                    position --;
                }
                else {
                    position = recipe.getSteps().size();
                }
                displayFragment(position);
            }
            public void onSwipeLeft() {
                if(recipe.getSteps().size()<=position++) {
                    position = 0;
                }
                else {
                    position ++;
                }
                displayFragment(position);
            }
            public void onSwipeBottom() {
            }
        });
    }

    private void displayFragment(int pos) {
        Fragment frag = new RecipeDetailViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEPS_ITEM, recipe.getSteps().get(pos));
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_view_container, frag)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEPS_ITEM, recipe);
        outState.putInt("item_position", position);
    }

    @Override
    public void onShowNext(int position) {

    }

    @Override
    public void onShowPrevious(int position) {

    }
}
