package com.example.rickrolled;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        LocationInfoArgs args = LocationInfoArgs.fromBundle(getArguments());

        assert getArguments() != null;
        locationname.setText(String.format(getResources().getString(R.string.locationname), args.getName()));
        locationtype.setText(String.format(getResources().getString(R.string.locationtype), args.getType()));
        locationdimension.setText(String.format(getResources().getString(R.string.locationdimension), args.getDimension()));
        String residents = args.getResidents();
        try {
            residentsJSON = new JSONArray(residents);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            RVAdapter rva = new RVAdapter(getContext(), residentsJSON, getResources().getString(R.string.allResidents));
            residentsRV.setAdapter(rva);
            residentsRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        return v;
    }
}