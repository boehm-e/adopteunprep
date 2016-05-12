package com.automation.epic.etnappli;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 12/05/16.
 */
public class activityNotifications extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    public String login = "";
    private ListView listView;
    public ProgressBar notifLoading;

    public ArrayList<String> arrayName = new ArrayList<String>();
    public ArrayList<String> notificationsTitle = new ArrayList<String>();
    public ArrayList<String> arrayType = new ArrayList<String>();
    public ArrayList<String> arrayUe = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        notifLoading = (ProgressBar) findViewById(R.id.notifLoading);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        listView = (ListView) findViewById(R.id.notificationsListView);


        try {
            getData(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(String username) throws Exception {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url = "http://ccmobile-etna.cloudapp.net/getUserByUsername";
        RequestBody formBody = new FormEncodingBuilder()
                .add("username", username)
                .build();
        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        com.squareup.okhttp.Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String jsonData = response.body().string();
        JSONObject Jobject = new JSONObject(jsonData);
        JSONObject obj = Jobject.getJSONObject("application");

        arrayName = new ArrayList<String>();
        arrayType = new ArrayList<String>();
        arrayUe = new ArrayList<String>();


        JSONArray userIsSelected = obj.getJSONArray("userIsSelected");           // MY PROFILE IS MATCHED BY PEOPLE
        JSONArray commonMatches = obj.getJSONArray("commonMatches");             // CLEAR ENOUGH
        JSONArray userSelectMatches = obj.getJSONArray("userSelectMatches");     // I HAVE MATCH THIS USERS

        Log.e("userISSELECTED", userIsSelected.toString());

        for (int i=0; i < userIsSelected.length(); i++) {
            try {
                JSONObject loginObject = userIsSelected.getJSONObject(i);
                JSONArray loginArray = loginObject.getJSONArray("usernames");
                String ueName = loginObject.getString("name");
                for (int j=0; j<loginArray.length(); j++) {
                    arrayName.add(loginArray.getString(j));
                    arrayType.add("t'as matche");
                    arrayUe.add(ueName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i=0; i < commonMatches.length(); i++) {
            try {
                JSONObject loginObject = commonMatches.getJSONObject(i);
                JSONArray loginArray = loginObject.getJSONArray("usernames");
                String ueName = loginObject.getString("name");
                for (int j=0; j<loginArray.length(); j++) {
                    arrayName.add(loginArray.getString(j));
                    arrayType.add(" et vous vous etes matche mutuellement");
                    arrayUe.add(ueName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i=0; i < userSelectMatches.length(); i++) {
            try {
                JSONObject loginObject = userSelectMatches.getJSONObject(i);
                JSONArray loginArray = loginObject.getJSONArray("usernames");
                String ueName = loginObject.getString("name");
                for (int j=0; j<loginArray.length(); j++) {
                    arrayName.add(loginArray.getString(j));
                    arrayType.add("est un user que tu as match");
                    arrayUe.add(ueName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Log.e("FINALLLLLLL", arrayName.toString());
        Log.e("FINALLLLLLL", arrayType.toString());
        Log.e("FINALLLLLLL", arrayUe.toString());

    }



    public void initListView() {
        final NotificationsAdapter adapter = new NotificationsAdapter(activityNotifications.this, arrayName, arrayUe, arrayType);
        listView.setAdapter(adapter);
        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                adapter.remove(position);
                            }
                        });

//        touchListener.setDismissDelay(TIME_TO_AUTOMATICALLY_DISMISS_ITEM);
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(activityNotifications.this, "Position " + position, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("START", "START");
        initListView();
        notifLoading.setVisibility(View.GONE);
    }

}
