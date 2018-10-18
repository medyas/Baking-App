package ml.medyas.bakingapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.bakingapp.AppSingleton;
import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.Fragments.RecipeDetailFragment;
import ml.medyas.bakingapp.Fragments.RecipeDetailViewFragment;
import ml.medyas.bakingapp.R;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnDetailItemListener, RecipeDetailViewFragment.OnSlideListener{
    public static final String RECIPE_ITEM = "recipe_item";
    public static final String STEPS_ITEM = "steps_item";
    @BindView(R.id.toolbar) Toolbar toolbar;

    private int position;
    private boolean masterDetail = false;
    private RecipeClass recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RECIPE_ITEM);

        } else {
            recipe = AppSingleton.getSelectedRecipe();
        }

        if (findViewById(R.id.right_container) != null) {
            masterDetail = true;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipe.getName());

        if (savedInstanceState == null ) {
            Fragment frag = new RecipeDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(RECIPE_ITEM, recipe);
            frag.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_ITEM, recipe);
    }



    @Override
    public void onDetailItemClicked(int p) {
        if (masterDetail) {
            position = p;
            NestedScrollView leftLayout = findViewById(R.id.left_layout);
            leftLayout.setLayoutParams( new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            NestedScrollView rightLayout  = findViewById(R.id.right_layout);
            rightLayout.setLayoutParams( new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            displayFragment(p);

        } else {

            Intent intent = new Intent(this, RecipeDetailViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEPS_ITEM, recipe);
            bundle.putInt("item_position", p);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onShowNext() {
        swipeRight();
    }

    @Override
    public void onShowPrevious() {
        swipeLeft();
    }

    private void swipeRight() {
        if(recipe.getSteps().size()<=position+1) {
            position = 0;
        }
        else {
            position ++;
        }
        displayFragment(position);
    }

    private void swipeLeft() {
        if(0<position-1) {
            position --;
        }
        else {
            position = recipe.getSteps().size()-1;
        }
        displayFragment(position);
    }

    private void displayFragment(int pos) {
        Fragment frag = new RecipeDetailViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEPS_ITEM, recipe.getSteps().get(pos));
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.right_container, frag)
                .commit();
    }
}
