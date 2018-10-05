package ml.medyas.bakingapp.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.bakingapp.Classes.RecipeClass;

public class RecipeRepository {

    private DaoClass mRecipeDao;
    private LiveData<List<RecipeClass>> mRecipes;

    public RecipeRepository(Application application) {
        RecipesRoomDB db = RecipesRoomDB.getDatabase(application);
        mRecipeDao = db.recipeDao();
        mRecipes = mRecipeDao.getRecipes();
    }

    public LiveData<List<RecipeClass>> getRecipes() {
        return mRecipes;
    }


    public void insert (List<RecipeClass> recipes) {
        mRecipeDao.insert(recipes);
    }
}
