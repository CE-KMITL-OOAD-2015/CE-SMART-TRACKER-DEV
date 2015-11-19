package com.example.monoblos.myapplication00;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class RegisterActivity extends AppCompatActivity {

    private Spinner spinner = null;
    private String fbId = null;
    private HashMap<String,String> map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("CE SMART TRACKER");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003366")));

        setSpinner();
        setMap();
        setBundle();
        setBtn();
    }

    private void setMap(){
        map = new HashMap<String,String>();
        map.put("วิศวกรรมศาสตร์","ENGINEERING");
        map.put("สถาปัตยกรรมศาสตร์","ARCHITECTURE");
        map.put("ครุศาสตร์อุตสาหกรรม","INDUSTRIAL_EDUCATION");
        map.put("เทคโนโลยีการเกษตร","AGRICULTURAL_TECHNOLOGY");
        map.put("วิทยาศาสตร์","SCIENCE");
        map.put("อุตสาหกรรมเกษตร","AGRO_INDUSTRY");
        map.put("เทคโนโลยีสารสนเทศ","INFORMATION_TECHNOLOGY");
        map.put("วิทยาลัยนานาชาติ","INTERNATIONAL_COLLEGE");
        map.put("วิทยาลัยนาโนเทคโนโลยีพระจอมเกล้าลาดกระบัง","NANOTECHNOLOGY_COLLEGE");
        map.put("วิทยาลัยนวัตกรรมการจัดการข้อมูล", "DATA_STORAGE_INNOVATION_COLLEGE");
        map.put("วิทยาลัยการบริหารและจัดการ","ADMINISTRATION_AND_MANAGEMENT_COLLEGE");
    }

    private void register(){
        boolean check = true;
        EditText editTextUserName = (EditText)findViewById(R.id.userName);
        EditText editTextPassword = (EditText)findViewById(R.id.password);
        EditText editTextFirstName = (EditText)findViewById(R.id.firstName);
        EditText editTextLastName = (EditText)findViewById(R.id.lastName);
        EditText editTextDepartment = (EditText)findViewById(R.id.department);
        EditText editTextStdId = (EditText)findViewById(R.id.editTextStdId);
        RadioButton radioButtonTeacher = (RadioButton)findViewById(R.id.radioTeacher);
        RadioButton radioButtonStudent = (RadioButton)findViewById(R.id.radioStd);
        if(spinner.getSelectedItemPosition()==0){
            Toast.makeText(getApplicationContext(), "กรุณาเลือกคณะ", Toast.LENGTH_SHORT).show();
            check = false;
            return;
        }

        if(editTextUserName.getText().toString().equals("") && editTextUserName.getText().toString().contains(" ")){
            check = false;
            Toast.makeText(getApplicationContext(),"Username ต้องไม่เว้นวรรคและเว้นไว้",Toast.LENGTH_SHORT).show();
        }
        if(editTextPassword.getText().toString().equals("") && editTextPassword.getText().toString().contains(" ")){
            Toast.makeText(getApplicationContext(),"Password ต้องไม่เว้นวรรคและเว้นไว้",Toast.LENGTH_SHORT).show();
            check = false;
        }
        if(editTextFirstName.getText().toString().equals("")) check = false;
        if(editTextLastName.getText().toString().equals("")) check = false;
        if(editTextStdId.getText().toString().equals("") && radioButtonStudent.isChecked()) check = false;
        if(!check){
            Toast.makeText(getApplicationContext(),"กรอกข้อมูลไม่ครบ",Toast.LENGTH_SHORT).show();
            return;
        }


        String inParam = null;

        String url = null;
        String fac = map.get(spinner.getSelectedItem().toString());
        Log.i("TEST",spinner.getSelectedItem().toString());
        if(radioButtonTeacher.isChecked() && check){
            url = "http://203.151.92.187:8080/createTeacher";
            inParam = "username=" + URLEncoder.encode(editTextUserName.getText().toString()) +
                    "&password=" + URLEncoder.encode(editTextPassword.getText().toString()) +
                    "&firstName=" + URLEncoder.encode(editTextFirstName.getText().toString()) +
                    "&lastName=" + URLEncoder.encode(editTextLastName.getText().toString()) +
                    "&faculty=" + URLEncoder.encode(fac) +
                    "&department=" + URLEncoder.encode(editTextDepartment.getText().toString());
            if(fbId != null) inParam = inParam + "&facebookId=" + URLEncoder.encode(fbId);
        }
        else if(radioButtonStudent.isChecked() & check){
            url = "http://203.151.92.187:8080/createStudent";
            inParam = "username=" + URLEncoder.encode(editTextUserName.getText().toString()) +
                    "&password=" + URLEncoder.encode(editTextPassword.getText().toString()) +
                    "&studentId=" + URLEncoder.encode(editTextStdId.getText().toString()) +
                    "&firstName=" + URLEncoder.encode(editTextFirstName.getText().toString()) +
                    "&lastName=" + URLEncoder.encode(editTextLastName.getText().toString()) +
                    "&faculty=" + URLEncoder.encode(fac) +
                    "&department=" + URLEncoder.encode(editTextDepartment.getText().toString());
            if(fbId != null) inParam = inParam + "&facebookId=" + URLEncoder.encode(fbId);
        }
        else {
            Toast.makeText(RegisterActivity.this, "เลือกสถานะ", Toast.LENGTH_SHORT).show();
            check = false;
        }
        if(check){
            String result = "Fail";
            try {
                HttpPostConnection httpPostConnection = new HttpPostConnection();
                httpPostConnection.execute(url, inParam);
                String strJson = httpPostConnection.get();
                JSONObject jsonObject = new JSONObject(strJson);
                result = jsonObject.getString("status");
            }catch(ExecutionException e){e.printStackTrace();
            }catch(InterruptedException e){e.printStackTrace();
            }catch (JSONException e){ e.printStackTrace();}
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            FacebookSdk.clearLoggingBehaviors();
            finish();
        }
    }

    private void setBundle(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.fbId = bundle.getString("fbId");
        }
    }

    private void setBtn(){
        Button btn = (Button)findViewById(R.id.btnLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void setSpinner(){
        spinner = (Spinner)findViewById(R.id.spinnerRegisFac);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("เลือกคณะ");
        arrayList.add("วิศวกรรมศาสตร์");
        arrayList.add("สถาปัตยกรรมศาสตร์");
        arrayList.add("ครุศาสตร์อุตสาหกรรม");
        arrayList.add("เทคโนโลยีการเกษตร");
        arrayList.add("วิทยาศาสตร์");
        arrayList.add("อุตสาหกรรมเกษตร");
        arrayList.add("เทคโนโลยีสารสนเทศ");
        arrayList.add("วิทยาลัยนานาชาติ");
        arrayList.add("วิทยาลัยนาโนเทคโนโลยีพระจอมเกล้าลาดกระบัง");
        arrayList.add("วิทยาลัยนวัตกรรมการจัดการข้อมูล");
        arrayList.add("วิทยาลัยการบริหารและจัดการ");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
