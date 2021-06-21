package com.example.rickrolled;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EpisodeFragment extends Fragment {

    private TextView episodeNumber;
    private TextView episodeName;
    private TextView firstAired;
    private Button watchlist;
    private List<String> characters;
    private RecyclerView recyclerView;

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
        watchlist = view.findViewById(R.id.addToWatchlist);
        recyclerView = view.findViewById(R.id.castRV);
        episodeNumber.setText(data.getEpisode());
        episodeName.setText(data.getName());
        firstAired.setText(String.format(getResources().getString(R.string.firstAired), data.getAir_date()));
        characters = data.getCharacters();
        RVAdapter adapter = new RVAdapter(getContext(), getResources().getString(R.string.allResidents), characters, null);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.single_episode_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.like_episode)
            addEpisodeToFavourite();
        else
            return super.onOptionsItemSelected(item);
        return true;
    }

    private void addEpisodeToFavourite() {

    }
}