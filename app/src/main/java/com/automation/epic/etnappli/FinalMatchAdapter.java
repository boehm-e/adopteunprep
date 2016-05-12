package com.automation.epic.etnappli;

/**
 * Created by root on 11/05/16.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FinalMatchAdapter extends RecyclerView.Adapter<FinalMatchAdapter.ViewHolder> {
    private JSONArray data;
    private Context context;
    public  int MAX_PEOPLE = 4;

    public ArrayList<String> finalLoginArray = new ArrayList<String>();

    public FinalMatchAdapter(Context context, JSONArray data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public FinalMatchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_row_final_match, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FinalMatchAdapter.ViewHolder viewHolder, final int i) {
        try {
            final JSONObject obj = data.getJSONObject(i);
            viewHolder.login.setText(obj.getString("login"));
            Picasso.with(context).load("https://intra.etna-alternance.net/report/trombi/image/login/"+obj.getString("login")).into(viewHolder.picture);



            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        Log.e("CHECKED", "LOGIN "+obj.getString("login")+" : "+isChecked);

                        if (isChecked == true) {
                            if (finalLoginArray.size() < MAX_PEOPLE-1)
                                finalLoginArray.add(obj.getString("login"));
                            else {
                                viewHolder.checkBox.setChecked(false);
                                Toast.makeText(context, "Le groupe doit etre constitue de "+MAX_PEOPLE+" personnes", Toast.LENGTH_SHORT);
                            }

                        } else if (isChecked == false) {
                                finalLoginArray.remove(finalLoginArray.indexOf(obj.getString("login")));
                        }

                        Log.e("ARRAY", finalLoginArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getFinalLoginArray() {
        return finalLoginArray;
    }

    public Object getItem(int position) {
        try {
            return data.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView login;
        private ImageView picture;
        private CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            login = (TextView)view.findViewById(R.id.finalMatchLogin);
            picture = (ImageView) view.findViewById(R.id.finalMatchPicture);
            checkBox = (CheckBox) view.findViewById(R.id.checkLogin);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.toggle();
                }
            });
        }
    }

}