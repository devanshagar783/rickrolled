package com.example.rickrolled;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AllEpisodesFragment extends Fragment {

    private static final String TAG = "AllEpisodesFragment";

    public String EPISODES_URL = "https://rickandmortyapi.com/api/episode";
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private TextView temp_episode;

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
        getjson();
        return v;
    }

    private void getjson() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EPISODES_URL,
                response -> {
                    try {
                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject episodeData = jsonArray.getJSONObject(i);
                            temp_episode.append(episodeData.getString("name") + "\n");
                        }
                        EPISODES_URL = jsonObject.getJSONObject("info").getString("next");
                        if (EPISODES_URL != null) {
                            getjson();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    } finally {
                        Toast.makeText(getContext(), "All Episodes Fetched", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}