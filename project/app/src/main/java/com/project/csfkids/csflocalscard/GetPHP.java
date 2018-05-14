package com.project.csfkids.csflocalscard;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GetPHP extends AsyncTask<String[], Void, String> {
    private PHPListener listener;
    private String link;

    public GetPHP(PHPListener listener, String link) {
        this.listener = listener;
        this.link = link;
    }

    @Override
    protected String doInBackground(String[]... arg0) {
        try {
            URL url;
            String data = "";
            for(int i = 0; i<arg0.length;i++) {
                if(i!=0)
                    data+="&";
                data+=URLEncoder.encode(arg0[i][0],"UTF-8") + "=" + URLEncoder.encode(arg0[i][1],"UTF-8");
            }
            Log.d("GetPHP","DATA: " + data);
            try {
                url = new URL(link);
            } catch (MalformedURLException e) {
                return new String("Exception: " + e.getMessage());
            }
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    protected void onPostExecute(String result) {
        listener.onTaskCompleted(result);
    }
}