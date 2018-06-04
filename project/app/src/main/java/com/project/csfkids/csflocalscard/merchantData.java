package com.project.csfkids.csflocalscard;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class merchantData implements PHPListener {
    public static ArrayList<JSONObject> merchantList;
    private GetPHP mAuth;
    private String url;
    public void load(){
        mAuth = new GetPHP(this, url);
        mAuth.execute(new String[]{});
    }

    public merchantData(String a){
        url = a;
    }

    public void onTaskCompleted(String output){
        Log.d("XD",output);
    }
}
