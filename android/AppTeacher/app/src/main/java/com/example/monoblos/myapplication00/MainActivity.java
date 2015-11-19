package com.example.monoblos.myapplication00;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

// vibrate from : http://www.myandroidsolutions.com/2012/06/08/android-vibrate-on-click/


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ArrayList<String> listAnnouncement = new ArrayList<String>();
    private ArrayList<String> listAnnouncementSub = new ArrayList<String>();
    private ArrayList<String> jsonList = new ArrayList<String>();
    private ArrayList<Date> dates = new ArrayList<Date>();
    private ArrayList<String> arrayList = null;
    private Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        if(SingletonID.getInstance().getFirstName()==null) {
            startActivity(new Intent(MainActivity.this,MainActivity.class));
            finish();
        }

        createList();
        setImage();
        setNegative();
        setTextName();
    }

    private void setTextName(){
        TextView textName = (TextView)findViewById(R.id.textView);
        TextView textId = (TextView)findViewById(R.id.textViewID);
        String name = SingletonID.getInstance().getFirstName() + " " + SingletonID.getInstance().getLastName();
        String id = SingletonID.getInstance().getStdId();
        textName.setText(name);
        textId.setText(id);
    }

    private void setImage(){
        ImageView imageview = (ImageView)findViewById(R.id.imageView);
        imageview.setImageResource(R.drawable.ce_smart);
    }

    private void setNegative(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void addEnrolledCourses(){
        HttpPostConnection httpPostConnection = new HttpPostConnection();
        String pass = null;
        if(SingletonID.getInstance().getStatus()==0) pass = "http://203.151.92.187:8080/viewEnrolledCourses";
        else pass = "http://203.151.92.187:8080/viewTeachingCourses";
        String inParam = "sessionId=" + URLEncoder.encode(SingletonID.getInstance().getSessionId());
        String strJson = null;
        String comment = null;
        try {
            httpPostConnection.execute(pass, inParam);
            strJson = httpPostConnection.get();
            JSONObject jsonObject = new JSONObject(strJson);
            String result = jsonObject.getString("status");
            JSONArray jsonArray = null;

            if(SingletonID.getInstance().getStatus()==0)jsonArray = jsonObject.getJSONArray("enrolledCourses");
            else jsonArray = jsonObject.getJSONArray("teachingCourses");
            if (result.equals("success")) {
                for(int i=0 ; i<jsonArray.length() ; i++){
                    JSONObject jsonComment = jsonArray.getJSONObject(i);
                    comment = jsonComment.getString("courseId");
                    String sub = jsonComment.getString("courseName");
                    listAnnouncement.add(comment);
                    listAnnouncementSub.add(sub);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void createList(){
        addEnrolledCourses();
        arrayList = new ArrayList<String>();
        String pass = "http://203.151.92.187:8080/viewAllAssignmentDescriptions";
        String inParam = null;
        String strJson = null;
        String noti = "";
        try {
            for(int i=0 ; i<listAnnouncement.size() ; i++){
                String post = listAnnouncement.get(i);
                String param = "courseId="+URLEncoder.encode(post);

                HttpPostConnection httpPostConnection = new HttpPostConnection();
                httpPostConnection.execute(pass, param);
                strJson = httpPostConnection.get();
                JSONObject jsonObject = new JSONObject(strJson);
                String result = jsonObject.getString("status");
                JSONArray jsonArray = jsonObject.getJSONArray("assignmentDescriptions");
                for(int j=0 ; j<jsonArray.length() ; j++){
                    JSONObject object = jsonArray.getJSONObject(j);
                    String jsonO = object.toString();
                    if (result.equals("success")) {
                        Date d = new Date();
                        String strD = null;
                        String dy = String.valueOf(d.getYear()+1900);
                        String dM = null;
                        if(String.valueOf(d.getMonth()).length()==1) dM = "0"+String.valueOf(d.getMonth());
                        else dM = String.valueOf(d.getMonth());
                        String dD = null;
                        if(String.valueOf(d.getDate()).length()==1) dD = "0"+String.valueOf(d.getDate());
                        else dD = String.valueOf(d.getDate());
                        String dH = null;
                        if(String.valueOf(d.getHours()).length()==1) dH = "0"+String.valueOf(d.getHours());
                        else dH = String.valueOf(d.getHours());
                        String dm = null;
                        if(String.valueOf(d.getMinutes()).length()==1) dm = "0"+String.valueOf(d.getMinutes());
                        else dm = String.valueOf(d.getMinutes());
                        String ds = "00";
                        strD = dD + "-" + dM + "-" + dy + " " + dH + ":" + dm + ":" + ds;

                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(object.getString("dueDate"));
                        String strDate = object.getString("dueDate");
                        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
                        DateTime dateTime = dateTimeFormatter.parseDateTime(strDate);
                        DateTime curTime = dateTimeFormatter.parseDateTime(strD);
                        String title = object.getString("title");
                        String str = dateTimeFormatter.print(dateTime) + "\n      "
                                + title + "\n      "
                                + listAnnouncementSub.get(i);
                        if(curTime.isBefore(dateTime)) {
                            dates.add(date);
                            arrayList.add(str);
                            jsonList.add(jsonO);

                            DateTime tempTime = curTime.plusDays(1);
                            if(curTime.compareTo(dateTime)==1){
                                if(noti.equals("")) noti += "วันนี้ต้องส่ง " + title;
                                else noti += "\nวันนี้ต้องส่ง " + title;
                            }
                            if(tempTime.compareTo(dateTime)==1){
                                if(noti.equals("")) noti += "พรุ่งนี้ต้องส่ง " + title;
                                else noti += "\nพรุ่งนี้ต้องส่ง " + title;
                            }
                        }
                    }
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        }
        if(!noti.equals("")){
            Toast.makeText(getApplicationContext(),noti,Toast.LENGTH_LONG).show();
            for(int i=0 ; i<100 ; i++) vibe.vibrate(400);
        }

        sortList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,
                R.layout.list_layout, arrayList);

        final ListView listview = (ListView)findViewById(R.id.listView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String jsonStr = jsonList.get(position);
                Intent intent = new Intent(MainActivity.this, AssignmentActivity.class);
                intent.putExtra("json",jsonStr);
                startActivity(intent);
            }
        });
    }

    private void sortList(){
        //dates jsonList arrayList
        for(int i=0 ; i<arrayList.size() ; i++){
            for(int j=i+1 ; j<arrayList.size() ; j++){
                if(dates.get(i).after(dates.get(j))){
                    Date temp = (Date)dates.get(i).clone();
                    dates.set(i,(Date)dates.get(j).clone());
                    dates.set(j,temp);
                    String strtemp = new String(arrayList.get(i));
                    arrayList.set(i,arrayList.get(j));
                    arrayList.set(j,strtemp);

                    String strJson = new String(jsonList.get(i));
                    jsonList.set(i,jsonList.get(j));
                    jsonList.set(j,strJson);
                }
            }
        }
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
                    startActivity(new Intent(MainActivity.this, MyCourseTeacherActivity.class));
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
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
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
