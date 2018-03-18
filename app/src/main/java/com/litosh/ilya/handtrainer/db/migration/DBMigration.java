package com.litosh.ilya.handtrainer.db.migration;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by ilya_ on 17.03.2018.
 */

public class DBMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if(oldVersion == 0){
            schema.create("Person")
                    .addField("login", String.class)
                    .addField("password", String.class)
                    .addField("wholeCountRotations", Integer.class);
            schema.create("Note")
                    .addField("id", Long.class)
                    .addField("task", String.class)
                    .addField("comment", String.class)
                    .addField("duration", Integer.class)
                    .addField("date", String.class);
            schema.create("Activity")
                    .addField("activity", Integer.class)
                    .addField("userId", Long.class);
            oldVersion++;
        }
    }

    @Override
    public int hashCode() {
        return 1 + super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DBMigration);
    }
}
