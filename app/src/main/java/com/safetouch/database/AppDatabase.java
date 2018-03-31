package com.safetouch.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.safetouch.R;
import com.safetouch.dao.ConfigurationDao;
import com.safetouch.dao.ContactDao;
import com.safetouch.domain.Configuration;
import com.safetouch.domain.Contact;

/**
 * Created by mktay on 3/1/2018.
 */

@Database(entities = {Contact.class, Configuration.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ContactDao getContactDao();
    public abstract ConfigurationDao getConfigurationDao();

    private static AppDatabase Database;

    public static AppDatabase getInstance(Context context) {
        if (null == Database) {
            Database = buildDatabaseInstance(context);
        }
        return Database;
    }

    private static AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, String.valueOf(R.string.db_name)).allowMainThreadQueries().build();
    }
}
