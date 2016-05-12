package com.automation.epic.etnappli;

/**
 * Created by root on 04/05/16.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class commentAdapter extends BaseAdapter{
    Context context;
    public ArrayList<String> arrayLogin = new ArrayList<String>();
    public ArrayList<String> arrayComment = new ArrayList<String>();
    public ArrayList<String> arrayGrade = new ArrayList<String>();

    private static LayoutInflater inflater=null;

    public commentAdapter(moreContent moreContent, ArrayList<String> _arrayLogin, ArrayList<String> _arrayComment, ArrayList<String> _arrayGrade) {
        context=moreContent;
        arrayComment=_arrayComment;
        arrayLogin = _arrayLogin;
        arrayGrade = _arrayGrade;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return arrayLogin.size();
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
        TextView login;
        TextView comment;
        ImageView picture;
        RatingBar ratingBar;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.single_row_comment, null);

        holder.login = (TextView) rowView.findViewById(R.id.commentLogin);
        holder.comment = (TextView) rowView.findViewById(R.id.commentComment);
        holder.picture = (ImageView) rowView.findViewById(R.id.commentPicture);
        holder.ratingBar = (RatingBar) rowView.findViewById(R.id.commentRating);

        holder.login.setText(arrayLogin.get(position));
        holder.comment.setText(arrayComment.get(position));
        Picasso.with(context).load("https://intra.etna-alternance.net/report/trombi/image/login/"+arrayLogin.get(position)).into((ImageView) rowView.findViewById(R.id.commentPicture));
        holder.ratingBar.setProgress((int) Float.parseFloat(arrayGrade.get(position))/4);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked :"+arrayLogin.get(position), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}