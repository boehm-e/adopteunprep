package com.automation.epic.etnappli;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class navigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<String> chooseProjectCategory = new ArrayList<>();
    public ArrayList<String> chooseProjectTitle = new ArrayList<>();
    public ArrayList<String> chooseProjectDateBegin = new ArrayList<>();
    public ArrayList<String> chooseProjectDateEnd = new ArrayList<>();
    public ListView listViewChooseProject = null;
    public String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");
        Log.e("LOGIN", "NAVDRAWER : " + login);

        listViewChooseProject = (ListView) findViewById(R.id.listViewProject);

        fillProjects();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fillProjects() {
        String url = "http://ccmobile-etna.cloudapp.net/getCurrentProjects";
        RequestQueue requestLogin = Volley.newRequestQueue(this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String name = jsonObject.getString("name");
                        String categ = jsonObject.getString("categ");
                        String start = jsonObject.getString("date_start");
                        String end = jsonObject.getString("date_end");
                        chooseProjectTitle.add(name);
                        chooseProjectCategory.add(categ);
                        chooseProjectDateBegin.add(start);
                        chooseProjectDateEnd.add(end);
                    }

                    listViewChooseProject.setAdapter(new chooseProjectAdapter(navigationDrawer.this, chooseProjectCategory, chooseProjectTitle, chooseProjectDateBegin, chooseProjectDateEnd));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "probleme d'authentification", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username", login);
                return MyData;
            }
        };

        requestLogin.add(MyStringRequest);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.fillMyCard) {
            Intent fillMyCard = new Intent(getApplicationContext(), activitySetInformations.class);
            fillMyCard.putExtra("username", login);
            startActivity(fillMyCard);
        } else if (id == R.id.search) {
            Intent searchLogin = new Intent(getApplicationContext(), activitySearch.class);
            startActivity(searchLogin);
        } else if (id == R.id.myCard) {
            Intent showMore = new Intent(navigationDrawer.this, moreContent.class);
            showMore.putExtra("login", login);
            startActivity(showMore);
        }else if (id == R.id.notifications) {
            Intent showMore = new Intent(navigationDrawer.this, activityNotifications.class);
            showMore.putExtra("login", login);
            startActivity(showMore);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
