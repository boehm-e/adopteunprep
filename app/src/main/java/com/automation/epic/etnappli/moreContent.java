package com.automation.epic.etnappli;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class moreContent extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private final OkHttpClient client = new OkHttpClient();

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private ImageView coverImage;
    private FrameLayout framelayoutTitle;
    private LinearLayout linearlayoutTitle;
    private Toolbar toolbar;
    private SimpleDraweeView avatar;
    private TextView bigTitle;
    private TextView appbarTitle;
    private TextView tvMoreDescription;
    private ProgressBar globalGrade;
    private Button showAllGrades;
    private Button addComment;
    private ListView commentListView;
    private LinearLayout bigContainer;

    public static int [] prgmImages={R.drawable.background, R.drawable.material_background};
    public static String [] prgmNameList={"Let Us C","c++","JAVA","Jsp"};
    public ArrayList<String> arrayLogin = new ArrayList<String>();
    public ArrayList<String> arrayComment = new ArrayList<String>();
    public ArrayList<String> arrayGrade = new ArrayList<String>();

    public String login = "";
    public String myLogin = "";

    private void findViews() {
        appbar = (AppBarLayout)findViewById( R.id.appbar );
        collapsing = (CollapsingToolbarLayout)findViewById( R.id.collapsing );
        coverImage = (ImageView)findViewById( R.id.imageview_placeholder );
        framelayoutTitle = (FrameLayout)findViewById( R.id.framelayout_title );
        linearlayoutTitle = (LinearLayout)findViewById( R.id.linearlayout_title );
        toolbar = (Toolbar)findViewById( R.id.toolbar );
        avatar = (SimpleDraweeView)findViewById(R.id.avatar);
        bigTitle = (TextView) findViewById(R.id.bigTitle);
        appbarTitle = (TextView) findViewById(R.id.appbarTitle);
        globalGrade = (ProgressBar) findViewById(R.id.globalGrade);
        showAllGrades = (Button) findViewById(R.id.showAllGrades);
        commentListView = (ListView) findViewById(R.id.commentListView);
        bigContainer = (LinearLayout) findViewById(R.id.bigContainer);
        tvMoreDescription = (TextView) findViewById(R.id.tvMoreDescription);
        addComment = (Button) findViewById(R.id.addComment);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_more);
        findViews();


        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);
        startAlphaAnimation(appbarTitle, 0, View.INVISIBLE);

        //set avatar and cover
        coverImage.setImageResource(R.drawable.material_background);


        Intent intent = getIntent();
        login = intent.getStringExtra("login");
        myLogin = intent.getStringExtra("myLogin");


        appbarTitle.setText(login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            commentListView.setNestedScrollingEnabled(true);
        }


        try {
            getData(login);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //when all done
        bigContainer.setVisibility(View.VISIBLE);
    }


    public void getData(final String username) throws Exception {
        Log.e("GETDATA", "USERNAME"+username);
        Log.e("GETDATA", "USERNAME2"+login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url = "http://ccmobile-etna.cloudapp.net/getUserByUsername";
        RequestBody formBody = new FormEncodingBuilder()
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String jsonData = response.body().string();
        JSONObject Jobject = new JSONObject(jsonData);
        // GET JSON OBJECT (Jobject);
        // NOW FILL VIEW;

        final JSONObject userDetail = Jobject.getJSONObject("userDetail");
        JSONObject userCard = Jobject.getJSONObject("userCard");

        int generalGrade = (int) (Float.parseFloat(userCard.getJSONObject("generalGrade").getString("grade")) * 5); // 20 ==> 100


        String description = userCard.getString("description");
        String picture = userCard.getString("picture");
        String firstname = userCard.getString("firstName");
        String lastname = userCard.getString("lastName");
        JSONArray gradeArray = new JSONArray(userCard.getString("skills"));

        if (description.isEmpty())
            tvMoreDescription.setText("Aucune information trouvee pour cet utilisateur");
        else
            tvMoreDescription.setText(description);
        avatar.setImageURI(Uri.parse(picture));
        bigTitle.setText(firstname + " " + lastname);
        globalGrade.setProgress(generalGrade);

        Log.e("userCard", userCard.toString());
        Log.e("userDetail", userDetail.toString());

        JSONArray commments = userDetail.getJSONArray("studentsComments");

        arrayComment.clear();
        arrayLogin.clear();
        arrayGrade.clear();

        for (int i=0; i<commments.length(); i++) {
            JSONObject obj = commments.getJSONObject(i);
            arrayLogin.add(obj.getString("username"));
            arrayComment.add(obj.getString("comment"));
            arrayGrade.add(obj.getString("grade"));
            // date

            Log.e("COMMENT", obj.toString());
        }


        commentListView.setAdapter(new commentAdapter(this, arrayLogin, arrayComment, arrayGrade));


        fillGrades(gradeArray);

        showAllGrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showGrades = new Intent(getApplicationContext(), gradeActivity.class);
                Bundle b = new Bundle();
                try {
                    b.putString("Array", userDetail.get("etnaGrades").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showGrades.putExtras(b);
                startActivity(showGrades);
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ADD COMMENT LOGIN PARAM
                Intent addComment = new Intent(moreContent.this, setCommentActivity.class);
                addComment.putExtra("username", username);
                addComment.putExtra("selfUsername", myLogin);
                startActivity(addComment);
            }
        });
    }

    public int getPx(int dimensionDp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

    private void fillGrades(JSONArray grades) {

        LinearLayout arcContainer = (LinearLayout) findViewById(R.id.arcContainer);
        CardView containerGrades = (CardView) findViewById(R.id.containerGrades);

        for (int i=0; i < grades.length(); i++) {
            try {
                ArcProgress arc = new ArcProgress(moreContent.this);

                JSONObject obj = new JSONObject(grades.get(i).toString());
                Log.e("OBJECT", grades.toString());
                Log.e("GRADE GRADE", obj.getString("grade").toString());
                Log.e("GRADE NAME", obj.getString("name").toString());



                arc.setProgress((int) Float.parseFloat(obj.getString("grade")));
                arc.setBottomText(obj.getString("name").toString());
                arc.setFinishedStrokeColor(Color.parseColor("#6C7EB7"));
                arc.setUnfinishedStrokeColor(Color.parseColor("#ffffff"));

                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(getPx(100), getPx(100));
                params.setMargins(getPx(4), getPx(4), getPx(4), getPx(4));
                arc.setLayoutParams(params);

                arcContainer.addView(arc);
            } catch (JSONException e) {
//                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("RESTART", "RESTARTTTTTTTTTTTTTTTT");
        try {
            arrayComment.clear();
            arrayLogin.clear();
            arrayGrade.clear();
            getData(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("MENU", item.getTitle().toString());
        switch (item.getTitle().toString()) {
            case "linkedin":
                Log.e("LINKEDIN", "LINKEDIN");
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse());
//                startActivity(i);
                return true;
            case "github":
                Log.e("GITHUB", "GITHUB");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.e("FINISH", "FINISH");
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(appbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(appbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
