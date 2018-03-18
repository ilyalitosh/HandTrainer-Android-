package com.litosh.ilya.handtrainer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.litosh.ilya.handtrainer.db.DBService;
import com.litosh.ilya.handtrainer.db.models.Activity;
import com.litosh.ilya.handtrainer.db.models.Person;

import java.util.List;

import io.realm.Realm;

/**
 * Created by ilya_ on 16.03.2018.
 */

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Realm.init(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DBService dbService = new DBService();
                Activity activity = dbService.getActivity();
                if(activity != null){
                    User.setId(0L);
                    User.setActivity(activity.getActivity());
                    User.setUserId(activity.getUserId());
                    if(User.getActivity() == 1){
                        List<Person> list = dbService.getPersons();
                        User.setUserLogin(list.get((int)User.getUserId()).getLogin());
                        User.setWholeCountRotations(list.get((int)User.getUserId()).getWholeCountRotations());
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashScreenActivity.this, AuthActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Intent intent = new Intent(SplashScreenActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 5000);

    }
}
