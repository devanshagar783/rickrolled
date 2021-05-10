package com.example.rickrolled;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

public class InfoFragment extends Fragment {

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

        Resources resources = getResources();

        TextView name = v.findViewById(R.id.charname);
        TextView name2 = v.findViewById(R.id.charname2);
        TextView gender = v.findViewById(R.id.chargender);
        TextView location = v.findViewById(R.id.charlocation);
        ImageView view = v.findViewById(R.id.charimage);
        Button btn = v.findViewById(R.id.rickroll);

        assert getArguments() != null;
        name.setText(getArguments().getString("name"));
        name2.setText(String.format(resources.getString(R.string.charname), getArguments().getString("name")));
        gender.setText(String.format(resources.getString(R.string.chargender), getArguments().getString("gender")));
        location.setText(String.format(resources.getString(R.string.charlocation), getArguments().getString("location")));

        Glide.with(getActivity().getApplicationContext())
                .asBitmap()
                .load(getArguments().getString("image"))
                .into(view);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RickrollMe.class));
            }
        });

        return v;
    }
}