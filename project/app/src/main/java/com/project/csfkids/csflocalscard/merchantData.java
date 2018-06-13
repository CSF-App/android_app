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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class merchantData implements PHPListener {
    public JSONObject merchantList;
    public static Map<String, Map<String,String>> merchantMap;
    private GetPHP mAuth;
    private String url;
    public void load(){
        merchantMap = new HashMap<String,Map<String,String>>();
        mAuth = new GetPHP(this, url);
        mAuth.execute(new String[]{"",""});
    }

    public merchantData(String a){
        url = a;
    }

    public void onTaskCompleted(String output){
        try {
            merchantList = new JSONObject(output);
            Iterator<String> keys = merchantList.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                Iterator<String> subKeys = ((JSONObject)merchantList.get(key)).keys();
                Map<String,String> bta = new HashMap<String,String>();
                while(subKeys.hasNext()) {
                    String subKey = subKeys.next();
                    bta.put(subKey,((JSONObject)merchantList.get(key)).getString(subKey));
                }
                merchantMap.put(key,bta);
            }
        }
        catch(org.json.JSONException e) {
            Log.d("merchantData", e.toString());
        }
    }
}
