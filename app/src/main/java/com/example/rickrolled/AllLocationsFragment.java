package com.example.rickrolled;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


public class AllLocationsFragment extends Fragment {

    private static final String TAG = "Your papa";
    Resources resources = getResources();
    String LOC_URL = resources.getString(R.string.LOCATION_URL);
    JSONObject jsonObject;
    int count;
    private ProgressBar bar;

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
        View v= inflater.inflate(R.layout.fragment_all_locations, container, false);
        //logic
        getjson();
        return v;
    }

    public void getjson() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOC_URL,
                response -> {
                    try {
                        jsonObject = new JSONObject(response);
                        count = Integer.parseInt(jsonObject.getJSONObject("info").getString("count"));

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.d(TAG, "onResponse: " + e.getMessage());
                    } finally {
                    }
                },
                error -> {
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}