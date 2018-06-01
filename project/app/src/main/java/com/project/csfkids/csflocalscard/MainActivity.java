package com.project.csfkids.csflocalscard;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private final boolean enableBlur = false;
    private GoogleMap mMap;
    private LinearLayout main_Main;
    private int mMapSampling = 1;
    private int mMapRadius = 0;
    private float  mMapRadiusMax = 8;
    private boolean deleted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        main_Main = (LinearLayout) findViewById(R.id.activity_main_main);
        mapFragment.getMapAsync(this);
        SlidingUpPanelLayout sl = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        sl.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                /*if(enableBlur) {
                    if (slideOffset == 0 && deleted == false) {
                        Blurry.delete((ConstraintLayout) findViewById(R.id.content_frame));
                        deleted = true;
                    }
                    if (mMapRadius != (int) (mMapRadiusMax * slideOffset)) {
                        mMapRadius = (int) (mMapRadiusMax * slideOffset);
                        deleted = false;
                        blurMap();
                        Log.d("MapRadius", "" + mMapRadius);
                    }
                }*/
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });

    }//https://github.com/umano/AndroidSlidingUpPanel
    //https://github.com/Manabu-GT/EtsyBlur BLUR EFFECT
//    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private double longitude, latitude;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMinZoomPreference(15.0f);
        mMap.setMinZoomPreference(12.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.setPadding(40,40,40,180);
        LatLng curLatLng = mMap.getCameraPosition().target;
        Log.d("Google Maps","LAT: "+curLatLng.latitude+", LON: " + curLatLng.longitude);
        // Add a marker in Sydney and move the camera
        LatLng coronado = new LatLng(32.6859, -117.1831);
        mMap.addMarker(new MarkerOptions().position(coronado).title("Marker in Coronado"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coronado));
        /*mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                ImageView blurView = (ImageView) findViewById(R.id.blur_view);
                blurView.setVisibility(View.VISIBLE);
                blurView.setImageBitmap(bitmap);
                ConstraintLayout mRootLayout = (ConstraintLayout) findViewById(R.id.content_frame);
                Blurry.with(getApplicationContext()).radius((int)mMapRadius).sampling(mMapSampling).onto(mRootLayout);
            }
        });*/
    }

    private void blurMap() {
        /*ConstraintLayout mRootLayout = (ConstraintLayout) findViewById(R.id.content_frame);
        Blurry.delete(mRootLayout);
        Blurry.with(getApplicationContext()).radius((int)mMapRadius).sampling(mMapSampling).onto(mRootLayout);*/
    }

}