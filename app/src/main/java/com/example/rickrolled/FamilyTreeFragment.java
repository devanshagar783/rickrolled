package com.example.rickrolled;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class FamilyTreeFragment extends Fragment {

    private static final String TAG = "FamilyTreeFragment";
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
                        view.setOnClickListener(view1 -> {
                            FamilyTreeFragmentDirections.ActionFamilyTreeFragmentToCharacterFragment action = FamilyTreeFragmentDirections.actionFamilyTreeFragmentToCharacterFragment();
                            try {
                                action.setName(object.getString("name"));
                                action.setGender(object.getString("gender"));
                                action.setSpecies(object.getString("species"));
                                action.setStatus(object.getString("status"));
                                action.setOrigin(object.getJSONObject("origin").getString("name"));
                                action.setLocation(object.getJSONObject("location").getString("name"));
                                action.setImage(object.getString("image"));
                                Navigation.findNavController(view1).navigate(action);
                            } catch (JSONException e) {
                                e.printStackTrace();
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