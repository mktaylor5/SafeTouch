package com.safetouch.dao;

import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.safetouch.domain.Configuration;

import java.util.List;

/**
 * Created by mktay on 3/5/2018.
 */

@Dao
public interface ConfigurationDao {
    @Insert
    long insert(Configuration config);

    @Query("SELECT * FROM configuration")
    List<Configuration> getAll();

    @Update
    void UpdateConfiguration(Configuration config);

    @Query("SELECT AppMode FROM configuration")
    int getMode();

    @Query("SELECT EmergencyText FROM configuration")
    String getEmergencyMessage();

    // @Delete
    // void deleteContact(Configuration contact);
}
