package com.automation.epic.etnappli;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView navMail = (TextView) header.findViewById(R.id.navMail);
        TextView navLogin= (TextView) header.findViewById(R.id.navLogin);

        navMail.setText(login+"@etna-alternance.net");
        navLogin.setText(login);

        Picasso.with(getApplicationContext()).load("https://intra.etna-alternance.net/report/trombi/image/login/"+login).into((ImageView) header.findViewById(R.id.imageViewHeader));

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
        }else if (id == R.id.share_facebook) {
            String url = "https://drive.google.com/file/d/0B5SdMY_E9YBJWm5TYzlsU0sxRGs/view?usp=sharing";
            shareFacebook(url);
        }else if (id == R.id.share_twitter) {
            String url = "https://drive.google.com/file/d/0B5SdMY_E9YBJWm5TYzlsU0sxRGs/view?usp=sharing";
            shareTwitter(url);
        }else if (id == R.id.share_gmail) {
            String url = "https://drive.google.com/file/d/0B5SdMY_E9YBJWm5TYzlsU0sxRGs/view?usp=sharing";
            shareGmail(url);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void shareGmail(String url) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "test1");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
        shareIntent.putExtra(Intent.EXTRA_ORIGINATING_URI, url);

        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("android.gm")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                startActivity(shareIntent);
                break;
            }
        }
    }

    private void shareTwitter(String url) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "test1");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
        shareIntent.putExtra(Intent.EXTRA_ORIGINATING_URI, url);

        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("twitter")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                startActivity(shareIntent);
                break;
            }
        }
    }

    private void shareFacebook(String url) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "test1");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
        shareIntent.putExtra(Intent.EXTRA_ORIGINATING_URI, url);

        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                startActivity(shareIntent);
                break;
            }
        }
    }
}
