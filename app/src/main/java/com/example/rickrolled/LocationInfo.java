package com.example.rickrolled;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;


public class LocationInfo extends Fragment {

    private static final String TAG = "LocationInfo";

    public LocationInfo() {
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
        View v = inflater.inflate(R.layout.fragment_location_info, container, false);

        RecyclerView residentsRV = v.findViewById(R.id.residentsRV);
        TextView locationname = v.findViewById(R.id.locationname);
        TextView locationtype = v.findViewById(R.id.locationtype);
        TextView locationdimension = v.findViewById(R.id.locationdimension);

        JSONArray residentsJSON = null;
        Resources resources = getResources();

        assert getArguments() != null;
        locationname.setText(String.format(resources.getString(R.string.locationname), getArguments().getString("name")));
        locationtype.setText(String.format(resources.getString(R.string.locationtype), getArguments().getString("type")));
        locationdimension.setText(String.format(resources.getString(R.string.locationdimension), getArguments().getString("dimension")));
        String residents = getArguments().getString("residents");
        Log.d(TAG, "onCreateView:"+residents);

        try {
            residentsJSON = new JSONArray(residents);
            Log.d(TAG, "onCreateView2: "+residentsJSON.getString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            RVAdapter rva = new RVAdapter(getContext(), residentsJSON, resources.getString(R.string.allResidents));
            residentsRV.setAdapter(rva);
            residentsRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }

        return v;
    }
}