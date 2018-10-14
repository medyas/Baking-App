package ml.medyas.bakingapp.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.bakingapp.Adapters.RecipesAdapter;
import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.R;
import ml.medyas.bakingapp.RecipesViewModal;

import static ml.medyas.bakingapp.Classes.UtilsClass.calculateNoOfColumns;
import static ml.medyas.bakingapp.Classes.UtilsClass.getScreenWidth;


public class RecipeFragment extends Fragment implements RecipesAdapter.itemOnclickListener{
    @BindView(R.id.recipe_recycleView) RecyclerView mRecyclerView;
    @BindView(R.id.progress) ProgressBar progress;

    private RecipesViewModal mRecipesViewModal;
    private RecipesAdapter mAdapter;
    private List<RecipeClass> recipes = new ArrayList<RecipeClass>();

    private OnFragmentInteractionListener mListener;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipesViewModal = ViewModelProviders.of(this).get(RecipesViewModal.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);

        mRecyclerView.setHasFixedSize(true);
        if(getActivity().getApplicationContext().getResources().getString(R.string.inTablet).equals("true")) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), calculateNoOfColumns(getActivity().getApplicationContext())));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        }
        mAdapter = new RecipesAdapter(recipes, getActivity().getApplicationContext(), getScreenWidth(), this);
        mRecyclerView.setAdapter(mAdapter);

        mRecipesViewModal.getRecipes().observeForever(new Observer<List<RecipeClass>>() {
            @Override
            public void onChanged(@Nullable List<RecipeClass> recipeClasses) {
                recipes.addAll(recipeClasses);
                progress.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }
        });


        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    @Override
    public void onClickListener(int position) {
        mListener.onFragmentInteraction(recipes.get(position));
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(RecipeClass recipe);
    }
}
