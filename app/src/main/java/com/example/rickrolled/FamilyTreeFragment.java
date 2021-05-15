package com.example.rickrolled;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class FamilyTreeFragment extends Fragment {

    private static final String INDICHAR_URL = "https://rickandmortyapi.com/api/character/";
    private View v;
    private ImageView rick, beth, jerry, morty, summer;

    public FamilyTreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_family_tree, container, false);
        rick = v.findViewById(R.id.rickimg);
        beth = v.findViewById(R.id.bethimage);
        jerry = v.findViewById(R.id.jerryimage);
        morty = v.findViewById(R.id.mortyimage);
        summer = v.findViewById(R.id.summerimage);
        getimg("1", rick);
        getimg("2", morty);
        getimg("3", summer);
        getimg("4", beth);
        getimg("5", jerry);
        return v;
    }

    private void getimg(String no, ImageView view) {
        StringRequest request = new StringRequest(Request.Method.GET, INDICHAR_URL + no,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        setimg(object.getString("image"), view);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", object.getString("name"));
                        bundle.putString("gender", object.getString("gender"));
                        bundle.putString("species", object.getString("species"));
                        bundle.putString("status", object.getString("status"));
                        bundle.putString("origin", object.getJSONObject("origin").getString("name"));
                        bundle.putString("location", object.getJSONObject("location").getString("name"));
                        bundle.putString("image", object.getString("image"));
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view1) {
                                CharacterFragment fragment = new CharacterFragment();
                                fragment.setArguments(bundle);
                                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack("New Fragment").commit();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void setimg(String url, ImageView view) {
        Glide.with(getContext())
                .asBitmap()
                .load(url)
                .into(view);
    }
}