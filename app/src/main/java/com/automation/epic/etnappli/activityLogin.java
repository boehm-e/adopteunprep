package com.automation.epic.etnappli;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 03/05/16.
 */

public class activityLogin extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView tvLogin = (TextView) findViewById(R.id.tvUsername);
        final TextView tvPassword = (TextView) findViewById(R.id.tvPassword);
        Button send = (Button) findViewById(R.id.loginButton);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(tvLogin.getText().toString(), tvPassword.getText().toString());
            }
        });
    }



    private void login(final String username, final String password) {


        RequestQueue requestLogin = Volley.newRequestQueue(this);
        Log.e("USERNAME", username);
        Log.e("PASSWORD", password);

        String url = "http://ccmobile-etna.cloudapp.net/login";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE", response);
                Toast.makeText(getApplicationContext(), "Bonjour "+username, Toast.LENGTH_SHORT).show();

                Intent chooseProject = new Intent(getApplicationContext(), navigationDrawer.class);
                chooseProject.putExtra("login", username);
                startActivity(chooseProject);

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "probleme d'authentification", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username", username); //Add the data you'd like to send to the server.
                MyData.put("password", password); //Add the data you'd like to send to the server.
                return MyData;
            }
        };

        requestLogin.add(MyStringRequest);
    }
}
