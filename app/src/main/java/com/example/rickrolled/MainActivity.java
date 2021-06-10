package com.example.rickrolled;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String CHAR_URL = "https://rickandmortyapi.com/api/character";

//    private ProgressBar progressBar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
// <<<<<<< main
    private Fragment fragment;
    private TextView randomQuotes;
    private String [] quotes;
// =======
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
// >>>>>>> navigationBranch

    private int flag = 0;
    JSONObject jsonObject;
    int random, count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        progressBar = findViewById(R.id.progresshome);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.hometoolbar);
// <<<<<<< main
        randomQuotes = findViewById(R.id.randomQuote);
        quotes = getResources().getStringArray(R.array.menuQuotes);

        int randomIndex = new Random().nextInt(quotes.length);
        randomQuotes.setText(quotes[randomIndex]);

//         ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
//                 this,
//                 drawerLayout,
//                 toolbar,
//                 R.string.draweropen,
//                 R.string.drawerclose
//         );

//         drawerLayout.setDrawerListener(actionBarDrawerToggle);
//         actionBarDrawerToggle.syncState();
//         navigationView.setNavigationItemSelectedListener(this);
// =======
        setSupportActionBar(toolbar);
// >>>>>>> navigationBranch

        getjson();
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.infoFragment, R.id.familyTreeFragment, R.id.allCharactersFragment, R.id.allLocationsFragment)
                .setDrawerLayout(drawerLayout)
                .build();
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
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
                                                navController.setGraph(R.navigation.drawer_nav, bundle);
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
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            startActivity(new Intent(this, EndSplash.class));
//            finish();
//        }
    }

// <<<<<<< main
//     @Override
//     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//         switch (item.getItemId()) {
//             case R.id.all_characters:
//                 fragment = new AllCharactersFragment();
//                 break;

//             case R.id.all_locations:
//                 fragment = new AllLocationsFragment();
//                 break;

//             case R.id.all_episodes:
//                 fragment = new AllEpisodesFragment();
//                 break;

//             case R.id.family:
//                 fragment = new FamilyTreeFragment();
//                 break;
//         }
//         getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack("All Character Fragment").commit();
//         drawerLayout.closeDrawers();
//         return true;
//     }
// =======
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
////            case R.id.all_characters:
////                fragment = new AllCharactersFragment();
////                break;
////
////            case R.id.all_locations:
////                fragment = new AllLocationsFragment();
////                break;
////
////            case R.id.family:
////                fragment = new FamilyTreeFragment();
////                break;
//        }
////        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack("All Character Fragment").commit();
//        drawerLayout.closeDrawers();
//        return true;
//    }
// >>>>>>> navigationBranch
}