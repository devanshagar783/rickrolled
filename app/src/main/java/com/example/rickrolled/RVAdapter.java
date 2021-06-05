package com.example.rickrolled;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    JSONArray data;
    Context context;
    String adapterType;
    JSONObject charsIndi;
    String imgURL;
    //    LinkedHashMap<String, List<EpisodeData>> map;
    List<Map.Entry<String, List<EpisodeData>>> newList;


    private static final String TAG = "RVAdapter";

    public RVAdapter(Context context, JSONArray data, String adapterType) {
        this.context = context;
        this.data = data;
        this.adapterType = adapterType;
    }

    public RVAdapter(Context context, LinkedHashMap<String, List<EpisodeData>> map, String adapterType) {
        this.context = context;
//        this.map = map;
        this.adapterType = adapterType;
        newList = new ArrayList<>(map.entrySet());
        // Get the i'th term
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
                View view2 = inflater.inflate(R.layout.all_location_card, parent, false);
                return new AllLocationsHolder(view2);
            case "ALL_EPISODES":
                View view3 = inflater.inflate(R.layout.all_ep_card, parent, false);
                return new AllEpisodesHolder(view3);
            case "ALL_RESIDENTS":
                View view4 = inflater.inflate(R.layout.residents_rv_card, parent, false);
                return new AllResidentsHolder(view4);
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
                    JSONObject charsIndi = data.getJSONObject(position);
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

                    ACH.itemView.setOnClickListener(view -> {
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
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "ALL_LOCATIONS":
                AllLocationsHolder ALH = (AllLocationsHolder) holder;
                try {
                    JSONObject locIndi = data.getJSONObject(position);
                    ALH.planetName.setText(locIndi.getString("name"));

                    ALH.itemView.setOnClickListener(view -> {
                        LocationInfo fragment = new LocationInfo();
                        Bundle bundle = new Bundle();
                        try {
                            bundle.putString("name", locIndi.getString("name"));
                            bundle.putString("type", locIndi.getString("type"));
                            bundle.putString("dimension", locIndi.getString("dimension"));
                            bundle.putString("residents", locIndi.getJSONArray("residents").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            fragment.setArguments(bundle);
                            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack("New Fragment").commit();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


            case "ALL_EPISODES":
                AllEpisodesHolder AEH = (AllEpisodesHolder) holder;

                String key = newList.get(position).getKey();
                List<EpisodeData> value = newList.get(position).getValue();

                AEH.seasonNum.setText(key);

                break;


            case "ALL_RESIDENTS":
                AllResidentsHolder ARH = (AllResidentsHolder) holder;
                try {
                    String residentURL = data.getString(position);
//                    Log.d(TAG, "onBindViewHolder: " + residentURL);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, residentURL,
                            response -> {
                                try {
                                    charsIndi = new JSONObject(response);
                                    imgURL = charsIndi.getString("image");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "onResponse: " + e.getMessage());
                                } finally {
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(imgURL)
                                            .into(ARH.residentImage);
                                }
                            },
                            error -> {
                            });
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);

                    ARH.itemView.setOnClickListener(view -> {
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
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

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
        if (newList != null)
            return newList.size();
        return data.length();
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
        TextView planetName;

        public AllLocationsHolder(@NonNull View itemView) {
            super(itemView);
            planetName = itemView.findViewById(R.id.planetName);
        }
    }

    public static class AllResidentsHolder extends RecyclerView.ViewHolder {
        //        TextView residentName;
        ImageView residentImage;

        public AllResidentsHolder(@NonNull View itemView) {
            super(itemView);
            residentImage = itemView.findViewById(R.id.residentImage);
        }
    }

    public static class AllEpisodesHolder extends RecyclerView.ViewHolder {
        //fields
        ImageView dropdown;
        RecyclerView episodeNum;
        TextView seasonNum;

        public AllEpisodesHolder(@NonNull View itemView) {
            super(itemView);
            //initialize values
            dropdown = itemView.findViewById(R.id.drop_down);
            episodeNum = itemView.findViewById(R.id.episodeRV);
            seasonNum = itemView.findViewById(R.id.season_num);
        }
    }
}
