package com.automation.epic.etnappli;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daprlabs.cardstack.SwipeDeck;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public String login = "";
    public String ueName = "";
    private final OkHttpClient client = new OkHttpClient();
    public ArrayList<cardContent> testData = new ArrayList<cardContent>();
    public JSONObject jsonObj;
    public JSONArray finalMatch = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");
        Log.e("MAINNNNNNNNNNNNNNNNNNNN", login);
        ueName = intent.getStringExtra("ueName");

        Log.e("LOGIN", "ACCTIVITY STARTED LOGIN  : "+login);
        Log.e("LOGIN", "ACCTIVITY STARTED UENAME : "+ueName);

        // cardview
        final SwipeDeck cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);


        jsonObj = new JSONObject();
        try {
            jsonObj.put("name", "michel");
            jsonObj.put("surname", "sardou");
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        testData.add(new cardContent("https://intra.etna-alternance.net/report/trombi/image/login/gueye_o", new String[] {"A", "B", "C"}, "gueye_o", "bastien", "chevallier", "description", jsonObj, "15"));


        try {
            fillCards(login, ueName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SwipeDeckAdapter adapter = new SwipeDeckAdapter(testData, this);
        cardStack.setLeftImage(R.id.left_image);
        cardStack.setRightImage(R.id.right_image);
        cardStack.setAdapter(adapter);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {

            @Override
            public void cardSwipedRight(int position) {
//                Toast.makeText(getApplicationContext(), "ACCEPTE " + testData.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                JSONObject newAccept = new JSONObject();
                try {
                    newAccept.put("login", testData.get(position).getUsername());
                    newAccept.put("grade", testData.get(position).getGeneralNote());
                    newAccept.put("description", testData.get(position).getDescription());
                    newAccept.put("picture", testData.get(position).getPicture());
                    finalMatch.put(newAccept);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sendMatchRequest(login,testData.get(position).getUsername(),ueName);
            }

            @Override
            public void cardSwipedLeft(int position) {
//                Toast.makeText(getApplicationContext(),"REFUSE "+testData.get(position).getUsername(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardsDepleted() {
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.showFinalMatch);

                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                recyclerView.setLayoutManager(layoutManager);

                final FinalMatchAdapter adapter = new FinalMatchAdapter(getApplicationContext(),finalMatch);
                recyclerView.setAdapter(adapter);

                Button createGroup = (Button) findViewById(R.id.createGroup);
                createGroup.setVisibility(View.VISIBLE);
                createGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> finalLoginArray= adapter.getFinalLoginArray();
                        JSONObject group = new JSONObject();
                        JSONArray jsArray = new JSONArray(finalLoginArray);
                        Log.e("YOUR GROUP", jsArray.toString());
                        Log.e("ueNAME", ueName.toString());
                        createGroupIntra(login, ueName, jsArray.toString(),"petite note");

                    }
                });

                Log.e("NOMORE", "NO MORE CARDS");
                Log.e("NOMORE", finalMatch.toString());
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });
    }

    public void createGroupIntra (final String _username, final String _selectedUsername, final String _ueName, final String _petiteNote) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://ccmobile-etna.cloudapp.net/createGroup";
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(getApplicationContext(), "TON GROUPE A BIEN ETE CREE SUR L'INTRA", Toast.LENGTH_SHORT);
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", _username);
                params.put("ueName", _selectedUsername);
                params.put("mates", _ueName);
                params.put("memo", _petiteNote);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void sendMatchRequest(final String _username, final String _selectedUsername, final String _ueName) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://ccmobile-etna.cloudapp.net/selectMatches";
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(getApplicationContext(), _selectedUsername+" a bien ete ajoute a vos match", Toast.LENGTH_SHORT).show();
                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", _username);
                params.put("selectedUsername", _selectedUsername);
                params.put("ueName", _ueName);

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void fillCards2(String _username, String _ueName) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestBody formBody = new FormEncodingBuilder()
                .add("username", _username)
                .add("ueName", _ueName)
                .build();
        Request request = new Request.Builder()
                .url("http://ccmobile-etna.cloudapp.net/getUsersDispoUE")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        Log.e("ABCDEFGHI", response.body().string());
    }

    public void fillCards(String _username, String _ueName) throws JSONException, IOException {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//
//            String url = "http://ccmobile-etna.cloudapp.net/getAllUsersCard";
//            RequestBody formBody = new FormEncodingBuilder()
//                    .build();
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            Response response = client.newCall(request).execute();
//         if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestBody formBody = new FormEncodingBuilder()
                .add("username", _username)
                .add("ueName", _ueName)
                .build();
        Request request = new Request.Builder()
                .url("http://ccmobile-etna.cloudapp.net/getUsersDispoUE")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String jsonData = response.body().string();
        Log.e("RESPONSE ABCDEF", jsonData);
        JSONArray jsonArray = new JSONArray(jsonData);

        //        testData.add(new cardContent("https://intra.etna-alternance.net/report/trombi/image/login/gueye_o", new String[] {"A", "B", "C"}, "gueye_o", "bastien", "chevallier", "description", jsonObj, "15"));


        for (int i=0; i<jsonArray.length(); i++) {
                Log.e("TEST", jsonArray.get(i).toString());
                JSONObject obj = (JSONObject) jsonArray.get(i);
                String picture = obj.getString("picture");
                String firstName = obj.getString("firstName");
                String lastName = obj.getString("lastName");
                String username = obj.getString("username");
                String description =  obj.getString("description");

                JSONObject jsobj = new JSONObject(obj.get("generalGrade").toString());
                String flt = jsobj.getString("grade");
                String generalGrade = Float.toString(Float.parseFloat(Float.toString(Math.round(Float.parseFloat(flt) * 100)))/100);

                JSONArray arr = obj.getJSONArray("badges");
                ArrayList<String> badges = new ArrayList<String>();
                for(int j = 0; j < arr.length(); j++){
                    badges.add(arr.getString(j));
                }

                testData.add(new cardContent(picture, badges, username, lastName, firstName, description, jsonObj, generalGrade));
            }
    }


    public class SwipeDeckAdapter extends BaseAdapter {

        private ArrayList<cardContent> data;
        private Context context;

        public SwipeDeckAdapter(ArrayList<cardContent> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if(v == null){
                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.card_view, parent, false);
            }


            TextView tvGrade = (TextView) v.findViewById(R.id.tvGrade);
            TextView tvLogin = (TextView) v.findViewById(R.id.tvLogin);
            TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            Button seeMore = (Button) v.findViewById(R.id.seeMore);

            tvGrade.setText(data.get(position).getGeneralNote());
            tvLogin.setText(data.get(position).getUsername());
            tvDescription.setText(data.get(position).getDescription());

            Picasso.with(context)
                    .load(data.get(position).getPicture())
                    .into((ImageView) v.findViewById(R.id.loginPicture));

            Log.e("position", "POS : "+position);
            seeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String _login = data.get(position).getUsername();
                    Log.i("MainActivity", _login);

                    Intent showMore = new Intent(getApplicationContext(), moreContent.class);
                    showMore.putExtra("login", _login);
                    showMore.putExtra("myLogin", login);
                    startActivity(showMore);

                }
            });

            return v;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
