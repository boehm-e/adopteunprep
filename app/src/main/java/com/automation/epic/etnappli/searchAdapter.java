package com.automation.epic.etnappli;

/**
 * Created by root on 04/05/16.
 */
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class searchAdapter extends BaseAdapter{
    ArrayList<String> firstname;
    ArrayList<String> lastname;
    ArrayList<String> login;

    Context context;
    private static LayoutInflater inflater=null;

    public searchAdapter(activitySearch gradeContent, ArrayList<String> _firstname, ArrayList<String> _lastname, ArrayList<String> _login) {
        firstname = _firstname;
        lastname =_lastname;
        login = _login;
        context=gradeContent;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return login.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tvFirstname;
        TextView tvLastname;
        TextView tvLogin;
        ImageView imLogin;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.single_row_search_login, null);

        holder.tvFirstname = (TextView) rowView.findViewById(R.id.searchFirstname);
        holder.tvLastname  = (TextView) rowView.findViewById(R.id.searchLastname);
        holder.tvLogin = (TextView) rowView.findViewById(R.id.searchLogin);
        holder.imLogin = (ImageView) rowView.findViewById(R.id.searchImage);

        holder.tvFirstname.setText(firstname.get(position));
        holder.tvLastname.setText(lastname.get(position));
        holder.tvLogin.setText(login.get(position));
        Picasso.with(context)
                .load("https://intra.etna-alternance.net/report/trombi/image/login/" + login.get(position))
                .into(holder.imLogin);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMore = new Intent(context, moreContent.class);
                showMore.putExtra("login", login.get(position));
                context.startActivity(showMore);
                Toast.makeText(context, "You Clicked "+login.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        return rowView;
    }

}