package com.example.monoblos.myapplication00;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyCourseTeacherActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ArrayList<String> annL = null;
    private ArrayList<String> subId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course_teacher);

        createList();
        setImage();
        setNegative();
    }

    private void setImage(){
        ImageView imageview = (ImageView)findViewById(R.id.imageView);
        imageview.setImageResource(R.drawable.ic_folder_black_24dp);
    }

    private void setNegative(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void createList(){
        annL = JSONSubject();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,
                R.layout.list_layout, annL);

        final ListView listview = (ListView)findViewById(R.id.listView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popup(position);
            }
        });
    }

    private void popup(final int position){
        final String [] i=new String []{"Add Assignment","Add Announcement","Update score","ดูสถิติ","Report"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("ตัวเลือก");

        builder.setItems(i, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) goSubActivity(position, AddAssActivity.class);
                else if (which == 1) goSubActivity(position, AddAnnActivity.class);
                else if (which == 2) goSubActivity(position, ViewAssTeacherActivity.class);
                else if (which == 3) popupStatistic(subId.get(position));
                else if (which == 4) goSubActivity(position,ProgressActivity.class);
            }
        });
        builder.show();
    }

    private void popupStatistic(String courseId){
        final Dialog d = new Dialog(MyCourseTeacherActivity.this);
        d.setTitle("Statistic");
        d.setContentView(R.layout.layout_popup_statistic);
        TextView textView = (TextView) d.findViewById(R.id.pop_statistic);
        textView.setText(getStatistic(courseId));
        d.show();
    }

    private String getStatistic(String courseId){
        HttpPostConnection httpPostConnection = new HttpPostConnection();
        String url = "http://203.151.92.187:8080/viewReport";
        String param = "sessionId=" + URLEncoder.encode(SingletonID.getInstance().getSessionId()) +
                "&courseId=" + URLEncoder.encode(courseId);
        String str = "";
        try {
            httpPostConnection.execute(url,param);
            String strJson = httpPostConnection.get();
            JSONObject jsonObject = new JSONObject(strJson);
            String result = jsonObject.getString("status");
            if(result.equals("success")){
                JSONObject jsonObject1 = jsonObject.getJSONObject("statistic");
                str = "possibleMax : " + jsonObject1.getString("possibleMax") +
                        "\nmean : " + jsonObject1.getString("mean") +
                        "\nsd : " + jsonObject1.getString("sd") +
                        "\nmax : " + jsonObject1.getString("max") +
                        "\nmin : " + jsonObject1.getString("min");
            }
        }catch(ExecutionException e){e.printStackTrace();
        }catch(InterruptedException e){e.printStackTrace();
        }catch (JSONException e){ e.printStackTrace();}
        return str;
    }

    private void goSubActivity(int position,Class c){
        String id = subId.get(position);
        Intent intent = new Intent(this,c);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private ArrayList<String> JSONSubject(){
        ArrayList<String> list = new ArrayList<String>();
        subId = new ArrayList<String>();
        HttpPostConnection httpPostConnection = new HttpPostConnection();
        String url = "http://203.151.92.187:8080/viewTeachingCourses";
        String param = "sessionId=" + URLEncoder.encode(SingletonID.getInstance().getSessionId());
        try {
            httpPostConnection.execute(url,param);
            String strJson = httpPostConnection.get();
            JSONObject jsonObject = new JSONObject(strJson);
            String result = jsonObject.getString("status");
            if(result.equals("success")){
                JSONArray jsonArray = jsonObject.getJSONArray("teachingCourses");
                for(int i=0 ; i<jsonArray.length() ; i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String course = object.getString("courseName");
                    String courseId = object.getString("courseId");
                    String subject = courseId + " " + course;
                    list.add(subject);
                    subId.add(courseId);
                }
            }
        }catch(ExecutionException e){e.printStackTrace();
        }catch(InterruptedException e){e.printStackTrace();
        }catch (JSONException e){ e.printStackTrace();}
        return list;
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
            ((MyCourseTeacherActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
