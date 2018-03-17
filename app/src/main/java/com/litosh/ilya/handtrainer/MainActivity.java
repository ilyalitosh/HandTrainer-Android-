package com.litosh.ilya.handtrainer;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.litosh.ilya.handtrainer.adapters.ViewPagerAdapter;
import com.litosh.ilya.handtrainer.db.DBService;
import com.litosh.ilya.handtrainer.db.models.Activity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater layoutInflater;
    private List<View> pages;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TextView angleView;
    private Button buttonStartStop;
    private EditText inputCountRotations;
    private TextView sessionCountRotationsText;
    private TextView wholeCountRotationsText;
    private TextView headerNavMenu;
    private RelativeLayout background;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isStart = false;
    private byte side = 0;// -1 - лево, 1 - право
    private int sessionCountRotations;
    private int wholeCountRotations = 0;
    private boolean isBurgerPressed = false;
    private DBService dbService;
    private SensorEventListener accelerometerListener = new SensorEventListener() {
        int iterator;
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double angle = Math.toDegrees(Math.atan(sensorEvent.values[0]
                                          / Math.sqrt(Math.pow(sensorEvent.values[1], 2)
                                                      + Math.pow(sensorEvent.values[2], 2))));
            if(!isStart){
                if(Math.round(angle) >= 80){
                    isStart = true;
                    iterator = 0;
                    angleView.setText(String.valueOf(iterator));
                    side = 1;
                }
            }else{
                switch(side){
                    case -1:
                        if(Math.round(angle) >= 80){
                            iterator++;
                            wholeCountRotations++;
                            StringBuilder s = new StringBuilder(String.valueOf(iterator));
                            s.append("/").append(sessionCountRotations);
                            sessionCountRotationsText.setText(s);
                            wholeCountRotationsText.setText(String.valueOf(wholeCountRotations));
                            angleView.setText(String.valueOf(iterator));
                            side = 1;
                        }
                        break;
                    case 1:
                        if(Math.round(angle) <= -80){
                            iterator++;
                            wholeCountRotations++;
                            StringBuilder s = new StringBuilder(String.valueOf(iterator));
                            s.append("/").append(sessionCountRotations);
                            sessionCountRotationsText.setText(s);
                            wholeCountRotationsText.setText(String.valueOf(wholeCountRotations));
                            angleView.setText(String.valueOf(iterator));
                            side = -1;
                        }
                        break;
                }
                if(iterator == sessionCountRotations){
                    background.setBackgroundColor(Color.GREEN);
                    side = 0;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accelerometerListener, sensorAccelerometer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Realm.init(this);
        dbService = new DBService();

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.navigation_bar_open, R.string.navigation_bar_close){
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                isBurgerPressed = false;
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isBurgerPressed = true;
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.addHeaderView(getLayoutInflater().inflate(R.layout.header_nav_menu, null));

        layoutInflater = getLayoutInflater();
        pages = new ArrayList<>();
        View trainerPageView = layoutInflater.inflate(R.layout.trainer_page, null);
        pages.add(trainerPageView);
        View statsPageView = layoutInflater.inflate(R.layout.stats_page, null);
        pages.add(statsPageView);

        initComponents(trainerPageView, statsPageView);

        adapter = new ViewPagerAdapter(pages);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        initListeners();

        Button buttonAdd = trainerPageView.findViewById(R.id.button1);
        Button buttonShow = trainerPageView.findViewById(R.id.button2);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void initComponents(View trainerPage, View statsPage){
        angleView = trainerPage.findViewById(R.id.angle_textview_trainerpage);
        buttonStartStop = trainerPage.findViewById(R.id.startstop_button_trainerpage);
        inputCountRotations = trainerPage.findViewById(R.id.input_count_rotations_trainerpage);
        sessionCountRotationsText = trainerPage.findViewById(R.id.session_count_rotations_textview_trainerpage);
        wholeCountRotationsText = trainerPage.findViewById(R.id.whole_count_rotations_textview_trainerpage);
        background = trainerPage.findViewById(R.id.background_trainer_page);
        headerNavMenu = navigationView.getHeaderView(0).findViewById(R.id.title_header_nav_menu);
        headerNavMenu.setText(User.getUserLogin());
    }

    public void initListeners(){
        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStart){
                    sensorManager.unregisterListener(accelerometerListener, sensorAccelerometer);
                    background.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                    buttonStartStop.setText("Начать");
                    isStart = false;
                    angleView.setVisibility(View.GONE);
                    angleView.setText(getString(R.string.start_message));
                    inputCountRotations.setVisibility(View.VISIBLE);
                    StringBuilder s = new StringBuilder("0/");
                    s.append(sessionCountRotations);
                    sessionCountRotationsText.setText(s);
                }else{
                    background.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                    inputCountRotations.setVisibility(View.GONE);
                    angleView.setVisibility(View.VISIBLE);
                    if(inputCountRotations.getText().toString().equals("")){
                        sessionCountRotationsText.setText("свободная");
                    }else{
                        sessionCountRotations = Integer.valueOf(inputCountRotations.getText().toString());
                        StringBuilder s = new StringBuilder("0/");
                        s.append(sessionCountRotations);
                        sessionCountRotationsText.setText(s);
                    }
                    buttonStartStop.setText("Стоп");
                    sensorManager.registerListener(accelerometerListener, sensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.trainer_nav_menu:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        viewPager.setCurrentItem(0, true);
                        return true;
                    case R.id.stats_nav_menu:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        viewPager.setCurrentItem(1, true);
                        return true;
                    case R.id.exit_nav_menu:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        User.setActivity(0);
                        Activity activity = new Activity();
                        activity.setId(0L);
                        activity.setActivity(0);
                        dbService.updateActivity(activity);
                        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(isBurgerPressed){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
