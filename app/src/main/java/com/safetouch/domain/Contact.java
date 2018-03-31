package com.safetouch.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by mktay on 3/1/2018.
 */

@Entity(tableName = "contact")
public class Contact {

    public Contact() {
        IsApproved = false;
    }

    public Contact(String name, String phoneNumber) {
        Name = name;
        PhoneNumber = phoneNumber;
        IsApproved= false;
    }

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

    //Contact Phone Number Column Definition
    @ColumnInfo(name = "PhoneNumber")
    private String PhoneNumber;

    //Contact Phone Methods
    public String getPhoneNumber() { return PhoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.PhoneNumber = phoneNumber; }

    //Contact IsApproved Flag
    @ColumnInfo(name = "IsApproved")
    private boolean IsApproved;

    //Contact IsApproved Methods
    public boolean getIsApproved() { return IsApproved; }
    public void setIsApproved(boolean isApproved) { this.IsApproved = isApproved; }






    //Override Methods
    @Override
    public String toString() { return Name + " - " + PhoneNumber; }
}
