package ml.medyas.bakingapp.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.medyas.bakingapp.Classes.ExoPlayerManager;
import ml.medyas.bakingapp.Classes.StepsClass;
import ml.medyas.bakingapp.R;

import static ml.medyas.bakingapp.Activities.RecipeDetailActivity.STEPS_ITEM;
import static ml.medyas.bakingapp.Classes.UtilsClass.getScreenHeight;


public class RecipeDetailViewFragment extends Fragment {
    public static final String PLAY_WHEN_READY = "play_when_ready";
    public static final String PLAYBACK_POSITION = "playback_position";
    public static final String thumb = "https://d2v9y0dukr6mq2.cloudfront.net/video/thumbnail/itCjTBE/loading-waiting-web-symbol-element-black-and-white-motion-design-video-looping-animation-hd-1920x1080_baa7wxg__F0000.png";
    @BindView(R.id.video_view) PlayerView playerView;
    @BindView(R.id.view_step_title) TextView title;
    @BindView(R.id.view_step_desc) TextView desc;


    private SimpleExoPlayer player;
    private long playbackPosition = 0;
    private int currentWindow;
    private Boolean playWhenReady = false;

    private OnSlideListener mListener;

    private ExoPlayerManager exoPlayerManager;

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
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEPS_ITEM, step);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        outState.putLong(PLAYBACK_POSITION, playbackPosition);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_recipe_detail_view, container, false);
        ButterKnife.bind(this, root);

        title.setText(step.getShortDescription()==null?"":step.getShortDescription());
        desc.setText(step.getDescription()==null?"":step.getDescription());

        if(getActivity().getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            playerView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    getScreenHeight()));
        }


        exoPlayerManager = new ExoPlayerManager(playerView, getActivity().getApplicationContext());
        if(!step.getVideoURL().equals("")) {
            Uri videoUri = Uri.parse(step.getVideoURL());
            exoPlayerManager.play(videoUri);
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick({R.id.view_swipe_left, R.id.view_swipe_right})
    public void swipeFragments(ImageView img) {
        int id = img.getId();
        if(id == R.id.view_swipe_left) {
            mListener.onShowPrevious();
        } else if(id == R.id.view_swipe_right) {
            mListener.onShowNext();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        exoPlayerManager.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        exoPlayerManager.onResume();

        exoPlayerManager.getPlayer().setPlayWhenReady(playWhenReady);
        exoPlayerManager.getPlayer().seekTo(playbackPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        playbackPosition = exoPlayerManager.getPlayer().getCurrentPosition();
        playWhenReady = exoPlayerManager.getPlayer().getPlayWhenReady();
        exoPlayerManager.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        exoPlayerManager.onStop();
    }

    private void initializePlayer() {
        /*BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Initialize the player
        player = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), trackSelector);

        //Initialize simpleExoPlayerView
        playerView.setPlayer(player);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getActivity().getApplicationContext(), Util.getUserAgent(getActivity().getApplicationContext(), "BackingApp"));

        // This is the MediaSource representing the media to be played.
        if(!step.getVideoURL().equals("")) {
            Uri videoUri = Uri.parse(step.getVideoURL());
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);

            // Prepare the player with the source.
            player.prepare(videoSource);


            player.seekTo(playbackPosition);
            player.setPlayWhenReady(playWhenReady);
        }
        */


    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

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
        void onShowNext();
        void onShowPrevious();
    }
}
