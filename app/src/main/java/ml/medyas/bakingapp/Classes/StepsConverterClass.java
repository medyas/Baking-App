package ml.medyas.bakingapp.Classes;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StepsConverterClass {

    @TypeConverter
    public String fromStepsList(List<StepsClass> steps) {
        if (steps == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StepsClass>>() {}.getType();
        String json = gson.toJson(steps, type);
        return json;
    }

    @TypeConverter
    public List<StepsClass> toStepsList(String steps) {
        if (steps == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<StepsClass>>() {}.getType();
        List<StepsClass> countryLangList = gson.fromJson(steps, type);
        return countryLangList;
    }
}
