package com.safetouch.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by mktay on 3/1/2018.
 */

@Entity(tableName = "contact")
public class Contact {

    //ContactID Column Definition
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ContactID")
    private int ContactID;

    //ContactID Methods
    public int getContactID() { return ContactID; }
    public void setContactID(int contactID) { this.ContactID = contactID; }

    //Contact Name Column Definition
    @ColumnInfo(name = "Name")
    private String Name;

    //Contact Name Methods
    public String getName() { return Name; }
    public void setName(String name) { this.Name = name; }

    //Override Methods
    @Override
    public String toString() { return Name; }
}
