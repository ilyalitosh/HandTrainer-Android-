package com.litosh.ilya.handtrainer.db;

import com.litosh.ilya.handtrainer.db.migration.DBMigration;
import com.litosh.ilya.handtrainer.db.models.Activity;
import com.litosh.ilya.handtrainer.db.models.Note;
import com.litosh.ilya.handtrainer.db.models.Person;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by ilya_ on 17.03.2018.
 */

public class DBService {
    RealmConfiguration dbConfig = new RealmConfiguration.Builder()
            .schemaVersion(1)
            .migration(new DBMigration())
            .build();

    public void addPerson(Person person){
        Realm realm = Realm.getInstance(dbConfig);
        long id;
        try{
            id = realm.where(Person.class).max("id").intValue() + 1;
        }catch (Exception e){
            id = 0L;
        }
        person.setId(id);
        realm.beginTransaction();
        realm.insert(person);
        realm.commitTransaction();
    }

    public List<Person> getPersons(){
        Realm realm = Realm.getInstance(dbConfig);
        realm.beginTransaction();
        RealmResults<Person> result;
        result = realm.where(Person.class).findAll();
        List<Person> list = new ArrayList<>();
        list.addAll(result);
        realm.commitTransaction();
        return list;
    }

    public void addNote(Note note){
        Realm realm = Realm.getInstance(dbConfig);
        realm.beginTransaction();
        realm.insert(note);
        realm.commitTransaction();
    }

    public List<Note> getNotes(){
        Realm realm = Realm.getInstance(dbConfig);
        realm.beginTransaction();
        RealmResults<Note> result;
        result = realm.where(Note.class).findAll();
        List<Note> list = new ArrayList<>();
        list.addAll(result);
        realm.commitTransaction();
        return list;
    }

    public void updateActivity(Activity activity){
        Realm realm = Realm.getInstance(dbConfig);
        realm.beginTransaction();
        RealmResults<Activity> result;
        result = realm.where(Activity.class).findAll();
        if(result.size() == 0){
            realm.insert(activity);
        }else{
            Activity result1 = realm.where(Activity.class).equalTo("id", 0L).findFirst();
            result1.setActivity(activity.getActivity());
            result1.setUserId(activity.getUserId());
        }
        realm.commitTransaction();
    }

    public Activity getActivity(){
        Realm realm = Realm.getInstance(dbConfig);
        realm.beginTransaction();
        Activity activity = null;
        Activity result = realm.where(Activity.class).equalTo("id", 0L).findFirst();
        if(result != null){
            activity = new Activity();
            activity.setId(0L);
            activity.setActivity(result.getActivity());
            activity.setUserId(result.getUserId());
        }
        realm.commitTransaction();
        return activity;
    }

}
