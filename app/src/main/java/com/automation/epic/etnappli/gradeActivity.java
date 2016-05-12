package com.automation.epic.etnappli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by root on 04/05/16.
 */
public class gradeActivity extends AppCompatActivity {

    ArrayList<String> comment = new ArrayList<String>();
    ArrayList<String> grade = new ArrayList<String>();
    ArrayList<String> name = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);


        Bundle b = getIntent().getExtras();
        String Array=b.getString("Array");
        try {
            JSONArray array = new JSONArray(Array);
            for (int i=0; i<array.length(); i++) {
                JSONObject _this = new JSONObject(array.get(i).toString());
                Log.e("ARRAY", _this.toString());
                comment.add(_this.get("comment").toString());
                grade.add(_this.get("grade").toString());
                name.add(_this.get("name").toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView gradeListView = (ListView) findViewById(R.id.listViewGrades);
        gradeListView.setNestedScrollingEnabled(true);
        gradeListView.setAdapter(new gradeAdapter(this, comment, grade, name));

    }
}
