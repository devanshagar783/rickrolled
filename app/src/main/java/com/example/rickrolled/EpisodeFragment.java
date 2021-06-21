package com.example.rickrolled;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class EpisodeFragment extends Fragment {

    private TextView episodeNumber;
    private TextView episodeName;
    private TextView firstAired;
    private Button watchlist;

    public EpisodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode, container, false);
        EpisodeFragmentArgs args = EpisodeFragmentArgs.fromBundle(getArguments());
        EpisodeData data = args.getEpData();
        episodeNumber = view.findViewById(R.id.episode_number);
        episodeName = view.findViewById(R.id.episode_name);
        firstAired = view.findViewById(R.id.firstAired);
        episodeNumber.setText(data.getEpisode());
        episodeName.setText(data.getName());
        firstAired.setText(String.format(getResources().getString(R.string.firstAired), data.getAir_date()));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.single_episode_menu, menu);
    }
}