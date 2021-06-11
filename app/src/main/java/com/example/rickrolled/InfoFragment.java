package com.example.rickrolled;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

public class InfoFragment extends Fragment {

    private static final String TAG = "InfoFragment";
    private ProgressBar progressBar;


    public InfoFragment() {
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
        View v= inflater.inflate(R.layout.fragment_info, container, false);
        progressBar = v.findViewById(R.id.progresshome);
        TextView name = v.findViewById(R.id.charname);
        TextView name2 = v.findViewById(R.id.charname2);
        TextView gender = v.findViewById(R.id.chargender);
        TextView location = v.findViewById(R.id.charlocation);
        ImageView view = v.findViewById(R.id.charimage);
        Button btn = v.findViewById(R.id.rickroll);
        if(getArguments().getString("gender") == null)
            v.findViewById(R.id.main_layout).setVisibility(View.GONE);

        assert getArguments() != null;
        name.setText(getArguments().getString("name"));
        name2.setText(String.format(getResources().getString(R.string.charname), getArguments().getString("name")));
        gender.setText(String.format(getResources().getString(R.string.chargender), getArguments().getString("gender")));
        location.setText(String.format(getResources().getString(R.string.charlocation), getArguments().getString("location")));
        progressBar.setVisibility(View.GONE);
        v.findViewById(R.id.main_layout).setVisibility(View.VISIBLE);

        Glide.with(getActivity().getApplicationContext())
                .asBitmap()
                .load(getArguments().getString("image"))
                .into(view);

        btn.setOnClickListener(v1 -> startActivity(new Intent(getContext(), RickrollMe.class)));

        return v;
    }
}