package com.automation.epic.etnappli;

/**
 * Created by root on 04/05/16.
 */
import android.content.Context;
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

public class gradeAdapter extends BaseAdapter{
    ArrayList<String> comment;
    ArrayList<String> grade;
    ArrayList<String> name;

    Context context;
    private static LayoutInflater inflater=null;

    public gradeAdapter(gradeActivity gradeContent, ArrayList<String> _comment, ArrayList<String> _grade, ArrayList<String> _name) {
        comment = _comment;
        grade = _grade;
        name = _name;
        context=gradeContent;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return comment.size();
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
        TextView gradeUeName;
        TextView gradeUeGrade;
        TextView gradeUeComment;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.single_row_grade, null);

        holder.gradeUeName = (TextView) rowView.findViewById(R.id.gradeUeName);
        holder.gradeUeGrade = (TextView) rowView.findViewById(R.id.gradeUeGrade);
        holder.gradeUeComment = (TextView) rowView.findViewById(R.id.gradeUeComment);


        holder.gradeUeName.setText(name.get(position));
        holder.gradeUeGrade.setText(grade.get(position));
        holder.gradeUeComment.setText(comment.get(position));

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked "+name.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        return rowView;
    }

}