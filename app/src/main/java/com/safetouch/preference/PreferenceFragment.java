//package com.safetouch.preference;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.preference.Preference;
//import android.support.v7.preference.PreferenceManager;
//
//public class PreferenceFragment extends android.support.v7.preference.PreferenceManager {
//
//    private FragmentManager fragmentManager;
//
//    @SuppressLint("RestrictedApi")
//    public PreferenceFragment(Context context) {
//        super(context);
//    }
//
//    //@Override
////    public void onDisplayPreferenceDialog(Preference preference) {
////        DialogFragment fragment;
////        if (preference instanceof TimePreference) {
////            fragment = TimePreferenceDialogFragmentCompat.newInstance(preference.getKey());
////            fragment.setTargetFragment(fragment, 0);
////            fragment.show(getFragmentManager(),
////                    "android.support.v7.preference.PreferenceFragment.DIALOG");
////        } //else super.onDisplayPreferenceDialog(preference);
////    }
//
//    public FragmentManager getFragmentManager() {
//        return fragmentManager;
//    }
//}
