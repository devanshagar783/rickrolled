package com.example.rickrolled;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AllCharactersFragment extends Fragment {

    private static final String TAG = "AllCharactersFragment";
    View v;

    RecyclerView charView;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Button button, buttonnext, buttonprev;
    ImageView sparkyHome;
    LinearProgressIndicator progressIndicator;
    private String CHAR_URL = "https://rickandmortyapi.com/api/character";

    public AllCharactersFragment() {
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
        v = inflater.inflate(R.layout.fragment_all_characters, container, false);
        button = v.findViewById(R.id.rick_roll);
        buttonnext = v.findViewById(R.id.next);
        buttonprev = v.findViewById(R.id.prev);
        sparkyHome = v.findViewById(R.id.sparkyhome);

        Glide.with(this)
                .asGif()
                .load(R.drawable.sparky)
                .into(sparkyHome);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RickrollMe.class));
            }
        });
        progressIndicator = v.findViewById(R.id.progressbar);
        getjson();
        return v;
    }

    public void getjson() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CHAR_URL,
                response -> {
                    try {
                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    } finally {
                        charView = v.findViewById(R.id.charView);
                        RVAdapter rva = new RVAdapter(getContext(), jsonArray, getResources().getString(R.string.allCharacters));
                        charView.setAdapter(rva);
                        charView.setLayoutManager(new LinearLayoutManager(getContext()));
                        progressIndicator.setVisibility(View.GONE);
                        buttonnext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    progressIndicator.setVisibility(View.VISIBLE);
                                    CHAR_URL = jsonObject.getJSONObject("info").getString("next");
                                    if (CHAR_URL != null)
                                        getjson();
                                    else
                                        Toast.makeText(getContext(), "Doesn't exist", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        buttonprev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    progressIndicator.setVisibility(View.VISIBLE);
                                    CHAR_URL = jsonObject.getJSONObject("info").getString("prev");
                                    if (!CHAR_URL.equals("null")) {
                                        getjson();
                                    } else {
                                        Toast.makeText(getContext(), "Doesn't exist", Toast.LENGTH_SHORT).show();
                                        progressIndicator.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                },
                error -> {
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}