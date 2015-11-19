package com.example.monoblos.myapplication00;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SubjectActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String json = null;
    private String courseId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        setImage();
        setNegative();
        setBundle();
        setClickBtn();
        seDis();
        setDropBtn();
        setBtnRateAndComment();
        setSpinnerComment();
    }

    private void setSpinnerComment(){
        Spinner spinnerComment = (Spinner)findViewById(R.id.spinnerComment);
        ArrayList<String> arrayListComment = new ArrayList<String>();
        arrayListComment.add("ดูความคิดเห็น");

        HttpPostConnection httpPostConnection = new HttpPostConnection();
        String pass = "http://203.151.92.187:8080/viewComment";
        String inParam = "courseId=" + URLEncoder.encode(courseId);
        String strJson = null;
        String comment = null;
        try {
            httpPostConnection.execute(pass, inParam);
            strJson = httpPostConnection.get();
            JSONObject jsonObject = new JSONObject(strJson);
            String result = jsonObject.getString("status");
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            if (result.equals("success")) {
                for(int i=0 ; i<jsonArray.length() ; i++){
                    JSONObject jsonComment = jsonArray.getJSONObject(i);
                    comment = jsonComment.getString("message");
                    arrayListComment.add(comment);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,arrayListComment);

        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinnerComment.setAdapter(adapter);
    }

    private void setBtnRateAndComment(){
        Button btn = (Button)findViewById(R.id.btn_com_rate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonStr = json;
                Intent intent = new Intent(SubjectActivity.this, Comment_Rate_Activity.class);
                intent.putExtra("json", jsonStr);
                startActivity(intent);
            }
        });
    }

    private void seDis(){
        TextView textView = (TextView)findViewById(R.id.text_dis);
        String description = null;
        String str = "";
        String strName = null;
        String strDay = null;
        String strTime = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            courseId = jsonObject.getString("courseId");
            description = jsonObject.getString("description");
            strName = jsonObject.getString("courseName");
            strDay = jsonObject.getString("courseDay");
            strTime = jsonObject.getString("courseTime");
            str = " รหัสวิชา : " + courseId + "\n\n" + " ชื่อวิชา : " +strName + "\n\n"
                    + " คำอธิบาย : " + description + "\n\n" + " วันที่เรียน : " + strDay + "\n\n"
                    + " เวลาเรียน : " + strTime + "\n\n"
                    + "คะแนนเฉลี่ย : " + getAverage();
        }catch(JSONException e){e.printStackTrace();}
        textView.setText(str);
    }

    private String getAverage() {
        HttpPostConnection httpPostConnection = new HttpPostConnection();
        String pass = "http://203.151.92.187:8080/viewRating";
        String inParam = "courseId=" + URLEncoder.encode(courseId);
        String strJson = null;
        String avg = null;
        try {
            httpPostConnection.execute(pass, inParam);
            strJson = httpPostConnection.get();
            JSONObject jsonObject = new JSONObject(strJson);
            String result = jsonObject.getString("status");
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            if (result.equals("success")) {
                avg = jsonObject.getString("averageRating");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return avg;
    }

    private void setBundle(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String name = bundle.getString("subject");
            json = bundle.getString("json");
            TextView tv = (TextView)findViewById(R.id.textViewSub);
        }
    }

    private void setNegative(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void setImage(){
        ImageView imageview = (ImageView)findViewById(R.id.imageView);
        imageview.setImageResource(R.drawable.ic_folder_black_24dp);
    }

    private void setClickBtn(){
        Button btn = (Button)findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpPostConnection httpPostConnection = new HttpPostConnection();
                String pass = "http://203.151.92.187:8080/enrollCourse";
                String inParam = "sessionId=" + URLEncoder.encode(SingletonID.getInstance().getSessionId()) +
                        "&courseId=" + courseId;
                String strJson = null;
                try {
                    httpPostConnection.execute(pass, inParam);
                    strJson = httpPostConnection.get();
                    JSONObject jsonObject = new JSONObject(strJson);
                    String result = jsonObject.getString("status");
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    if (result.equals("success")) {
                        startActivity(new Intent(SubjectActivity.this, MyCourseActivity.class));
                        finish();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDropBtn(){
        Button btn = (Button)findViewById(R.id.btn_drop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpPostConnection httpPostConnection = new HttpPostConnection();
                String pass = "http://203.151.92.187:8080/dropCourse";
                String inParam = "sessionId=" + URLEncoder.encode(SingletonID.getInstance().getSessionId()) +
                        "&courseId=" + courseId;
                String strJson = null;
                try {
                    httpPostConnection.execute(pass, inParam);
                    strJson = httpPostConnection.get();
                    JSONObject jsonObject = new JSONObject(strJson);
                    String result = jsonObject.getString("status");
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    if (result.equals("success")) {
                        startActivity(new Intent(SubjectActivity.this, MyCourseActivity.class));
                        finish();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                if(getSupportFragmentManager().getFragments().size()>1) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                break;
            case 2:
                startActivity(new Intent(this,SearchActivity.class));
                break;
            case 3:
                if(SingletonID.getInstance().getStatus()==0) startActivity(new Intent(this,AnnStdActivity.class));
                else startActivity(new Intent(this,SearchStudentActivity.class));
                break;
            case 4:
                if(SingletonID.getInstance().getStatus()==0){
                    startActivity(new Intent(this, MyCourseActivity.class));
                }
                else startActivity(new Intent(this, CreateSubjectActivity.class));
                break;
            case 5:
                if(SingletonID.getInstance().getStatus()==0){
                    SingletonID.getInstance().clear();
                    FacebookSdk.clearLoggingBehaviors();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else {//my course teacher
                    startActivity(new Intent(this, MyCourseTeacherActivity.class));
                }
                break;
            case 6:
                if(SingletonID.getInstance().getStatus()==1){
                    SingletonID.getInstance().clear();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("CE SMART TRACKER");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003366")));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((SubjectActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
