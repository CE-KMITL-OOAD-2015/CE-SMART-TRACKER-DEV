package com.example.monoblos.myapplication00;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class CreateSubjectActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Spinner spDay = null;
    private Spinner spTime = null;
    private Spinner spFac = null;
    private HashMap<String,String> mapFac = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);

        setNegative();
        setMapFac();
        setImage();
        setBtn();
        Spinner();
    }

    private void setMapFac(){
        mapFac = new HashMap<String,String>();
        mapFac.put("วิศวกรรมศาสตร์","ENGINEERING");
        mapFac.put("สถาปัตยกรรมศาสตร์","ARCHITECTURE");
        mapFac.put("ครุศาสตร์อุตสาหกรรม","INDUSTRIAL_EDUCATION");
        mapFac.put("เทคโนโลยีการเกษตร","AGRICULTURAL_TECHNOLOGY");
        mapFac.put("วิทยาศาสตร์","SCIENCE");
        mapFac.put("อุตสาหกรรมเกษตร","AGRO_INDUSTRY");
        mapFac.put("เทคโนโลยีสารสนเทศ","INFORMATION_TECHNOLOGY");
        mapFac.put("วิทยาลัยนานาชาติ","INTERNATIONAL_COLLEGE");
        mapFac.put("วิทยาลัยนาโนเทคโนโลยีพระจอมเกล้าลาดกระบัง","NANOTECHNOLOGY_COLLEGE");
        mapFac.put("วิทยาลัยนวัตกรรมการจัดการข้อมูล", "DATA_STORAGE_INNOVATION_COLLEGE");
        mapFac.put("วิทยาลัยการบริหารและจัดการ", "ADMINISTRATION_AND_MANAGEMENT_COLLEGE");
    }

    private void sendReq(String req){
        HttpPostConnection httpPostConnection = new HttpPostConnection();
        String s = null;
        String result = "Fail";
        String pass = "http://203.151.92.187:8080/createCourse";

        try {
            httpPostConnection.execute(pass,req);
            s = httpPostConnection.get();
            JSONObject parentObject = new JSONObject(s);
            result = parentObject.getString("status");
        }catch(ExecutionException e){e.printStackTrace();
        }catch(CancellationException e){e.printStackTrace();
        }catch(InterruptedException e){e.printStackTrace();
        }catch(JSONException e){ e.printStackTrace();}
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setBtn(){
        Button btn = (Button)findViewById(R.id.btnCreate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                EditText editId = (EditText) findViewById(R.id.editID);
                EditText editSub = (EditText) findViewById(R.id.editSub);
                if(editId.getText().length()!=8){
                    Toast.makeText(getApplicationContext(),"รหัสวิชา 8 หลัก",Toast.LENGTH_SHORT).show();
                    check = false;
                }
                if(spDay.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"กรุณาเลือกคณะ",Toast.LENGTH_SHORT).show();
                    check = false;
                }
                if(spDay.getSelectedItemPosition()==0) {
                    Toast.makeText(getApplicationContext(),"กรุณาเลือกคณะ",Toast.LENGTH_SHORT).show();
                    check = false;
                }
                EditText editDes = (EditText) findViewById(R.id.editDes);
                EditText editDep = (EditText) findViewById(R.id.editDep);
                if(spTime.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"กรุณาเลือกเวลาที่สอน",Toast.LENGTH_SHORT).show();
                    check = false;
                }
                if(editId.getText().toString().contains(" ")){
                    Toast.makeText(getApplicationContext(),"ID ต้องใช้เฉพาะตัวเลข",Toast.LENGTH_SHORT).show();
                    check = false;

                }

                if(check) {
                    String inParam = "sessionId=" + URLEncoder.encode(SingletonID.getInstance().getSessionId()) +
                            "&courseId=" + URLEncoder.encode(editId.getText().toString()) +
                            "&courseName=" + URLEncoder.encode(editSub.getText().toString()) +
                            "&faculty=" + URLEncoder.encode(mapFac.get(spFac.getSelectedItem().toString())) +
                            "&courseDay=" + URLEncoder.encode(spDay.getSelectedItem().toString().toUpperCase()) +
                            "&description=" + URLEncoder.encode(editDes.getText().toString()) +
                            "&department=" + URLEncoder.encode(editDep.getText().toString()) +
                            "&courseTime=" + URLEncoder.encode(spTime.getSelectedItem().toString());

                    sendReq(inParam);
                }
            }
        });
    }

    private void Spinner(){
        spDay = (Spinner)findViewById(R.id.spinnerDay);
        ArrayList<String> Daylist = new ArrayList<String>();
        Daylist.add("วันที่ทำการเรียนการสอน");
        Daylist.add("Monday");
        Daylist.add("Tuesday");
        Daylist.add("Wednesday");
        Daylist.add("Friday");
        Daylist.add("Saturday");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,Daylist);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spDay.setAdapter(dataAdapter);

        spTime = (Spinner)findViewById(R.id.spinnerTime);
        ArrayList<String> timeList = new ArrayList<String>();
        timeList.add("เวลาที่ทำการเรียนการสอน");
        timeList.add("9:00 - 12:00");
        timeList.add("13:00 - 16:00");
        timeList.add("16:30 - 19:30");
        ArrayAdapter<String> TimeAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,timeList);

        TimeAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spTime.setAdapter(TimeAdapter);

        spFac = (Spinner)findViewById(R.id.spinFac);
        ArrayList<String> facList = new ArrayList<String>();
        facList.add("คณะ");
        facList.add("วิศวกรรมศาสตร์");
        facList.add("สถาปัตยกรรมศาสตร์");
        facList.add("ครุศาสตร์อุตสาหกรรม");
        facList.add("เทคโนโลยีการเกษตร");
        facList.add("วิทยาศาสตร์");
        facList.add("อุตสาหกรรมเกษตร");
        facList.add("เทคโนโลยีสารสนเทศ");
        facList.add("วิทยาลัยนานาชาติ");
        facList.add("วิทยาลัยนาโนเทคโนโลยีพระจอมเกล้าลาดกระบัง");
        facList.add("วิทยาลัยนวัตกรรมการจัดการข้อมูล");
        facList.add("วิทยาลัยการบริหารและจัดการ");
        ArrayAdapter<String> facAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,facList);

        facAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spFac.setAdapter(facAdapter);
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
        imageview.setImageResource(R.drawable.ic_create_black_24dp);
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
            ((CreateSubjectActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
