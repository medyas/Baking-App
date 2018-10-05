package ml.medyas.bakingapp.Classes;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientsConverterClass {

    @TypeConverter
    public String fromIngredientsList(List<IngredientsClass> ing) {
        if (ing == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<IngredientsClass>>() {}.getType();
        String json = gson.toJson(ing, type);
        return json;
    }

    @TypeConverter
    public List<IngredientsClass> toIngredientsList(String ing) {
        if (ing == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<IngredientsClass>>() {}.getType();
        List<IngredientsClass> countryLangList = gson.fromJson(ing, type);
        return countryLangList;
    }
}
