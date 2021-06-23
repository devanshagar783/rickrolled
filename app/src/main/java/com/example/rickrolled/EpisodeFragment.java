package com.example.rickrolled;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EpisodeFragment extends Fragment {

    private static final String TAG = "EpisodeFragment";
    private TextView episodeNumber;
    private TextView episodeName;
    private TextView firstAired;
    private Button watchlist;
    private List<String> characters;
    private RecyclerView recyclerView;
    private String episodeUrl;
    private boolean favourite;
    private FirebaseFirestore db;

    public EpisodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode, container, false);
        EpisodeData data = EpisodeFragmentArgs.fromBundle(getArguments()).getEpData();

        episodeNumber = view.findViewById(R.id.episode_number);
        episodeName = view.findViewById(R.id.episode_name);
        firstAired = view.findViewById(R.id.firstAired);
        watchlist = view.findViewById(R.id.addToWatchlist);
        recyclerView = view.findViewById(R.id.castRV);

        episodeNumber.setText(data.getEpisode());
        episodeName.setText(data.getName());
        firstAired.setText(String.format(getResources().getString(R.string.firstAired), data.getAir_date()));
        characters = data.getCharacters();
        episodeUrl = data.getUrl();
        favourite = false;

        RVAdapter adapter = new RVAdapter(getContext(), getResources().getString(R.string.allResidents), characters, null);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.single_episode_menu, menu);
        isFavourite(menu);
    }

    private void isFavourite(Menu menu) {
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> list1 = (ArrayList<String>) documentSnapshot.get("favourite");
                        if (list1 != null)
                            for (int i = 0; i < list1.size(); ++i) {
                                if (list1.get(i).equals(episodeUrl)) {
                                    menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.fav_icon_red));
                                    favourite = true;
                                }
                            }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.like_episode) {
            if (favourite) {
                item.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.fav_icon));
                removeEpisodeFromFavourite();
                favourite = false;
            } else {
                item.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.fav_icon_red));
                addEpisodeToFavourite();
                favourite = true;
            }

        } else
            return super.onOptionsItemSelected(item);
        return true;
    }

    private void removeEpisodeFromFavourite() {
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update("favourite", FieldValue.arrayRemove(episodeUrl));
    }

    private void addEpisodeToFavourite() {
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update("favourite", FieldValue.arrayUnion(episodeUrl));
    }
}