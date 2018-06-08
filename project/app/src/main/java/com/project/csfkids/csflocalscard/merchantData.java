package com.project.csfkids.csflocalscard;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

public class merchantData implements PHPListener {
    public JSONObject merchantList;
    private GetPHP mAuth;
    private String url;
    public void load(){
        mAuth = new GetPHP(this, url);
        mAuth.execute(new String[]{"",""});
    }

    public merchantData(String a){
        url = a;
    }

    public void onTaskCompleted(String output){
        Log.d("merchantData",output);
        try {
            merchantList = new JSONObject(output);
        }
        catch(org.json.JSONException e){
            Log.d("merchantData",e.toString());
        }
        try {
            Log.d("MARCHANT DUTA",
                    merchantList.get("California American Water").toString());
        }
        catch(JSONException e){
            Log.d("merchantData",e.toString());
        }
    }
}
