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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationsAdapter extends BaseAdapter{
    public ArrayList<String> notificationUserName;
    public ArrayList<String> notificationDescription;
    public ArrayList<String> notificationUeName;
    public String username = null;

    Context context;
    private static LayoutInflater inflater=null;

    public NotificationsAdapter(activityNotifications activityNotifications, ArrayList<String> _notificationUserName, ArrayList<String> _notificationDescription, ArrayList<String> _notificationUeName) {
        notificationDescription = _notificationDescription;
        notificationUserName = _notificationUserName;
        notificationUeName = _notificationUeName;

        context=activityNotifications;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Intent intent = ((Activity) context).getIntent();
        username = intent.getStringExtra("login");
    }

    @Override
    public int getCount() {
        return notificationUserName.size();
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
        TextView notifLogin;
        TextView notifUe;
        TextView notifDescription;
        ImageView notifPicture;
    }

    public void remove(int pos) {
        Log.e("REMOVE", "REMOVE : "+pos);
        notificationUeName.remove(pos);
        notificationDescription.remove(pos);
        notificationUserName.remove(pos);
        notifyDataSetChanged();
    }



    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder=new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.single_row_notifications, null);

        holder.notifLogin = (TextView) rowView.findViewById(R.id.notifLogin);
        holder.notifUe = (TextView) rowView.findViewById(R.id.notifUe);
        holder.notifDescription = (TextView) rowView.findViewById(R.id.notifDescription);
        holder.notifPicture = (ImageView) rowView.findViewById(R.id.notifPicture);

        Log.e("TEXTTT", ":" + notificationUserName.get(position));

        holder.notifLogin.setText(notificationUserName.get(position));
        holder.notifUe.setText(notificationUeName.get(position));
        holder.notifDescription.setText(notificationDescription.get(position));
        Picasso.with(context).load("https://intra.etna-alternance.net/report/trombi/image/login/" + notificationUserName.get(position)).into(holder.notifPicture);

        return rowView;
    }

}