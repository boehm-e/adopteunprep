package com.automation.epic.etnappli;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10/05/16.
 */
public class activitySetInformations extends AppCompatActivity {
    int i = 0;
    int rowIndex = 1;
    int colIndex = 0;
    private GridLayout gridLayout;
    private final OkHttpClient client = new OkHttpClient();

    EditText textDescription;
    EditText textLinkedin;
    EditText textGithub;


    public String login = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_informations);

        Intent intent = getIntent();
        login = intent.getStringExtra("username");

        Button addCompetance = (Button) findViewById(R.id.addCompetances);

        Button validateDescription = (Button) findViewById(R.id.validateDescription);
        Button validateLinkedin = (Button) findViewById(R.id.validateLinkedin);
        Button validateGithub = (Button) findViewById(R.id.validateGithub);
        Button validateCompetances = (Button) findViewById(R.id.validateCompetances);

        textDescription = (EditText) findViewById(R.id.textDescription);
        textLinkedin = (EditText) findViewById(R.id.textLinkedin);
        textGithub = (EditText) findViewById(R.id.textGithub);

        assert validateCompetances != null;
        validateCompetances.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                LinearLayout container = (LinearLayout) findViewById(R.id.container);
                int count = container.getChildCount();
                View view = null;
                final JSONArray JSONgrades = new JSONArray();
                for(int i=0; i<count; i++) {
                    view = container.getChildAt(i);
                    EditText competance = (EditText) ((LinearLayout) view).getChildAt(0);
                    EditText grade = (EditText) ((LinearLayout) view).getChildAt(1);

                    JSONObject currentObj = new JSONObject();

                    try {
                        if(competance.getText().toString() != "" && grade.getText().toString() != "") {
                            JSONObject obj = new JSONObject();
                            obj.put("name", competance.getText().toString());
                            obj.put("grade", grade.getText().toString());
                            JSONgrades.put(obj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                // SEND REQUEST

                String url = "http://ccmobile-etna.cloudapp.net/setSkills";
                RequestQueue requestLogin = Volley.newRequestQueue(activitySetInformations.this);
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE", response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            if (resp.getString("nModified") != "1") {
                                Toast.makeText(getApplicationContext(), "probleme lors de l'update de votre description", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "votre description a bien ete changee", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "probleme lors de l'update de votre description", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("username", login);
                        MyData.put("skills", JSONgrades.toString());
                        return MyData;
                    }
                };

                requestLogin.add(MyStringRequest);






                Log.e("COMPETANCE USERNAME    : ", login);
                Log.e("COMPETANCE JSON OBJECT : ", JSONgrades.toString());
            }
        });

        assert addCompetance != null;
        addCompetance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout container = (LinearLayout) findViewById(R.id.container);

                LinearLayout subContainer = new LinearLayout(getApplicationContext());
                subContainer.setLayoutParams(new ActionBar.LayoutParams(getPx(300), ViewGroup.LayoutParams.WRAP_CONTENT));

                EditText name = new EditText(getApplicationContext());
                EditText grade = new EditText(getApplicationContext());
                grade.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                name.setHint("Competance");
                grade.setHint("note (%)");
                name.setHintTextColor(Color.parseColor("#00B7EB"));
                grade.setHintTextColor(Color.parseColor("#00B7EB"));
                name.setTextColor(Color.parseColor("#00B7EB"));
                grade.setTextColor(Color.parseColor("#00B7EB"));

                container.setMinimumHeight(20);


                name.setLayoutParams(new ActionBar.LayoutParams(getPx(150), ViewGroup.LayoutParams.WRAP_CONTENT));
                grade.setLayoutParams(new ActionBar.LayoutParams(getPx(150), ViewGroup.LayoutParams.WRAP_CONTENT));

                subContainer.addView(name);
                subContainer.addView(grade);

                container.addView(subContainer, 0);
            }
        });


        assert validateDescription != null;
        validateDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("VALIDATE", "DESCRIPTION");
                final String description = textDescription.getText().toString();
                String url = "http://ccmobile-etna.cloudapp.net/setDescription";
                RequestQueue requestLogin = Volley.newRequestQueue(activitySetInformations.this);
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE", response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            if (resp.getString("nModified") != "1") {
                                Toast.makeText(getApplicationContext(), "probleme lors de l'update de votre description", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "votre description a bien ete changee", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "probleme lors de l'update de votre description", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("username", login);
                        MyData.put("description", description);
                        return MyData;
                    }
                };

                requestLogin.add(MyStringRequest);

            }
        });

        assert validateLinkedin != null;
        validateLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("VALIDATE", "LINKEDIN");
                final String linkedin = textLinkedin.getText().toString();
                String url = "http://ccmobile-etna.cloudapp.net/setLinkedin";
                RequestQueue requestLogin = Volley.newRequestQueue(activitySetInformations.this);
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE", response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            if (resp.getString("nModified") != "1") {
                                Toast.makeText(getApplicationContext(), "probleme lors de l'update de votre description", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "votre description a bien ete changee", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "probleme lors de l'update de votre description", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("username", login);
                        MyData.put("linkedin", linkedin);
                        return MyData;
                    }
                };

                requestLogin.add(MyStringRequest);

            }
        });

        assert validateGithub != null;
        validateGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("VALIDATE", "GITHUB");
                final String github = textGithub.getText().toString();
                String url = "http://ccmobile-etna.cloudapp.net/setGithub";
                RequestQueue requestLogin = Volley.newRequestQueue(activitySetInformations.this);
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE", response);
                        try {
                            JSONObject resp = new JSONObject(response);
                            if (resp.getString("nModified") != "1") {
                                Toast.makeText(getApplicationContext(), "probleme lors de l'update de votre description", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "votre description a bien ete changee", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "probleme lors de l'update de votre description", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("username", login);
                        MyData.put("github", github);
                        return MyData;
                    }
                };

                requestLogin.add(MyStringRequest);
            }
        });

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
        JSONObject userCard = Jobject.getJSONObject("userCard");

        String description = userCard.getString("description");
        JSONArray gradeArray = new JSONArray(userCard.getString("skills"));


        textDescription.setText(description);


        Log.e("GRADE ACTIVITYSETINFORMATION", gradeArray.toString());


        for (int i=0; i < gradeArray.length(); i++) {
            try {
                JSONObject obj = new JSONObject(gradeArray.get(i).toString());
                String stringGrade = obj.getString("grade").toString();
                String stringName = obj.getString("name").toString();

                Log.e("GRADE", stringGrade);
                Log.e("NAME", stringName);

                LinearLayout container = (LinearLayout) findViewById(R.id.container);

                LinearLayout subContainer = new LinearLayout(getApplicationContext());
                subContainer.setLayoutParams(new ActionBar.LayoutParams(getPx(300), ViewGroup.LayoutParams.WRAP_CONTENT));

                EditText name = new EditText(getApplicationContext());
                EditText grade = new EditText(getApplicationContext());
                grade.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                name.setText(stringName);
                grade.setText(stringGrade);
                name.setTextColor(Color.parseColor("#00B7EB"));
                grade.setTextColor(Color.parseColor("#00B7EB"));

                container.setMinimumHeight(20);


                name.setLayoutParams(new ActionBar.LayoutParams(getPx(150), ViewGroup.LayoutParams.WRAP_CONTENT));
                grade.setLayoutParams(new ActionBar.LayoutParams(getPx(150), ViewGroup.LayoutParams.WRAP_CONTENT));

                subContainer.addView(name);
                subContainer.addView(grade);

                container.addView(subContainer, 0);





            } catch (JSONException e) {
                e.printStackTrace();
            }
          }
    }

    public int getPx(int dimensionDp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

}



