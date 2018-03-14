package com.safetouch.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.safetouch.R;

/**
 * Created by mktay on 3/5/2018.
 */

public class ConfigurationActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//for back on actionbar
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();// go to parent activity.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
        }
    }



}