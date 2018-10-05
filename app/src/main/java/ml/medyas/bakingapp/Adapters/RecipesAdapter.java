package ml.medyas.bakingapp.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.bakingapp.Classes.RecipeClass;
import ml.medyas.bakingapp.R;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>{
    private List<RecipeClass> mRecipes = new ArrayList<RecipeClass>();
    private Context ctx;
    private int width;
    private itemOnclickListener mListener;

    public RecipesAdapter(List<RecipeClass> recipes, Context ctx, int width, itemOnclickListener listener) {
        mRecipes = recipes;
        this.ctx = ctx;
        this.width = width;
        mListener = (itemOnclickListener) listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipes_layout, viewGroup, false);


        return new RecipeViewHolder(root);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int i) {
        holder.title.setText(mRecipes.get(i).getName());
        holder.serving.setText(String.format("  %d Persons", mRecipes.get(i).getServings()));
        if(ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!mRecipes.get(i).getImage().equals("")) {
                Picasso.get().load(mRecipes.get(i).getImage())
                        .resize(250, 250)
                        .placeholder(ctx.getResources().getDrawable(R.drawable.default_background))
                        .error(ctx.getResources().getDrawable(R.drawable.default_background))
                        .into(holder.img);
            } else {
                Picasso.get().load(R.drawable.default_background)
                        .resize(250, 250)
                        .into(holder.img);
            }
        } else {
            if (!mRecipes.get(i).getImage().equals("")) {
                Picasso.get().load(mRecipes.get(i).getImage())
                        .resize(width, 600)
                        .placeholder(ctx.getResources().getDrawable(R.drawable.default_background))
                        .error(ctx.getResources().getDrawable(R.drawable.default_background))
                        .into(holder.img);
            } else {
                Picasso.get().load(R.drawable.default_background)
                        .resize(width, 600)
                        .into(holder.img);
            }
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickListener(holder.getAdapterPosition());
            }
        });
    }

    public interface itemOnclickListener {
        public void onClickListener(int position);
    }

    @Override
    public int getItemCount() {
        return (mRecipes != null)? mRecipes.size(): 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        private TextView title;
        private TextView serving;
        private ImageView img;

        public RecipeViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.recipe_layout);
            title = view.findViewById(R.id.recipe_title);
            serving = view.findViewById(R.id.recipe_serving);
            img = view.findViewById(R.id.recipe_image);
        }
    }
}
