package com.project.csfkids.csflocalscard;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Button buttonOpenNav;
    private Button buttonCenterMap;
    private int mMapSampling = 1;
    private int mMapRadius = 0;
    private SlidingUpPanelLayout sl;
    private float  mMapRadiusMax = 8;
    private boolean deleted = false;
    private boolean collapsed = true;
    private boolean mMapReady = false;
    private ViewGroup.MarginLayoutParams buttonOpenNavLayoutParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        main_Main = (LinearLayout) findViewById(R.id.activity_main_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        buttonOpenNav = (Button)findViewById(R.id.button_open_nav);
        buttonCenterMap = (Button)findViewById(R.id.button_center_map);
        buttonOpenNavLayoutParams= (ViewGroup.MarginLayoutParams) buttonOpenNav.getLayoutParams();
        navView = (NavigationView)findViewById(R.id.nav_view);
        mapFragment.getMapAsync(this);
        sl = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
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
                if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                    collapsed = true;
                }
                else if(newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
                    collapsed = false;
                }
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Log.d("DrawerSlide","Offset: " +slideOffset);
                if(collapsed == false) {
                    sl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    collapsed = true;
                }
                /*ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone((ConstraintLayout)findViewById(R.id.layout_buttons));*/
                FrameLayout.LayoutParams blp = (FrameLayout.LayoutParams)buttonOpenNav.getLayoutParams();
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(toDp(46),toDp(46));
                float newMargin = toDp(16)+(float)navView.getWidth()*slideOffset;
                lp.setMargins((int)newMargin,blp.topMargin,0,0);
                FrameLayout fl = (FrameLayout)findViewById(R.id.frameButtonOpenNav);
                fl.removeView(buttonOpenNav);
                fl.addView(buttonOpenNav,lp);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        buttonOpenNav.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        buttonCenterMap.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if(mMapReady){
                    LatLng coronado = new LatLng(32.6859, -117.1831);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(coronado));
                }
            }
        });
    }
    //https://github.com/umano/AndroidSlidingUpPanel
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


    public int toDp(int h){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, getResources().getDisplayMetrics());
    }

    public float toDp(float h){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, getResources().getDisplayMetrics());
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapReady = true;
        mMap = googleMap;
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.setPadding(0,200,0,200);
        LatLng curLatLng = mMap.getCameraPosition().target;
        Log.d("Google Maps","LAT: "+curLatLng.latitude+", LON: " + curLatLng.longitude);
        // Add a marker in Sydney and move the camera
        LatLng coronado = new LatLng(32.6859, -117.1831);
        if(BuildConfig.DEBUG) {
            mMap.addMarker(new MarkerOptions().position(coronado).title("Marker in Coronado"));
        }
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