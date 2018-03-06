package com.safetouch.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by mktay on 3/5/2018.
 */

@Entity(tableName = "configuration")
public class Configuration {

    // ConfigurationID Column Definition
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ConfigurationID")
    private int ConfigurationID;

    // ConfigurationID Methods
    public int getConfigurationID() { return ConfigurationID; }
    public void setConfigurationID(int configID) { this.ConfigurationID = configID; }

    // Emergency Text Column Definition
    @ColumnInfo(name = "EmergencyText")
    private String EmergencyText;

    // Emergency Text Methods
    public String getEmergencyText() { return EmergencyText; }
    public void setEmergencyText(String emergencyText) { this.EmergencyText = emergencyText; }

    // Override Methods
    @Override
    public String toString() { return EmergencyText; }
}
