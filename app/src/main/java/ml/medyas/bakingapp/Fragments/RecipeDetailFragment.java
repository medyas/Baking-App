package ml.medyas.bakingapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.medyas.bakingapp.Adapters.RecipeDetailAdapter;
import ml.medyas.bakingapp.Classes.IngredientsClass;
import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.R;

import static ml.medyas.bakingapp.Activities.RecipeDetailActivity.RECIPE_ITEM;
import static ml.medyas.bakingapp.Classes.UtilsClass.getScreenWidth;

public class RecipeDetailFragment extends Fragment implements RecipeDetailAdapter.itemOnclickListener {
    @BindView(R.id.detail_img) ImageView img;
    @BindView(R.id.detail_fav) ImageView fav;
    @BindView(R.id.detail_title) TextView title;
    @BindView(R.id.detail_serving) TextView serving;
    @BindView(R.id.detail_review_text) TextView review;
    @BindView(R.id.ing_text) TextView ing_text;
    @BindView(R.id.detail_recycleView) RecyclerView mRecyclerView;
    @BindView(R.id.show_ing) Button show_ings;

    private RecipeClass recipeClass;
    private View root;
    private RecipeDetailAdapter mAdapter;
    private Boolean ingShown = false;

    int[] icons = {R.id.feedback_icon1, R.id.feedback_icon2, R.id.feedback_icon3, R.id.feedback_icon4, R.id.feedback_icon5};
    private String ingText;

    private OnDetailItemListener mListener;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            assert getArguments() != null;
            recipeClass = getArguments().getParcelable(RECIPE_ITEM);
        } else {
            recipeClass = savedInstanceState.getParcelable(RECIPE_ITEM);
        }
        ingText = "<ol>";
        for(IngredientsClass ing:recipeClass.getIngredients()) {
            ingText+="<li> "+ing.getQuantity()+" "+ing.getMeasure()+(ing.getQuantity()>1?"'s":"")+" of "+ing.getIngredient()+"</li>";
        }
        ingText+="</ol>";
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_ITEM, recipeClass);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, root);

        title.setText(recipeClass.getName());
        serving.setText(String.format("  %d Person's", recipeClass.getServings()));
        review.setText(String.format("%d of 5", 0));
        ing_text.setText(Html.fromHtml(ingText));
        if (!recipeClass.getImage().equals("")) {
            Picasso.get().load(recipeClass.getImage())
                    .resize(getScreenWidth(), 600)
                    .placeholder(getActivity().getApplicationContext().getResources().getDrawable(R.drawable.default_background))
                    .error(getActivity().getApplicationContext().getResources().getDrawable(R.drawable.default_background))
                    .into(img);
        } else {
            Picasso.get().load(R.drawable.default_background)
                    .resize(getScreenWidth(), 600)
                    .into(img);
        }

        show_ings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ingShown) {
                    ingShown = false;
                    ing_text.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 150));
                } else {
                    ingShown = true;
                    ing_text.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mAdapter = new RecipeDetailAdapter(recipeClass.getSteps(), this);
        mRecyclerView.setAdapter(mAdapter);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailItemListener) {
            mListener = (OnDetailItemListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick({R.id.feedback_icon1, R.id.feedback_icon2, R.id.feedback_icon3, R.id.feedback_icon4, R.id.feedback_icon5})
    public void onFeedbackIconClick(ImageView icon) {
        int id = icon.getId();
        int iconIndex = findIconIndex(id);
        resetIcons();
        review.setText(String.format("%d of 5", iconIndex + 1));
        for (int i = 0; i < iconIndex+1; i++) {
            ImageView img = root.findViewById(icons[i]);
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
        }
    }

    private void resetIcons() {
        for (int i = 0; i < icons.length; i++) {
            ImageView img = root.findViewById(icons[i]);
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
        }
    }

    private int findIconIndex(int id) {
        for(int i=0; i<icons.length; i++) {
            if(icons[i] == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onClickListener(int position) {
        mListener.onDetailItemClicked(position);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDetailItemListener {
        // TODO: Update argument type and name
        void onDetailItemClicked(int position);
    }
}
