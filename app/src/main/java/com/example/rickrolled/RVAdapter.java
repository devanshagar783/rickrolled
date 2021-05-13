package com.example.rickrolled;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    JSONArray characters;
    Context context;
    String adapterType;
    private static final String TAG = "RVAdapter";

    public RVAdapter(Context context, JSONArray characters, String adapterType) {
        this.context = context;
        this.characters = characters;
        this.adapterType = adapterType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (adapterType) {
            case "ALL_CHARACTERS":
                View view1 = inflater.inflate(R.layout.char_view, parent, false);
                return new AllCharactersHolder(view1);
            case "ALL_LOCATIONS":
                View view2 = inflater.inflate(R.layout.char_view, parent, false);
                return new AllLocationsHolder(view2);
            case "ALL_EPISODES":
                View view3 = inflater.inflate(R.layout.char_view, parent, false);
                return new AllEpisodesHolder(view3);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Resources resources = context.getResources();
        switch (adapterType) {
            case "ALL_CHARACTERS":
                AllCharactersHolder ACH = (AllCharactersHolder) holder;
                try {
                    JSONObject charsIndi = characters.getJSONObject(position);
                    ACH.charname.setText(charsIndi.getString("name"));
                    ACH.chargender.setText(charsIndi.getString("gender"));
                    ACH.charstatus.setText(charsIndi.getString("status"));
                    ACH.charspecies.setText(String.format(resources.getString(R.string.charSpecies_placeholder), charsIndi.getString("species"), charsIndi.getString("type")));
                    ACH.charorigin.setText(charsIndi.getJSONObject("origin").getString("name"));
                    ACH.charlastknown.setText(charsIndi.getJSONObject("location").getString("name"));

                    String imgURL = charsIndi.getString("image");
                    Glide.with(context)
                            .asBitmap()
                            .load(imgURL)
                            .into(ACH.charimage);

                    ACH.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CharacterFragment fragment = new CharacterFragment();
                            Bundle bundle = new Bundle();
                            try {
                                bundle.putString("name", charsIndi.getString("name"));
                                bundle.putString("gender", charsIndi.getString("gender"));
                                bundle.putString("species", charsIndi.getString("species"));
                                bundle.putString("status", charsIndi.getString("status"));
                                bundle.putString("origin", charsIndi.getJSONObject("origin").getString("name"));
                                bundle.putString("location", charsIndi.getJSONObject("location").getString("name"));
                                bundle.putString("image", charsIndi.getString("image"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                fragment.setArguments(bundle);
                                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack("New Fragment").commit();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

//            case "ALL_LOCATIONS":
//                AllLocationsHolder ALH = (AllLocationsHolder) holder;
//                try {
//                        //Logic
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//
//            case "ALL_EPISODES":
//                AllEpisodesHolder AEH = (AllEpisodesHolder) holder;
//                try {
//                    //Logic
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
        }
    }

    @Override
    public int getItemCount() {
        return characters.length();
    }

    public static class AllCharactersHolder extends RecyclerView.ViewHolder {

        TextView charname, chargender, charstatus, charspecies, charorigin, charlastknown;
        ImageView charimage;

        public AllCharactersHolder(@NonNull View itemView) {
            super(itemView);
            charname = itemView.findViewById(R.id.charname);
            chargender = itemView.findViewById(R.id.chargender);
            charstatus = itemView.findViewById(R.id.charstatus);
            charspecies = itemView.findViewById(R.id.charspecies);
            charorigin = itemView.findViewById(R.id.charorigin);
            charlastknown = itemView.findViewById(R.id.charlastknown);
            charimage = itemView.findViewById(R.id.charimage);
        }
    }

    public static class AllLocationsHolder extends RecyclerView.ViewHolder {
        //fields
        public AllLocationsHolder(@NonNull View itemView) {
            super(itemView);
            //initialize values
        }
    }

    public static class AllEpisodesHolder extends RecyclerView.ViewHolder {
        //fields
        public AllEpisodesHolder(@NonNull View itemView) {
            super(itemView);
            //initialize values
        }
    }
}
