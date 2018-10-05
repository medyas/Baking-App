package ml.medyas.bakingapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.bakingapp.Classes.StepsClass;
import ml.medyas.bakingapp.R;

import static ml.medyas.bakingapp.Activities.RecipeDetailActivity.STEPS_ITEM;


public class RecipeDetailViewFragment extends Fragment {
    @BindView(R.id.video_view) PlayerView playerView;
    @BindView(R.id.step_title) TextView title;
    @BindView(R.id.step_desc) TextView desc;


    private OnSlideListener mListener;

    public RecipeDetailViewFragment() {
        // Required empty public constructor
    }

    private StepsClass step;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            step = getArguments().getParcelable(STEPS_ITEM);
        }

        if(savedInstanceState != null ) {
            step = savedInstanceState.getParcelable(STEPS_ITEM);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEPS_ITEM, step);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_recipe_detail_view, container, false);
        ButterKnife.bind(this, root);

        title.setText(step.getShortDescription()==null?"":step.getShortDescription());
        desc.setText(step.getDescription()==null?"":step.getDescription());

        return root;
    }

    /*private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity().getApplicationContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
    }*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSlideListener) {
            mListener = (OnSlideListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSlideListener {
        // TODO: Update argument type and name
        void onShowNext(int position);
        void onShowPrevious(int position);
    }
}
