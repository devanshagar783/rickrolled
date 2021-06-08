package com.example.rickrolled;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private final String CHAR_URL = "https://rickandmortyapi.com/api/character";

    private ProgressBar bar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Fragment fragment;

    JSONObject jsonObject;
    JSONArray jsonArray;
    int random, count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = findViewById(R.id.progresshome);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.hometoolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.draweropen,
                R.string.drawerclose
        );

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getjson();
    }

    public void getjson() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CHAR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject = new JSONObject(response);
                            count = Integer.parseInt(jsonObject.getJSONObject("info").getString("count"));

                            Random rand = new Random();
                            random = rand.nextInt(count) + 1;
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, CHAR_URL + "/" + String.valueOf(random),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject object = new JSONObject(response);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("name", object.getString("name"));
                                                bundle.putString("gender", object.getString("gender"));
                                                bundle.putString("species", object.getString("species"));
                                                bundle.putString("status", object.getString("status"));
                                                bundle.putString("origin", object.getJSONObject("origin").getString("name"));
                                                bundle.putString("location", object.getJSONObject("location").getString("name"));
                                                bundle.putString("image", object.getString("image"));
                                                fragment = new InfoFragment();
                                                fragment.setArguments(bundle);
                                                bar.setVisibility(View.GONE);
                                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack("Home Fragment").commit();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    error -> {
                                    });
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            queue.add(stringRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: " + e.getMessage());
                        } finally {
                        }
                    }
                },
                error -> {
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            startActivity(new Intent(this, EndSplash.class));
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_characters:
                fragment = new AllCharactersFragment();
                break;

            case R.id.all_locations:
                fragment = new AllLocationsFragment();
                break;

            case R.id.all_episodes:
                fragment = new AllEpisodesFragment();
                break;

            case R.id.favourite:
                fragment = new FavouriteEpisodesFragment();

            case R.id.family:
                fragment = new FamilyTreeFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack("All Character Fragment").commit();
        drawerLayout.closeDrawers();
        return true;
    }
}