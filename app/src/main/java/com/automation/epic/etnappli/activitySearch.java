package com.automation.epic.etnappli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by root on 10/05/16.
 */
public class activitySearch extends AppCompatActivity {

    public ArrayList<String> firstnameArray = new ArrayList<String>();
    public ArrayList<String> lastnameArray = new ArrayList<String>();
    public ArrayList<String> loginArray = new ArrayList<String>();

    public ListView listViewLogin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText searchLogin = (EditText) findViewById(R.id.searchLoginInput);
        listViewLogin = (ListView) findViewById(R.id.listViewLogin);

        assert searchLogin != null;
        searchLogin.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("TEXTCHANGED", s.toString());
                ArrayList<String> _firstnameArray = new ArrayList<String>();
                ArrayList<String> _lastnameArray = new ArrayList<String>();
                ArrayList<String> _loginArray = new ArrayList<String>();
                for (int i=0; i<loginArray.size(); i++) {
                    Log.e("FIRSTNAME: ",firstnameArray.get(i).toString());
                    Log.e("LASTNAME : ",lastnameArray.get(i).toString());
                    if (loginArray.get(i).toLowerCase().indexOf(s.toString().toLowerCase()) != -1 || firstnameArray.get(i).toLowerCase().indexOf(s.toString().toLowerCase()) != -1 || lastnameArray.get(i).toLowerCase().indexOf(s.toString().toLowerCase()) != -1 ) {
                        _firstnameArray.add(firstnameArray.get(i));
                        _lastnameArray.add(lastnameArray.get(i));
                        _loginArray.add(loginArray.get(i));
                    }
                }
                listViewLogin.setAdapter(new searchAdapter(activitySearch.this, _firstnameArray, _lastnameArray, _loginArray));
            }
        });
        getTrombi();
    }

    public void getTrombi() {
        final String url = "http://ccmobile-etna.cloudapp.net/getTrombi";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject obj = new JSONObject(response.get(i).toString());
                                Log.d("Response login    ", obj.get("login").toString());
                                Log.d("Response firstname", obj.get("firstname").toString());
                                Log.d("Response lastname ", obj.get("lastname").toString());

                                firstnameArray.add(obj.get("firstname").toString());
                                lastnameArray.add(obj.get("lastname").toString());
                                loginArray.add(obj.get("login").toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listViewLogin.setAdapter(new searchAdapter(activitySearch.this, firstnameArray, lastnameArray, loginArray));
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );
        queue.add(getRequest);
    }
}
