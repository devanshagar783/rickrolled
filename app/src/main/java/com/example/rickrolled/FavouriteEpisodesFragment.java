package com.example.rickrolled;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavouriteEpisodesFragment extends Fragment {

    private static final String TAG = "FavouriteEpisodesFragment";

    private FirebaseFirestore db;
    private TextView textView;
    private CircularProgressIndicator indicator;
    private RecyclerView favRV;
    private List<EpisodeData> episodeDataList;

    public FavouriteEpisodesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourite_episodes, container, false);
        textView = v.findViewById(R.id.no_fav);
        indicator = v.findViewById(R.id.progress_fav);
        favRV = v.findViewById(R.id.fav_episodes_rev_view);
        getFavourite();
        return v;
    }

    private void getFavourite() {
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> list1 = (ArrayList<String>) documentSnapshot.get("favourite");
                        if (list1 == null || list1.size() == 0) {
                            indicator.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }
                        if (list1 != null && list1.size() > 0) {
                            indicator.setVisibility(View.GONE);
                            favRV.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onSuccess: called");
                            episodeDataList = new ArrayList<>();
                            setList(list1);
                        }
                    }
                });
    }

    private void setList(ArrayList<String> list) {
        for (int i = 0; i < list.size(); ++i) {
            int finalI = i;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, list.get(i),
                    response -> {
                        try {
                            JSONObject episodeData = new JSONObject(response);
                            JSONArray characters = episodeData.getJSONArray("characters");
                            List<String> charName = new ArrayList<>();
                            for (int j = 0; j < characters.length(); ++j) {
                                charName.add(characters.getString(j));
                            }
                            episodeDataList.add(new EpisodeData(episodeData.getInt("id"),
                                    episodeData.getString("name"),
                                    episodeData.getString("air_date"),
                                    episodeData.getString("episode"),
                                    episodeData.getString("url"),
                                    charName));
                            if (episodeDataList.size() == list.size()) {
                                Log.d(TAG, "setList: called" + episodeDataList);
                                RVAdapter adapter = new RVAdapter(getContext(), getResources().getString(R.string.favEpisode), episodeDataList);
                                favRV.setLayoutManager(new LinearLayoutManager(getContext()));
                                favRV.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: " + e.getMessage());
                        }
                    },
                    error -> {
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }
}