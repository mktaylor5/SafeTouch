package com.safetouch.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.safetouch.domain.Contact;

import java.util.List;

/**
 * Created by mktay on 3/1/2018.
 */

@Dao
public interface ContactDao {
    @Insert
    long insert(Contact contact);

    @Query("SELECT * FROM contact WHERE ContactID = 'contactID'")
    Contact getContactById(int contactID);

    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Update
    void updateContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);
}
