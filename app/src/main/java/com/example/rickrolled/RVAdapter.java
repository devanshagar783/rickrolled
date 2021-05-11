package com.example.rickrolled;

import android.content.Context;
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

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyviewHolder> {

    JSONArray characters;
    Context context;
    private static final String TAG = "RVAdapter";

    public RVAdapter(Context context, JSONArray characters) {
        this.context = context;
        this.characters = characters;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.char_view, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.MyviewHolder holder, int position) {
        try {
            JSONObject charsIndi = characters.getJSONObject(position);
            holder.charname.setText(charsIndi.getString("name"));
            holder.chargender.setText(charsIndi.getString("gender"));
            holder.charstatus.setText(charsIndi.getString("status"));
            holder.charspecies.setText(charsIndi.getString("species") + "   " + charsIndi.getString("type"));
            holder.charorigin.setText(charsIndi.getJSONObject("origin").getString("name"));
            holder.charlastknown.setText(charsIndi.getJSONObject("location").getString("name"));

            String imgURL = charsIndi.getString("image");
            Glide.with(context)
                    .asBitmap()
                    .load(imgURL)
                    .into(holder.charimage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
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
                    }
                    fragment.setArguments(bundle);
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack("New Fragment").commit();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return characters.length();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView charname, chargender, charstatus, charspecies, charorigin, charlastknown;
        ImageView charimage;

        public MyviewHolder(@NonNull View itemView) {
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
}
