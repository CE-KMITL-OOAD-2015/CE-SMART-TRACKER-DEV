package com.example.monoblos.myapplication00;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

//login with facebook from : https://developers.facebook.com/docs/facebook-login/android

public class LoginActivity extends AppCompatActivity {

    private TextView mtextDetial;
    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallback;
    private ProfileTracker mProfileTracker;

    private RadioButton rbS;
    private RadioButton rbT;

    private int status = 0;
    private boolean f = false;
    private String id = null;
    private String fName = null;
    private String lName = null;
    private String stdId = null;
    private String session = null;
    private String st = null; //std or teacher


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.fragment_login);
        setStatus(0);
        setBtnLogin();
        setBtnRegister();

        getSupportActionBar().setTitle("CE SMART TRACKER");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003366")));

        rbS = (RadioButton)findViewById(R.id.radioStd);
        rbT = (RadioButton)findViewById(R.id.radioTeacher);
        rbS.setChecked(true);
        rbT.setChecked(false);

        mCallbackManager = CallbackManager.Factory.create();
        mCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        if(profile2 != null) {
                            String fbId = profile2.getId();
                            int st = logingWithFB(fbId);
                            if(st==0){
                                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                                intent.putExtra("fbId", fbId);
                                startActivity(intent);
                            }
                            else {
                                if (((getSt().equals("teacher")&&getStatus()==1) || (getSt().equals("student")&&getStatus()==0))) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    SingletonID.getInstance().clear();
                                    FacebookSdk.clearLoggingBehaviors();
                                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                    }
                };
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        };

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(mCallbackManager, mCallback);
        LoginManager.getInstance().logOut();

        mtextDetial = (TextView)findViewById(R.id.textView);
    }

    private int logingWithFB(String fbId){
        String pass = "http://203.151.92.187:8080/loginWithFacebook";
        String inParam = "facebookId=" + fbId;
        String strJson = null;
        int res = 0;
        try {
                HttpPostConnection httpPostConnection = new HttpPostConnection();
                httpPostConnection.execute(pass, inParam);
                strJson = httpPostConnection.get();
                JSONObject jsonObject = new JSONObject(strJson);
                String result = jsonObject.getString("status");
                if(result.equals("success")){
                    res = 1;

                    this.session = jsonObject.getString("sessionId");
                    this.st = jsonObject.getString("userType");

                    JSONObject object = jsonObject.getJSONObject(st);
                    fName = object.getString("firstName");
                    lName = object.getString("lastName");
                    stdId = object.getString("studentId");
                    String user = object.getString("username");

                    SingletonID singletonID = SingletonID.getInstance();
                    singletonID.clear();
                    singletonID.setId(id);
                    singletonID.setFirstName(fName);
                    singletonID.setLastName(lName);
                    singletonID.setStdId(stdId);
                    singletonID.setSessionId(session);
                    singletonID.setUserName(user);

                    if(rbS.isChecked()) setStatus(0);
                    else if(rbT.isChecked()) setStatus(1);
                    if(singletonID.getStatus() != getStatus()) singletonID.setStatus(getStatus());
                }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }
        return res;
    }

    private void setStatus(int status){
        this.status = status;
    }
    private int getStatus(){
        return this.status;
    }
    private String getSt(){
        return this.st;
    }

    private void setBtnRegister(){
        Button bt = (Button)findViewById(R.id.regisBtn);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void setBtnLogin(){
        Button bt = (Button)findViewById(R.id.btnLogin);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    private void login(){
        HttpPostConnection httpPostConnection = new HttpPostConnection();
        TextView textId = (TextView)findViewById(R.id.textId);
        TextView textPass = (TextView)findViewById(R.id.textPass);

        if(rbS.isChecked()) setStatus(0);
        else if(rbT.isChecked()) setStatus(1);

        String pass = "http://203.151.92.187:8080/login";
        String strJson = null;
        String status = null;
        String user = textId.getText().toString();
        String password = textPass.getText().toString();

        try {
            String inParam = "username=" + URLEncoder.encode(user,"UTF-8") +
                    "&password=" + URLEncoder.encode(password,"UTF-8");
            httpPostConnection.execute(pass,inParam);
            strJson = httpPostConnection.get();
            if(strJson==null){
                return;
            }
            JSONObject jsonObject = new JSONObject(strJson);
            status = jsonObject.getString("status");
            String session = jsonObject.getString("sessionId");
            st = jsonObject.getString("userType");
            this.session = session;
            JSONObject object = jsonObject.getJSONObject(st);
            fName = object.getString("firstName");
            lName = object.getString("lastName");
            stdId = object.getString("studentId");

        }catch(ExecutionException e){e.printStackTrace();
        }catch(InterruptedException e){e.printStackTrace();
        }catch (JSONException e){e.printStackTrace();
        }catch(UnsupportedEncodingException e){e.printStackTrace();}

        SingletonID singletonID = SingletonID.getInstance();
        singletonID.clear();
        singletonID.setId(id);
        singletonID.setFirstName(fName);
        singletonID.setLastName(lName);
        singletonID.setStdId(stdId);
        singletonID.setSessionId(session);
        if(singletonID.getStatus() != getStatus()) singletonID.setStatus(getStatus());

        if ((status.equals("success") && ((st.equals("teacher")&&getStatus()==1) || (st.equals("student")&&getStatus()==0)))) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            SingletonID.getInstance().clear();
            FacebookSdk.clearLoggingBehaviors();
            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

}
