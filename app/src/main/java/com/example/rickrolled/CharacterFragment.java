package com.example.rickrolled;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

public class CharacterFragment extends Fragment {

    private CircularImageView imageView;
    private TextView name, status, gender, species, origin, location;

    public CharacterFragment() {
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
        View v = inflater.inflate(R.layout.fragment_character, container, false);
        imageView = v.findViewById(R.id.charimage);
        name = v.findViewById(R.id.charname);
        status = v.findViewById(R.id.charstatus);
        gender = v.findViewById(R.id.chargender);
        species = v.findViewById(R.id.charspecies);
        origin = v.findViewById(R.id.charorigin);
        location = v.findViewById(R.id.charlastknown);
        CharacterFragmentArgs args = CharacterFragmentArgs.fromBundle(getArguments());

        Glide.with(getActivity().getApplicationContext())
                .asBitmap()
                .load(args.getImage())
                .into(imageView);

        name.setText(args.getName());
        status.setText(String.format(getResources().getString(R.string.chardoa), args.getStatus()));
        gender.setText(args.getGender());
        species.setText(args.getSpecies());
        origin.setText(args.getOrigin());
        location.setText(args.getLocation());

        return v;
    }
}