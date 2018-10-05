package ml.medyas.bakingapp.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.github.kittinunf.result.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import kotlin.Triple;
import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.R;
import ml.medyas.bakingapp.RecipesViewModal;


public class LoadDataFragment extends Fragment {

    public static final String LOADED_DATA = "loaded_data";
    private onLoadDataFinished mListener;

    private RecipesViewModal mRecipesViewModal;

    public LoadDataFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipesViewModal =  ViewModelProviders.of(this).get(RecipesViewModal.class);
        new asyncTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load_data, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onLoadDataFinished) {
            mListener = (onLoadDataFinished) context;
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

    class asyncTask extends AsyncTask<Void, Void, ArrayList<RecipeClass>> {

        @Override
        protected ArrayList<RecipeClass> doInBackground(Void... voids) {
            ArrayList<RecipeClass> recipes = null;
            try {
                Triple<Request, Response, Result<String,FuelError>> data = Fuel.get(getActivity().getApplicationContext().getResources().getString(R.string.recipe_url)).responseString();
                Result<String,FuelError> text = data.getThird();

                String json = text.get();

                Type listType = new TypeToken<ArrayList<RecipeClass>>(){}.getType();
                recipes = new Gson().fromJson(json, listType);

            } catch (Exception networkError) {
                Log.d("RecipeActivity", networkError.getMessage());
            }
            if(recipes != null) {
                SharedPreferences.Editor sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
                sharedPref.putBoolean(LOADED_DATA, true);
                mRecipesViewModal.insertRecipes(recipes);
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(ArrayList<RecipeClass> r) {
            mListener.loadFragment();
        }
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
    public interface onLoadDataFinished {
        // TODO: Update argument type and name
        void loadFragment();
    }
}
