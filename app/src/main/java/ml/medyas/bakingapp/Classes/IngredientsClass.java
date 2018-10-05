package ml.medyas.bakingapp.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class IngredientsClass implements Parcelable {
    private Double quantity;
    private String ingredient;
    private String measure;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public IngredientsClass(Double quantity, String ingredient, String measure) {
        this.quantity = quantity;
        this.ingredient = ingredient;
        this.measure = measure;
    }

    protected IngredientsClass(Parcel in) {
        quantity = in.readDouble();
        ingredient = in.readString();
        measure = in.readString();
    }

    public static final Creator<IngredientsClass> CREATOR = new Creator<IngredientsClass>() {
        @Override
        public IngredientsClass createFromParcel(Parcel in) {
            return new IngredientsClass(in);
        }

        @Override
        public IngredientsClass[] newArray(int size) {
            return new IngredientsClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(ingredient);
        parcel.writeString(measure);
    }
}
