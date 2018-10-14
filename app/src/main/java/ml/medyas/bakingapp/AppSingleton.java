package ml.medyas.bakingapp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ml.medyas.bakingapp.Classes.RecipeClass;

@Module
public class AppSingleton {

    private static RecipeClass recipe;

    @Provides
    @Singleton
    public static RecipeClass getSelectedRecipe() {
        return recipe;
    }

    public static void setSelectedRecipe(RecipeClass r) {
        recipe = r;
    }
}
