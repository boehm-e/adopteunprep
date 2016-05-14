package com.automation.epic.etnappli;

/**
 * Created by root on 04/05/16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class chooseProjectAdapter extends BaseAdapter{
    ArrayList<String> chooseProjectCategory;
    ArrayList<String> chooseProjectTitle;
    ArrayList<String> chooseProjectDateBegin;
    ArrayList<String> chooseProjectDateEnd;
    String username = null;

    Context context;
    private static LayoutInflater inflater=null;

    public chooseProjectAdapter(navigationDrawer navigationDrawer, ArrayList<String> _chooseProjectCategory, ArrayList<String> _chooseProjectTitle, ArrayList<String> _chooseProjectDateBegin, ArrayList<String> _chooseProjectDateEnd) {
        chooseProjectCategory = _chooseProjectCategory;
        chooseProjectTitle = _chooseProjectTitle;
        chooseProjectDateBegin = _chooseProjectDateBegin;
        chooseProjectDateEnd = _chooseProjectDateEnd;

        Log.e("TEST" , _chooseProjectCategory.get(0));
        Log.e("TEST" , _chooseProjectTitle.get(0));
        Log.e("TEST" , _chooseProjectDateBegin.get(0));
        Log.e("TEST" , _chooseProjectDateEnd.get(0));

        context=navigationDrawer;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Intent intent = ((Activity) context).getIntent();
        username = intent.getStringExtra("login");
    }

    @Override
    public int getCount() {
        return chooseProjectTitle.size();
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
        TextView tvCategory;
        TextView tvTitle;
        TextView tvDateBegin;
        TextView tvDateEnd;
    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.single_row_project, null);

        holder.tvCategory = (TextView) rowView.findViewById(R.id.projectCategory);
        holder.tvTitle = (TextView) rowView.findViewById(R.id.projectTitle);
        holder.tvDateBegin = (TextView) rowView.findViewById(R.id.dateBegin);
        holder.tvDateEnd= (TextView) rowView.findViewById(R.id.dateEnd);


        holder.tvCategory.setText(chooseProjectCategory.get(position));
        holder.tvTitle.setText(chooseProjectTitle.get(position));
        holder.tvDateBegin.setText(chooseProjectDateBegin.get(position));
        holder.tvDateEnd.setText(chooseProjectDateEnd.get(position));

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked "+chooseProjectTitle.get(position), Toast.LENGTH_SHORT).show();


                Intent showCards = new Intent(context, MainActivity.class);
                showCards.putExtra("login", username);
                showCards.putExtra("ueName", chooseProjectCategory.get(position));
                Log.e("CHOOSEPROJECTADAPTER", "CHOOSEPROJECT : "+username);
                context.startActivity(showCards);


            }
        });
        return rowView;
    }

}