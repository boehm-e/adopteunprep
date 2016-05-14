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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 12/05/16.
 */
public class activityNotifications extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    public String login = "";
    private ListView listView;
    public ProgressBar notifLoading;

    public ArrayList<String> notificationsTitle = new ArrayList<String>();

    public ArrayList<String> arrayName = new ArrayList<String>();
    public ArrayList<String> arrayType = new ArrayList<String>();
    public ArrayList<String> arrayUe = new ArrayList<String>();

    public ArrayList<String> arrayUrl= new ArrayList<String>();
    public ArrayList<String> arrayLeader = new ArrayList<String>();
    public ArrayList<String> arrayMemo = new ArrayList<String>();




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
        JSONArray groupPropositions = obj.getJSONArray("groupPropositions");     // I HAVE MATCH THIS USERS

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
                    arrayUrl.add("");
                    arrayLeader.add("");
                    arrayMemo.add("");
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
                    arrayUrl.add("");
                    arrayLeader.add("");
                    arrayMemo.add("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i=0; i < groupPropositions.length(); i++) {
            Log.e("GROUP PROPOSITION", "PROPOSITION");
            try {
                JSONObject loginObject = groupPropositions.getJSONObject(i);
                String loginName = loginObject.getString("name");
                String loginUrl = loginObject.getString("url");
                String loginLeader = loginObject.getString("leader");
                String loginMemo = loginObject.getString("memo");

                arrayName.add(loginName);
                arrayType.add("");
                arrayUe.add("");
                arrayUrl.add(loginUrl);
                arrayLeader.add(loginLeader);
                arrayMemo.add(loginMemo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        Log.e("SIZE", arrayName.size()+"");
        Log.e("SIZE", arrayType.size()+"");
        Log.e("SIZE", arrayUe.size()+"");
        Log.e("SIZE", arrayUrl.size()+"");
        Log.e("SIZE", arrayLeader.size()+"");
        Log.e("SIZE", arrayMemo.size()+"");
    }



    public void initListView() {
        final NotificationsAdapter adapter = new NotificationsAdapter(activityNotifications.this, arrayName, arrayUe, arrayType, arrayUrl, arrayLeader, arrayMemo);

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
                                removeNotif(login, adapter.getUeName(position), adapter.getPerpetrator(position));
                            }
                        });

        listView.setOnTouchListener(touchListener);
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

    public void removeNotif(final String _username, final String _ueName, final String _perpetrator) {
        RequestQueue requestLogin = Volley.newRequestQueue(this);
        String url = "http://ccmobile-etna.cloudapp.net/login";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE", response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username", _username);
                MyData.put("ueName", _ueName);
                MyData.put("perpetrator", _perpetrator);
                return MyData;
            }
        };

        requestLogin.add(MyStringRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("START", "START");
        initListView();
        notifLoading.setVisibility(View.GONE);
    }

}
