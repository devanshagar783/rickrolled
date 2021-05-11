package com.example.rickrolled;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class LocationInfo extends Fragment {

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

        return v;
    }
}