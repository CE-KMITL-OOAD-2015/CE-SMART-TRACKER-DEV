package com.example.monoblos.myapplication00;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SearchFromFacActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ArrayList<String> subjectList = new ArrayList<String>();
    private ArrayList<String> jsonList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ListView listSub;
    private Spinner spFac = null;
    private HashMap<String,String> mapFac = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_from_fac);

        setImage();
        createList();
        setNegative();
        Spinner();
        setMapFac();
        setBtnKey();
    }

    private void setBtnKey(){
        Button btn = (Button)findViewById(R.id.btnSearchFromFac);
        final Spinner spinner = (Spinner)findViewById(R.id.spinnerSearchFromFac);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItemPosition() > 0) {
                    HttpPostConnection httpPostConnection = new HttpPostConnection();
                    String strJson = null;
                    String courseId = null;
                    String subName = null;
                    String pass = "http://203.151.92.187:8080/findCourseByFaculty";
                    try {
                        String fac = mapFac.get(spinner.getSelectedItem().toString());
                        String param = "faculty=" + URLEncoder.encode(fac);
                        httpPostConnection.execute(pass, param);
                        strJson = httpPostConnection.get();
                        if (strJson == null || strJson.length() == 0) {
                            Toast.makeText(getApplicationContext(), "not found id", Toast.LENGTH_SHORT).show();
                        } else if (strJson.equals("[]")) {
                            Toast.makeText(getApplicationContext(), "No student enrolled this courses ", Toast.LENGTH_SHORT).show();
                        } else {
                            JSONArray parentArray = new JSONArray(strJson);
                            subjectList.clear();
                            jsonList.clear();
                            for (int i = 0; i < parentArray.length(); i++) {
                                JSONObject finalObject = parentArray.getJSONObject(i);
                                courseId = finalObject.getString("courseId");
                                subName = finalObject.getString("courseName");
                                subjectList.add(courseId + " : " + subName);
                                jsonList.add(finalObject.toString());
                            }
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                    listSub.invalidateViews();
                    listSub.refreshDrawableState();
                }
            }
        });
    }

    private void createList(){
        adapter = new ArrayAdapter<String>( this,
                R.layout.list_layout, subjectList);

        listSub = (ListView)findViewById(R.id.listSearchFromFac);
        listSub.setAdapter(adapter);
        listSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goSubActivity(position);
            }
        });
    }

    private void setMapFac(){
        mapFac = new HashMap<String,String>();
        mapFac.put("วิศวกรรมศาสตร์", "ENGINEERING");
        mapFac.put("สถาปัตยกรรมศาสตร์", "ARCHITECTURE");
        mapFac.put("ครุศาสตร์อุตสาหกรรม", "INDUSTRIAL_EDUCATION");
        mapFac.put("เทคโนโลยีการเกษตร", "AGRICULTURAL_TECHNOLOGY");
        mapFac.put("วิทยาศาสตร์", "SCIENCE");
        mapFac.put("อุตสาหกรรมเกษตร", "AGRO_INDUSTRY");
        mapFac.put("เทคโนโลยีสารสนเทศ", "INFORMATION_TECHNOLOGY");
        mapFac.put("วิทยาลัยนานาชาติ", "INTERNATIONAL_COLLEGE");
        mapFac.put("วิทยาลัยนาโนเทคโนโลยีพระจอมเกล้าลาดกระบัง", "NANOTECHNOLOGY_COLLEGE");
        mapFac.put("วิทยาลัยนวัตกรรมการจัดการข้อมูล", "DATA_STORAGE_INNOVATION_COLLEGE");
        mapFac.put("วิทยาลัยการบริหารและจัดการ", "ADMINISTRATION_AND_MANAGEMENT_COLLEGE");
    }

    private void Spinner(){
        spFac = (Spinner)findViewById(R.id.spinnerSearchFromFac);
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


    private void setImage(){
        ImageView imageview = (ImageView)findViewById(R.id.imageView);
        imageview.setImageResource(R.drawable.ic_search_black_24dp);
    }

    private void setNegative(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void goSubActivity(int numberList){
        String str = subjectList.get(numberList);
        String jsonStr = jsonList.get(numberList);
        Intent intent = new Intent(this, SubjectActivity.class);
        intent.putExtra("subject", str);
        intent.putExtra("json",jsonStr);
        startActivity(intent);
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
        actionBar.setTitle("CE_Smart");
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
            ((SearchActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
