package com.turlir.abakgists.allinone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.notes.view.NotesFragment;

class TwoPagerAdapter extends FragmentPagerAdapter {

    TwoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AllGistsFragment();
            default:
                return new NotesFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "All Gists";
            default:
                return "Notes";
        }
    }
}
