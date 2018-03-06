package com.safetouch.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.safetouch.dao.ContactDao;
import com.safetouch.domain.Configuration;
import com.safetouch.domain.Contact;

/**
 * Created by mktay on 3/1/2018.
 */

@Database(entities = {Contact.class, Configuration.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "safetouch_db";

    public abstract ContactDao getContactDao();
}
