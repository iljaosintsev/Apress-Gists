package com.turlir.abakgists;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.notes.view.NotesFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AllInOneActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.viewpager)
    ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_in_one);
        ButterKnife.bind(this);

        PagerAdapter adapter = new SimplePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    private static class SimplePagerAdapter extends FragmentPagerAdapter {

        SimplePagerAdapter(FragmentManager fm) {
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
}
