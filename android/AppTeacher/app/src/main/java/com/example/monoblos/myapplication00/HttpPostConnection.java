package com.example.monoblos.myapplication00;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by MoNoblos on 11/3/2015.
 */
public class HttpPostConnection extends AsyncTask<String, String, String>{
    @Override
    protected String doInBackground(String... params) {
        String strUrl = params[0];
        String inParam = params[1];
        String result = null;

        try{
            URL url = new URL(strUrl);

            URLConnection httpURLConnection = url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("charset", "utf-8");
            httpURLConnection.connect();

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(inParam);
            wr.flush();
            wr.close();

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            result = sb.toString();
            br.close();
        }catch(IllegalStateException e){ e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
