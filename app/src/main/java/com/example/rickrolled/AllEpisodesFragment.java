package com.example.rickrolled;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllEpisodesFragment extends Fragment {

    private static final String TAG = "AllEpisodesFragment";

    public String EPISODES_URL = "https://rickandmortyapi.com/api/episode";
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private TextView temp_episode;
    private List<EpisodeData> episodeDataList = new ArrayList<>();

    public AllEpisodesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_episodes, container, false);
        temp_episode = v.findViewById(R.id.tv_episode);
        temp_episode.setText("");
        getJson();
        Log.d(TAG, "onCreateView: " + episodeDataList.size());
        return v;
    }

    private void getJson() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EPISODES_URL,
                response -> {
                    try {
                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject episodeData = jsonArray.getJSONObject(i);
                            temp_episode.append(episodeData.getString("name") + "\n");
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
                        }
                        EPISODES_URL = jsonObject.getJSONObject("info").getString("next");
                        if (!EPISODES_URL.equals("null"))
                            getJson();
                        else
                            arrangeEpisodes();
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

    private void arrangeEpisodes() {
        Pattern pattern = Pattern.compile("^S\\d\\d");
        LinkedHashMap<String, List<EpisodeData>> map = new LinkedHashMap<>();

        String currentSeason = "";
        List<EpisodeData> currentList = new ArrayList<>();

        for (int i = 0; i < episodeDataList.size(); ++i) {
            Matcher matcher = pattern.matcher(episodeDataList.get(i).getEpisode());
            if (matcher.find()) {
                if (!currentSeason.equals(matcher.group())) {
                    if (!currentSeason.equals(""))
                        map.put(currentSeason, currentList);
                    currentSeason = matcher.group();
                    currentList.clear();
                }
                currentList.add(episodeDataList.get(i));
            }
        }

        map.put(currentSeason, currentList);
        map.keySet().forEach(key -> {
            Log.d(TAG, "arrangeEpisodes: key: " + key + "  value: " + map.get(key));
        });

    }
}