package ml.medyas.bakingapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ml.medyas.bakingapp.Classes.RecipeClass;

@Dao
public interface DaoClass {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<RecipeClass> recipes);

    @Query("select * from recipes ORDER BY id ASC")
    LiveData<List<RecipeClass>> getRecipes();
}
