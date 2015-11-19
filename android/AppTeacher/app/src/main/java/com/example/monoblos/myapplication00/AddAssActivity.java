package com.example.monoblos.myapplication00;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class AddAssActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ass);

        setNegative();
        setImage();
        setBtn();
        setId();
        showDate();
        showTime();
    }

    private void showDate(){
        final Dialog date = new Dialog(AddAssActivity.this);
        date.setContentView(R.layout.layout_set_date);
        final DatePicker datePicker = (DatePicker)date.findViewById(R.id.dateAddAss);
        final Button btnText = (Button)findViewById(R.id.btnDate);
        Button b = (Button)date.findViewById(R.id.btnDateAddAss);
        date.setTitle("Date");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnText.setText(datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth());
                date.dismiss();
            }
        });
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date.show();
            }
        });
    }

    private void showTime(){
        final Dialog time = new Dialog(AddAssActivity.this);
        time.setContentView(R.layout.layout_set_time);
        final TimePicker timePicker = (TimePicker)time.findViewById(R.id.timeAddAss);
        final Button btnText = (Button)findViewById(R.id.btnTime);
        Button b = (Button)time.findViewById(R.id.btnTimeAddAss);
        time.setTitle("Time");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnText.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
                time.dismiss();
            }
        });
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.show();
            }
        });

    }

    private void setId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.id = bundle.getString("id");
        }
    }

    private void sendReq(String req){
        HttpPostConnection httpPostConnection = new HttpPostConnection();
        String s = null;
        String result = "Fail";
        String pass = "http://203.151.92.187:8080/addAssignment";

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
    }

    private void setBtn(){
        Button btn = (Button)findViewById(R.id.btnCreate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ch = true;
                EditText editTitle = (EditText) findViewById(R.id.addTitle);
                EditText editDes = (EditText) findViewById(R.id.addDes);
                EditText editMax = (EditText) findViewById(R.id.addMax);
                Button textDate = (Button)findViewById(R.id.btnDate);
                Button textTime = (Button)findViewById(R.id.btnTime);

                if(editTitle.getText().toString().equals("")) ch = false;
                if(editDes.getText().toString().equals("")) ch = false;
                if(editMax.getText().toString().equals("")) ch = false;
                if(Integer.getInteger(editMax.getText().toString())>100 || Integer.getInteger(editMax.getText().toString())<=0){
                    ch = false;
                    Toast.makeText(getApplicationContext(),"ใส่ตัวเลขตั้งแต่ 1 - 100",Toast.LENGTH_SHORT).show();
                }
                if(textDate.getText().toString().equals("Click here set Date")) ch = false;
                if(textTime.getText().toString().equals("Click here set Time")) ch = false;

                if(ch) {
                    String inParam = "sessionId=" + URLEncoder.encode(SingletonID.getInstance().getSessionId()) +
                            "&courseId=" + URLEncoder.encode(id) +
                            "&title=" + URLEncoder.encode(editTitle.getText().toString()) +
                            "&description=" + URLEncoder.encode(editDes.getText().toString()) +
                            "&maxScore=" + URLEncoder.encode(editMax.getText().toString()) +
                            "&dueDate=" + URLEncoder.encode(textDate.getText().toString() + " " + textTime.getText().toString());

                    sendReq(inParam);
                    finish();
                }
                else Toast.makeText(getApplicationContext(),"ข้อมูลไม่ครบ",Toast.LENGTH_SHORT).show();
            }
        });
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
        imageview.setImageResource(R.drawable.ic_announcement_black_24dp);
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
            ((AddAssActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
