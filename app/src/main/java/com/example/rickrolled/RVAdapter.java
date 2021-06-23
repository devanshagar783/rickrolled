package com.example.rickrolled;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    JSONArray data;
    Context context;
    String adapterType;
    JSONObject charsIndi;
    String imgURL;
    List<Map.Entry<String, List<EpisodeData>>> newList;
    List<EpisodeData> episodes;
    NavController controller;
    List<String> characters;

    private static final String TAG = "RVAdapter";

    public RVAdapter(Context context, JSONArray data, String adapterType) {
        this.context = context;
        this.data = data;
        this.adapterType = adapterType;
    }

    public RVAdapter(Context context, LinkedHashMap<String, List<EpisodeData>> map, String adapterType) {
        this.context = context;
        this.adapterType = adapterType;
        newList = new ArrayList<>(map.entrySet());
    }

    public RVAdapter(Context context, String adapterType, List<EpisodeData> episodes) {
        this.context = context;
        this.adapterType = adapterType;
        this.episodes = episodes;
    }

    public RVAdapter(Context context, String adapterType, List<String> characters, NavController controller) {
        this.context = context;
        this.characters = characters;
        this.adapterType = adapterType;
        this.controller = controller;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (adapterType) {
            case "ALL_CHARACTERS":
                return new AllCharactersHolder(inflater.inflate(R.layout.char_view, parent, false));
            case "ALL_LOCATIONS":
                return new AllLocationsHolder(inflater.inflate(R.layout.all_location_card, parent, false));
            case "ALL_EPISODES":
                return new AllEpisodesHolder(inflater.inflate(R.layout.all_ep_card, parent, false));
            case "ALL_RESIDENTS":
                return new AllResidentsHolder(inflater.inflate(R.layout.residents_rv_card, parent, false));
            case "ONE_EPISODE":
                return new OneEpisodeHolder(inflater.inflate(R.layout.ep_view, parent, false));
            case "FAV_EPISODE":
                return new FavEpisodeHolder(inflater.inflate(R.layout.fav_ep_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (adapterType) {
            case "ALL_CHARACTERS":
                AllCharactersHolder ACH = (AllCharactersHolder) holder;
                try {
                    JSONObject charsIndi = data.getJSONObject(position);
                    ACH.charname.setText(charsIndi.getString("name"));
                    ACH.chargender.setText(charsIndi.getString("gender"));
                    ACH.charstatus.setText(charsIndi.getString("status"));
                    ACH.charspecies.setText(String.format(context.getResources().getString(R.string.charSpecies_placeholder), charsIndi.getString("species"), charsIndi.getString("type")));
                    ACH.charorigin.setText(charsIndi.getJSONObject("origin").getString("name"));
                    ACH.charlastknown.setText(charsIndi.getJSONObject("location").getString("name"));

                    String imgURL = charsIndi.getString("image");
                    Glide.with(context)
                            .asBitmap()
                            .load(imgURL)
                            .into(ACH.charimage);

                    ACH.itemView.setOnClickListener(view -> {
                        AllCharactersFragmentDirections.ActionAllCharactersFragmentToCharacterFragment action = AllCharactersFragmentDirections.actionAllCharactersFragmentToCharacterFragment();
                        try {
                            action.setName(charsIndi.getString("name"));
                            action.setGender(charsIndi.getString("gender"));
                            action.setSpecies(charsIndi.getString("species"));
                            action.setStatus(charsIndi.getString("status"));
                            action.setOrigin(charsIndi.getJSONObject("origin").getString("name"));
                            action.setLocation(charsIndi.getJSONObject("location").getString("name"));
                            action.setImage(charsIndi.getString("image"));
                            Navigation.findNavController(view).navigate(action);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                        AllLocationsFragmentDirections.ActionAllLocationsFragmentToLocationInfo action = AllLocationsFragmentDirections.actionAllLocationsFragmentToLocationInfo();
                        try {
                            action.setName(locIndi.getString("name"));
                            action.setType(locIndi.getString("type"));
                            action.setDimension(locIndi.getString("dimension"));
                            action.setResidents(locIndi.getJSONArray("residents").toString());
                            Navigation.findNavController(view).navigate(action);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                AEH.dropdown.getLayoutParams().width = 100;
                AEH.episodeNum.setVisibility(View.GONE);
                RVAdapter rva = new RVAdapter(context, context.getResources().getString(R.string.oneEpisode), value);
                AEH.episodeNum.setAdapter(rva);
                AEH.episodeNum.setLayoutManager(new LinearLayoutManager(context));
                Animation arrowAnimShow = AnimationUtils.loadAnimation(context, R.anim.dropdown_arrow);
                Animation arrowAnimCollapse = AnimationUtils.loadAnimation(context, R.anim.drppdown_arrow_collapse);
                AEH.itemView.setOnClickListener(v -> {
                    if (AEH.episodeNum.getVisibility() == View.GONE) {
                        AEH.dropdown.startAnimation(arrowAnimShow);
                        AEH.episodeNum.setVisibility(View.VISIBLE);
                    } else {
                        AEH.dropdown.startAnimation(arrowAnimCollapse);
                        AEH.episodeNum.setVisibility(View.GONE);
                    }
                });
                break;

            case "ONE_EPISODE":
                OneEpisodeHolder OEH = (OneEpisodeHolder) holder;
                OEH.name.setText(episodes.get(position).getName());
                OEH.itemView.setOnClickListener(v -> {
                    AllEpisodesFragmentDirections.ActionAllEpisodesFragmentToEpisodeFragment action = AllEpisodesFragmentDirections.actionAllEpisodesFragmentToEpisodeFragment(episodes.get(position));
                    Navigation.findNavController(v).navigate(action);
                });
                break;

            case "ALL_RESIDENTS":
                AllResidentsHolder ARH = (AllResidentsHolder) holder;
                try {
                    String residentURL = "";
                    if (data != null)
                        residentURL = data.getString(position);
                    if (characters.size() > 0)
                        residentURL = characters.get(position);
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
                        LocationInfoDirections.ActionLocationInfoToCharacterFragment action = null;
                        EpisodeFragmentDirections.ActionEpisodeFragmentToCharacterFragment action1 = null;
                        if (data != null) {
                            action = LocationInfoDirections.actionLocationInfoToCharacterFragment();
                            try {
                                action.setName(charsIndi.getString("name"));
                                action.setGender(charsIndi.getString("gender"));
                                action.setSpecies(charsIndi.getString("species"));
                                action.setStatus(charsIndi.getString("status"));
                                action.setOrigin(charsIndi.getJSONObject("origin").getString("name"));
                                action.setLocation(charsIndi.getJSONObject("location").getString("name"));
                                action.setImage(charsIndi.getString("image"));
                                Navigation.findNavController(view).navigate(action);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            action1 = EpisodeFragmentDirections.actionEpisodeFragmentToCharacterFragment();
                            try {
                                action1.setName(charsIndi.getString("name"));
                                action1.setGender(charsIndi.getString("gender"));
                                action1.setSpecies(charsIndi.getString("species"));
                                action1.setStatus(charsIndi.getString("status"));
                                action1.setOrigin(charsIndi.getJSONObject("origin").getString("name"));
                                action1.setLocation(charsIndi.getJSONObject("location").getString("name"));
                                action1.setImage(charsIndi.getString("image"));
                                Navigation.findNavController(view).navigate(action1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "FAV_EPISODE":
                FavEpisodeHolder FEH = (FavEpisodeHolder)holder;
                FEH.name.setText(episodes.get(position).getName());
                Log.d(TAG, "onBindViewHolder: called" + episodes.get(position));
                FEH.episode.setText(episodes.get(position).getEpisode());
                FEH.itemView.setOnClickListener(v -> {
                    FavouriteEpisodesFragmentDirections.ActionFavouriteEpisodesFragmentToEpisodeFragment action = FavouriteEpisodesFragmentDirections.actionFavouriteEpisodesFragmentToEpisodeFragment(episodes.get(position));
                    Navigation.findNavController(v).navigate(action);
                });
        }
    }

    @Override
    public int getItemCount() {
        if (newList != null)
            return newList.size();
        if (episodes != null)
            return episodes.size();
        if (characters != null)
            return characters.size();
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

    public static class OneEpisodeHolder extends RecyclerView.ViewHolder {

        TextView name;

        public OneEpisodeHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.episode_name);
        }
    }

    public static class FavEpisodeHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView episode;


        public FavEpisodeHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.fav_ep_name);
            episode = itemView.findViewById(R.id.fav_ep_no);
        }
    }
}
