package ml.medyas.bakingapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ml.medyas.bakingapp.Classes.RecipeClass;

@Database(entities = {RecipeClass.class}, version = 1, exportSchema = false)
public abstract class RecipesRoomDB extends RoomDatabase {

    public abstract DaoClass recipeDao();

    private static volatile RecipesRoomDB INSTANCE;

    public static RecipesRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipesRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipesRoomDB.class, "recipes")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
