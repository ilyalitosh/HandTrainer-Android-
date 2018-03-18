package com.litosh.ilya.handtrainer;

import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.litosh.ilya.handtrainer.adapters.StatsListAdapter;
import com.litosh.ilya.handtrainer.adapters.ViewPagerAdapter;
import com.litosh.ilya.handtrainer.db.DBService;
import com.litosh.ilya.handtrainer.db.models.Activity;
import com.litosh.ilya.handtrainer.db.models.Person;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private TextView wholeCountRotationsTextStats;
    private TextView durationTime;
    private RelativeLayout background;
    private ListView statsList;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isStart = false;
    private byte side = 0;// -1 - лево, 1 - право
    private int sessionCountRotations; // -1 - свободная тренировка
    private int wholeCountRotations = 0;
    private boolean isBurgerPressed = false;
    private DBService dbService;
    private int iterator;
    private int duration;
    private String date;
    private SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double angle = Math.toDegrees(Math.atan(sensorEvent.values[0]
                                          / Math.sqrt(Math.pow(sensorEvent.values[1], 2)
                                                      + Math.pow(sensorEvent.values[2], 2))));
            if(!isStart){
                if(Math.round(angle) >= 80){
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = sdf.format(c.getTime());
                    isStart = true;
                    iterator = 0;
                    duration = 0;
                    angleView.setText(String.valueOf(iterator));
                    side = 1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isStart){
                                System.out.println(duration);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                                if(side != 0){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            duration++;
                                            StringBuilder s = new StringBuilder(String.valueOf(duration)).append(" сек");
                                            durationTime.setText(s);
                                        }
                                    });
                                }
                            }
                        }
                    }).start();
                }
            }else{
                switch(side){
                    case -1:
                        if(Math.round(angle) >= 80){
                            iterator++;
                            wholeCountRotations++;
                            if(sessionCountRotations == -1){
                                sessionCountRotationsText.setText(String.valueOf(iterator));
                            }else {
                                StringBuilder s = new StringBuilder(String.valueOf(iterator));
                                s.append("/").append(sessionCountRotations);
                                sessionCountRotationsText.setText(s);
                            }
                            wholeCountRotationsText.setText(String.valueOf(wholeCountRotations));
                            wholeCountRotationsTextStats.setText(String.valueOf(wholeCountRotations));
                            angleView.setText(String.valueOf(iterator));
                            side = 1;
                        }
                        break;
                    case 1:
                        if(Math.round(angle) <= -80){
                            iterator++;
                            wholeCountRotations++;
                            if(sessionCountRotations == -1){
                                sessionCountRotationsText.setText(String.valueOf(iterator));
                            }else {
                                StringBuilder s = new StringBuilder(String.valueOf(iterator));
                                s.append("/").append(sessionCountRotations);
                                sessionCountRotationsText.setText(s);
                            }
                            wholeCountRotationsText.setText(String.valueOf(wholeCountRotations));
                            wholeCountRotationsTextStats.setText(String.valueOf(wholeCountRotations));
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

    }

    public void initComponents(View trainerPage, View statsPage){
        angleView = trainerPage.findViewById(R.id.angle_textview_trainerpage);
        buttonStartStop = trainerPage.findViewById(R.id.startstop_button_trainerpage);
        inputCountRotations = trainerPage.findViewById(R.id.input_count_rotations_trainerpage);
        sessionCountRotationsText = trainerPage.findViewById(R.id.session_count_rotations_textview_trainerpage);
        wholeCountRotationsText = trainerPage.findViewById(R.id.whole_count_rotations_textview_trainerpage);
        durationTime = trainerPage.findViewById(R.id.duration_rotations_textview_trainerpage);
        wholeCountRotations = User.getWholeCountRotations();
        wholeCountRotationsText.setText(String.valueOf(wholeCountRotations));
        background = trainerPage.findViewById(R.id.background_trainer_page);
        headerNavMenu = navigationView.getHeaderView(0).findViewById(R.id.title_header_nav_menu);
        headerNavMenu.setText(User.getUserLogin());
        statsList = statsPage.findViewById(R.id.stats_listview_statpage);
        wholeCountRotationsTextStats = statsPage.findViewById(R.id.whole_count_rotations_textview_statpage);
        wholeCountRotationsTextStats.setText(String.valueOf(wholeCountRotations));
        StatsListAdapter adapter = new StatsListAdapter(this);
        statsList.setAdapter(adapter);

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
                    if(sessionCountRotations == -1){
                        sessionCountRotationsText.setText(String.valueOf(0));
                    }else {
                        StringBuilder s = new StringBuilder(String.valueOf(0));
                        s.append("/").append(sessionCountRotations);
                        sessionCountRotationsText.setText(s);
                    }
                    Person person = new Person();
                    person.setId(User.getUserId());
                    person.setWholeCountRotations(wholeCountRotations);
                    dbService.updateCountRotationsPerson(person);

                    User.setSessionCountRotations(sessionCountRotations);
                    User.setFinishedCountRotations(iterator);
                    User.setCurrentDuration(duration);
                    User.setCurrentDate(date);

                    CommentDialog a1 = new CommentDialog(MainActivity.this);
                    a1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            StatsListAdapter adapter = new StatsListAdapter(MainActivity.this);
                            statsList.setAdapter(adapter);
                        }
                    });
                    a1.show();

                }else{
                    background.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                    inputCountRotations.setVisibility(View.GONE);
                    angleView.setVisibility(View.VISIBLE);
                    if(inputCountRotations.getText().toString().equals("")){
                        sessionCountRotationsText.setText("0");
                        sessionCountRotations = -1;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.application_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                if(isBurgerPressed){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.reset_stats:
                resetStats();
                StatsListAdapter adapter = new StatsListAdapter(MainActivity.this);
                statsList.setAdapter(adapter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetStats(){
        dbService.deleteNotesById(User.getUserId());

        Person person = new Person();
        person.setId(User.getUserId());
        person.setWholeCountRotations(0);
        dbService.updateCountRotationsPerson(person);

        User.setWholeCountRotations(0);
        wholeCountRotations = 0;
        wholeCountRotationsText.setText(String.valueOf(0));
        wholeCountRotationsTextStats.setText(String.valueOf(0));
    }

}
