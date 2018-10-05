package ml.medyas.bakingapp.Adapters;

import android.content.Context;
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

import ml.medyas.bakingapp.Classes.StepsClass;
import ml.medyas.bakingapp.R;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeDetailViewHolder>{
    private List<StepsClass> mIngred = new ArrayList<StepsClass>();
    private RecipeDetailAdapter.itemOnclickListener mListener;

    public RecipeDetailAdapter(List<StepsClass> ing, RecipeDetailAdapter.itemOnclickListener listener) {
        mIngred = ing;
        mListener = (RecipeDetailAdapter.itemOnclickListener) listener;
    }

    @NonNull
    @Override
    public RecipeDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.detail_item_layout, viewGroup, false);


        return new RecipeDetailViewHolder(root);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeDetailViewHolder holder, int i) {
        holder.title.setText(mIngred.get(i).getShortDescription());
        holder.desc.setText(mIngred.get(i).getDescription());

        if (!mIngred.get(i).getThumbnailURL().equals("")) {
            Picasso.get().load(mIngred.get(i).getThumbnailURL())
                    .into(holder.img);
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
        return (mIngred != null)? mIngred.size(): 0;
    }

    class RecipeDetailViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        private TextView title;
        private TextView desc;
        private ImageView img;

        public RecipeDetailViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.detail_layout);
            title = view.findViewById(R.id.step_title);
            desc = view.findViewById(R.id.step_desc);
            img = view.findViewById(R.id.step_thumb);
        }
    }
}
