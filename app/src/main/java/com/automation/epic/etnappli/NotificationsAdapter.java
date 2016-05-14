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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsAdapter extends BaseAdapter{
    public ArrayList<String> notificationUserName;
    public ArrayList<String> notificationDescription;
    public ArrayList<String> notificationUeName;
    public ArrayList<String> notificationUrl;
    public ArrayList<String> notificationLeader;
    public ArrayList<String> notificationMemo;


    public String username = null;

    Context context;
    private static LayoutInflater inflater=null;

//    arrayUrl.add("");
//    arrayLeader.add("");
//    arrayMemo.add("");


    public NotificationsAdapter(activityNotifications activityNotifications, ArrayList<String> _notificationUserName, ArrayList<String> _notificationDescription, ArrayList<String> _notificationUeName, ArrayList<String> _notificationUrl, ArrayList<String> _notificatioinLeader, ArrayList<String> _notificationMemo) {
        notificationDescription = _notificationDescription;
        notificationUserName = _notificationUserName;
        notificationUeName = _notificationUeName;
        notificationUrl = _notificationUrl;
        notificationLeader = _notificatioinLeader;
        notificationMemo = _notificationMemo;

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

    public String getUeName(int position) {
        return notificationUeName.get(position);
    }

    public String getPerpetrator(int position) {
        return notificationLeader.get(position);
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

        TextView accept;
        TextView refuse;
        TextView url;

    }

    public void remove(int pos) {
        Log.e("REMOVE", "REMOVE : "+pos);
        notificationUeName.remove(pos);
        notificationDescription.remove(pos);
        notificationUserName.remove(pos);
        notificationLeader.remove(pos);
        notificationMemo.remove(pos);
        notificationUrl.remove(pos);
        notifyDataSetChanged();
    }



    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder=new Holder();
        final View rowView;
        if (notificationLeader.get(position)== "") {
            rowView = inflater.inflate(R.layout.single_row_notifications, null);
            holder.notifLogin = (TextView) rowView.findViewById(R.id.notifLogin);
            holder.notifUe = (TextView) rowView.findViewById(R.id.notifUe);
            holder.notifDescription = (TextView) rowView.findViewById(R.id.notifDescription);
            holder.notifPicture = (ImageView) rowView.findViewById(R.id.notifPicture);

            holder.notifLogin.setText(notificationUserName.get(position));
            holder.notifUe.setText(notificationUeName.get(position));
            holder.notifDescription.setText(notificationDescription.get(position));
            Picasso.with(context).load("https://intra.etna-alternance.net/report/trombi/image/login/" + notificationUserName.get(position)).into(holder.notifPicture);
        }
        else { // SI DEMANDE DE GROUPE
            rowView = inflater.inflate(R.layout.single_row_notifications_accept_group, null);

            holder.notifLogin = (TextView) rowView.findViewById(R.id.notifLogin);
            holder.notifUe = (TextView) rowView.findViewById(R.id.notifUe);
            holder.notifDescription = (TextView) rowView.findViewById(R.id.notifDescription);
            holder.url= (TextView) rowView.findViewById(R.id.notifUrl);
            holder.notifPicture = (ImageView) rowView.findViewById(R.id.notifPicture);

            Log.e("TEXTTT", ":" + notificationUserName.get(position));

            holder.notifLogin.setText(notificationLeader.get(position));
            holder.notifUe.setText(notificationUeName.get(position));
            holder.url.setText(notificationUrl.get(position));
            holder.notifDescription.setText(notificationDescription.get(position) + "\n" + "'" + notificationMemo.get(position) + "'");
            Picasso.with(context).load("https://intra.etna-alternance.net/report/trombi/image/login/" + notificationLeader.get(position)).into(holder.notifPicture);

            holder.accept = (TextView) rowView.findViewById(R.id.txt_accept);
            holder.refuse = (TextView) rowView.findViewById(R.id.txt_refuse);

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ACCEPT", "ACCEPT : "+notificationUrl.get(position));
                    responseInvitation(notificationUrl.get(position), "1", username);
                }
            });
            holder.refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("REFUSE", "REFUSE : "+notificationUrl.get(position));
                    responseInvitation(notificationUrl.get(position), "0", username);
                }
            });
        }

        return rowView;
    }

    public void responseInvitation(final String url, final String accept, final String _username) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://ccmobile-etna.cloudapp.net/acceptInvitation",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE invitation : ", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RESPONSE error : ", error.toString());
                    }

                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                 params.put("url", url);
                params.put("username", _username);
                params.put("validation", accept);
                return params;
            }

        };
        queue.add(postRequest);
    }

}