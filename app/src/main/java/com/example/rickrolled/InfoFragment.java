package com.example.rickrolled;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

public class InfoFragment extends Fragment {

    private TextView name, name2, gender, species, status, origin, location;
    private String img;
    private ImageView view;
    public InfoFragment() {
        // Required empty public constructor
    }
//
//    public InfoFragment(String name, String gender, String species, String status, String origin, String lastknown, String img){
//        this.name.setText(name);
//        this.gender.setText(gender);
//        this.species.setText(species);
//        this.status.setText(status);
//        this.origin.setText(origin);
//        this.lastknown.setText(lastknown);
//        this.img = img;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_info, container, false);

        name=v.findViewById(R.id.charname);
        name2=v.findViewById(R.id.charname2);
        gender=v.findViewById(R.id.chargender);
        location=v.findViewById(R.id.charlocation);
        img="";
        view=v.findViewById(R.id.charimage);

        name.setText(getArguments().getString("name"));
        name2.setText(" " + getArguments().getString("name"));
        gender.setText(" " + getArguments().getString("gender"));
        location.setText(" " + getArguments().getString("location"));

        Glide.with(getActivity().getApplicationContext())
                .asBitmap()
                .load(getArguments().getString("image"))
                .into(view);



        return v;
    }
}