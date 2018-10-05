package ml.medyas.bakingapp.Classes;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

@Entity(tableName = "recipes")
public class RecipeClass implements Parcelable {
    @PrimaryKey
    private int id;
    private int servings;
    private String name;
    private String image;
    @TypeConverters(IngredientsConverterClass.class)
    private List<IngredientsClass> ingredients;
    @TypeConverters(StepsConverterClass.class)
    private List<StepsClass> steps;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @TypeConverters(IngredientsConverterClass.class)
    public List<IngredientsClass> getIngredients() {
        return ingredients;
    }

    @TypeConverters(IngredientsConverterClass.class)
    public void setIngredients(List<IngredientsClass> ingredients) {
        this.ingredients = ingredients;
    }

    @TypeConverters(StepsConverterClass.class)
    public List<StepsClass> getSteps() {
        return steps;
    }

    @TypeConverters(StepsConverterClass.class)
    public void setSteps(List<StepsClass> steps) {
        this.steps = steps;
    }

    public RecipeClass(int id, int servings, String name, String image, List<IngredientsClass> ingredients, List<StepsClass> steps) {
        this.id = id;
        this.servings = servings;
        this.name = name;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    protected RecipeClass(Parcel in) {
        id = in.readInt();
        servings = in.readInt();
        name = in.readString();
        image = in.readString();
        ingredients = in.createTypedArrayList(IngredientsClass.CREATOR);
        steps = in.createTypedArrayList(StepsClass.CREATOR);
    }

    public static final Creator<RecipeClass> CREATOR = new Creator<RecipeClass>() {
        @Override
        public RecipeClass createFromParcel(Parcel in) {
            return new RecipeClass(in);
        }

        @Override
        public RecipeClass[] newArray(int size) {
            return new RecipeClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(servings);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
    }
}
