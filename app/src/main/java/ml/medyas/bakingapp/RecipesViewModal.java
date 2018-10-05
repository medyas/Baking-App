package ml.medyas.bakingapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.Database.RecipeRepository;

public class RecipesViewModal extends AndroidViewModel {

    private RecipeRepository mRepository;

    private LiveData<List<RecipeClass>> mAllRecipes;

    public RecipesViewModal(@NonNull Application application) {
        super(application);
        mRepository = new RecipeRepository(application);
        mAllRecipes = mRepository.getRecipes();
    }

    public LiveData<List<RecipeClass>> getRecipes() {return mAllRecipes;}

    public void insertRecipes(List<RecipeClass> r) {mRepository.insert(r);}
}
