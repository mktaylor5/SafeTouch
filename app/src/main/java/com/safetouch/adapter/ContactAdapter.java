package com.safetouch.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.safetouch.domain.Contact;

import java.util.List;

/**
 * Created by mktay on 3/1/2018.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {

    private final List<Contact> contactList;

    public ContactAdapter(@NonNull Context context, @LayoutRes int resource, List<Contact> contacts) {
        super(context, resource, contacts);
        this.contactList = contacts;
    }

    @Override
    public int getCount() { return super.getCount() + 1; }

    @Nullable
    @Override
    public Contact getItem(int position) {
        if (position == 0) {
            Contact contact = new Contact();
            contact.setContactID(0);
            contact.setName("Create new contact");
            return contact;
        }
        return super.getItem(position - 1);
    }

    public int getContactPosition(@Nullable Integer contactID) {
        if (contactID != null) {
            for (int i = 0; i < contactList.size(); i++) {
                if (contactID == contactList.get(i).getContactID()) {
                    return i + 1;
                }
            }
        }
        return -1;
    }
}
