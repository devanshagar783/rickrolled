package com.example.rickrolled;

import android.content.res.Resources;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class AllLocationsFragment extends Fragment {

    private static final String TAG = "Your papa";
    View v;

    JSONObject jsonObject;
    JSONArray jsonArray;
    RecyclerView locationsRV;
    Button button, buttonnext, buttonprev;
    ImageView sparkyHome;
    private LinearProgressIndicator progressIndicator;
    String LOC_URL = "https://rickandmortyapi.com/api/location";

    public AllLocationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_all_locations, container, false);
        locationsRV = v.findViewById(R.id.locationsRV);
        progressIndicator = v.findViewById(R.id.progressbar);
        button = v.findViewById(R.id.rick_roll);
        buttonnext = v.findViewById(R.id.next);
        buttonprev = v.findViewById(R.id.prev);
        sparkyHome = v.findViewById(R.id.sparkyhome);
        Glide.with(this)
                .asGif()
                .load(R.drawable.sparky)
                .into(sparkyHome);
        getjson();
        return v;
    }

    public void getjson() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOC_URL,
                response -> {
                    try {
                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("results");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    } finally {
                        Resources res = getResources();
                        locationsRV = v.findViewById(R.id.locationsRV);
                        RVAdapter rva = new RVAdapter(getContext(), jsonArray, res.getString(R.string.allLocations));
                        locationsRV.setAdapter(rva);
                        locationsRV.setLayoutManager(new LinearLayoutManager(getContext()));
                        progressIndicator.setVisibility(View.GONE);
                        buttonnext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    progressIndicator.setVisibility(View.VISIBLE);
                                    LOC_URL = jsonObject.getJSONObject("info").getString("next");
                                    if (LOC_URL != null)
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
                                    LOC_URL = jsonObject.getJSONObject("info").getString("prev");
                                    Log.d(TAG, "onClick: 123" + LOC_URL);
                                    if (!LOC_URL.equals("null")) {
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