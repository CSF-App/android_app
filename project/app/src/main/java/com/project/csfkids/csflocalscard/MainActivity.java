package com.project.csfkids.csflocalscard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatImageButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, PHPListener {

    private final boolean enableBlur = false;
    private GoogleMap mMap;
    private LinearLayout main_Main;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private AppCompatImageButton buttonOpenNav;
    private AppCompatImageButton buttonCenterMap;
    private TextInputEditText searchBar;
    private int mMapSampling = 1;
    private int mMapRadius = 0;
    private SlidingUpPanelLayout sl;
    private float  mMapRadiusMax = 8;
    private boolean deleted = false;
    private boolean collapsed = true;
    private boolean mMapReady = false;
    private int markerClickAnimateDuration = 200;
    private ConstraintLayout donateButtonMenu;
    private ViewGroup.MarginLayoutParams buttonOpenNavLayoutParams;
    private String donateUrl;

    //MDAT
    private ConstraintLayout mDat;
    private ImageView mDatExterior;
    private LinearLayout mDatContainer;
    private TextView mDatBName;
    private ImageView mDatBImage;
    private TextView mDatDescription;
    private TextView mDatURL;
    private mdatController mDatController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //GET MERCHANT DATA
        main_Main = (LinearLayout) findViewById(R.id.activity_main_main);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        buttonOpenNav = (AppCompatImageButton) findViewById(R.id.button_open_nav);
        buttonCenterMap = (AppCompatImageButton)findViewById(R.id.button_center_map);
        buttonOpenNavLayoutParams= (ViewGroup.MarginLayoutParams) buttonOpenNav.getLayoutParams();
        navView = (NavigationView)findViewById(R.id.nav_view);
        searchBar = (TextInputEditText)findViewById(R.id.search_bar);
        //donateButtonMenu = findViewById(R.id.menu_donate_button);
        donateUrl = getString(R.string.url_donate);
        //MDAT
        mDat = findViewById(R.id.mdat);
        mDatExterior = findViewById(R.id.mdat_exterior);
        mDatContainer = findViewById(R.id.mdat_container);
        mDatBName = findViewById(R.id.mdat_bname);
        mDatBImage = findViewById(R.id.mdat_bimage);
        mDatDescription = findViewById(R.id.mdat_description);
        mDatURL = findViewById(R.id.mdat_link);
        mDatController = new mdatController();
        mDatController.hide();
        mDatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mDatExterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatController.hide();
            }
        });

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
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(toDp(26),toDp(26));
                float newMargin = toDp(23)+(float)navView.getWidth()*slideOffset;
                lp.setMargins((int)newMargin,toDp(23),0,0);
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
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(coronado),100,null);
                }
            }
        });

        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    sl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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

    public int toDp(int h){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, getResources().getDisplayMetrics());
    }

    public float toDp(float h){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, h, getResources().getDisplayMetrics());
    }

    HashMap<String, HashMap<String,String>> xd;
    public static HashMap<String,String> currentMarker = null;
    Marker coronadoMarker;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapReady = true;
        mMap = googleMap;
        mMap.setMinZoomPreference(14.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.setPadding(0,200,0,200);
        LatLng curLatLng = mMap.getCameraPosition().target;
        xd = new HashMap<String, HashMap<String, String>>(){{
            put("California American Water", new HashMap<String, String>(){{
                put("bname","California American Water");
                put("pcat","Services");
                put("pstreet","1025 Palm Avenue");
                put("pcity","Imperial Beach");
                put("pstate","CA");
                put("pzip","91932");
                put("phone","1-619-446-5707");
                put("web","http://amwater.com/caaw");
                put("mail",null);
                put("lat",Double.toString(32.583551));
                put("lng",Double.toString(-117.111496));
                put("deal","Free program that will help save water and money." +
                        " A conservation specialist will visit your home or business" +
                        " to identify ways to save water indoors and out, check  irrigation" +
                        " systems for leaks, teach to read the meter, and create an" +
                        " annual watering schedule.");
                put("bimage",""+R.drawable.american_water);
            }});
            put("High Tide Bottle Shop & Kitchen", new HashMap<String, String>(){{
                put("bname","High Tide Bottle Shop & Kitchen");
                put("pcat","Dining");
                put("pstreet","933 Orange Avenue");
                put("pcity","Coronado");
                put("pstate","CA");
                put("pzip","92118");
                put("phone","1-619-435-1380");
                put("web","http://www.hightidecoronado.com/");
                put("mail",null);
                put("lat",Double.toString(32.686875));
                put("lng",Double.toString(-117.179192));
                put("deal","10% off any food item. Not to be combined with any other offer.");
                put("bimage",""+R.drawable.high_tide);
            }});
            put("Emerald City The Boarding Source", new HashMap<String, String>(){{
                put("bname","Emerald City The Boarding Source");
                put("pcat","Shopping");
                put("pstreet","1118 Orange Ave");
                put("pcity","Coronado");
                put("pstate","CA");
                put("pzip","92118");
                put("phone","1-619-435-6677");
                put("web","http:///www.emeraldcitysurf.com");
                put("mail",null);
                put("lat",Double.toString(32.684714));
                put("lng",Double.toString(-117.179826));
                put("deal","10% off regularly priced merchandise everyday.20% off Emerald City items.1/2 off rentals Monday-Friday only.");
                put("bimage",""+R.drawable.emerald_city);
            }});
            put("Charisma", new HashMap<String, String>(){{
                put("bname","Charisma");
                put("pcat","Shopping");
                put("pstreet","1158 Orange Ave");
                put("pcity","Coronado");
                put("pstate","CA");
                put("pzip","92118");
                put("phone","1-619-435-5542");
                put("web","http://www.charismacoronado.com/");
                put("mail",null);
                put("lat",Double.toString(32.684351));
                put("lng",Double.toString(-117.179380));
                put("deal","10% off in stock merchandise, some exceptions apply");
                put("bimage",""+R.drawable.charisma);
            }});
        }};

        for(String key : xd.keySet()) {
            HashMap<String,String> mdat = xd.get(key);
            double la = Double.parseDouble(mdat.get("lat"));
            double lo = Double.parseDouble(mdat.get("lng"));
            Log.d("LatLong",""+key+":LAT"+la+", LNG"+lo);
            LatLng keyLatLng = new LatLng(la, lo);
            Marker a = mMap.addMarker(new MarkerOptions().position(keyLatLng).title(key));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMap.animateCamera (
                        CameraUpdateFactory.newLatLngZoom(marker.getPosition(),mMap.getCameraPosition().zoom),
                        markerClickAnimateDuration, null);
                if(xd.containsKey(marker.getTitle())) {
                    Log.d("MARKER CLICKED",xd.get(marker.getTitle()).get("pstreet"));
                    mDatController.setData(xd.get(marker.getTitle()));
                    mDatController.show();
                    return true;
                }
                marker.showInfoWindow();
                return false;
            }
        });
        Log.d("Google Maps","LAT: "+curLatLng.latitude+", LON: " + curLatLng.longitude);
        // Add a marker in Sydney and move the camera
        LatLng coronado = new LatLng(32.6859, -117.1831);
        if(BuildConfig.DEBUG) {
            mMap.addMarker(new MarkerOptions().position(coronado).title("Coronado").snippet("THIS IS CORONADO"));
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

    public void onTaskCompleted(String output){

    }


    class mdatController {
        private boolean state = false;
        public final boolean mdatActive = true;
        public final boolean mdatHidden = false;
        private HashMap<String, String> mdatData = null;
        public void show(){
            if(mdatData != null) {
                mDat.setVisibility(View.VISIBLE);
                state = true;
            }
            else
                state = false;
        }
        public void setData(HashMap<String, String> dat){
            mdatData = new HashMap<String, String>(dat);
            mDatBName.setText(mdatData.get("bname"));
            /*if(!mdatData.containsKey("bimage")) {
                new DownLoadImageTask(mDatBImage).execute(mdatData.get("bimage"));
            }*/

            mDatBImage.setImageResource(Integer.parseInt(mdatData.get("bimage")));
            mDatDescription.setText(mdatData.get("deal"));
            mDatURL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(mdatData.get("web"));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
        public void hide(){
            mDat.setVisibility(View.GONE);
            state = false;
        }

        public boolean getState() {
            return state;
        }
    }
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}