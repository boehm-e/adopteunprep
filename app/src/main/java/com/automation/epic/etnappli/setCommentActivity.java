package com.automation.epic.etnappli;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 11/05/16.
 */
public class setCommentActivity extends AppCompatActivity {

    public ImageView picture;
    public TextView login;
    public TextView comment;
    public RatingBar ratingBar;
    public Button send;
    public String username;
    public String selfUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_comment);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        selfUsername = intent.getStringExtra("selfUsername");

        picture = (ImageView) findViewById(R.id.setCommentPicture);
        login = (TextView) findViewById(R.id.setCommentLogin);
        comment = (TextView) findViewById(R.id.setCommentComment);
        ratingBar = (RatingBar) findViewById(R.id.setCommentRating);
        send = (Button) findViewById(R.id.setCommentSend);

        Picasso.with(getApplicationContext()).load("https://intra.etna-alternance.net/report/trombi/image/login/"+username).into(picture);
        login.setText(username);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("USERNAME", username);
                Log.e("SELF_USERNAME", selfUsername);
                postData();
            }
        });

    }

    private void postData() {
        final String _comment = comment.getText().toString();
        final String _rating = Float.toString(ratingBar.getRating()*4);
        Log.e("SELFUSERNAMEEEEEEEEEE", selfUsername);
        Log.e("USERNAMEEEEEEEEEEEEEE", username);

        String url = "http://ccmobile-etna.cloudapp.net/setComment";

        RequestQueue requestLogin = Volley.newRequestQueue(this);

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Response: " + response.toString());
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Erreur lors de l'envoie de votre message, veuillez reessayer plus tard", Toast.LENGTH_LONG);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("selfUsername", selfUsername);
                params.put("username", username);
                params.put("comment", _comment);
                params.put("grade", _rating);
                return params;
            }

        };
        requestLogin.add(strReq);
    }
}
