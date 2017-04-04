package com.turlir.abakgists;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllInOneActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.viewpager)
    ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_in_one);
        ButterKnife.bind(this);

        AllGistsFragment allGist = new AllGistsFragment();
        NotesFragment note = new NotesFragment();
        PagerAdapter adapter = new SimplePagerAdapter(getSupportFragmentManager(), allGist, note);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    private static class SimplePagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentTabs;

        SimplePagerAdapter(FragmentManager fm, Fragment... tabs) {
            super(fm);
            mFragmentTabs = new ArrayList<>(tabs.length);
            mFragmentTabs.addAll(Arrays.asList(tabs));
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentTabs.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getItem(position).toString();
        }
    }
}
