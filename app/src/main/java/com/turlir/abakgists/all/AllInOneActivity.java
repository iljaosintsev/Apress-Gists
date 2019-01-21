package com.turlir.abakgists.all;

import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.tabs.TabLayout;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.turlir.abakgists.R;
import com.turlir.abakgists.allgists.view.AllGistsFragment;
import com.turlir.abakgists.base.BaseActivity;
import com.turlir.abakgists.notes.view.NotesFragment;
import com.turlir.abakgists.widgets.SearchingView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class AllInOneActivity extends BaseActivity implements AllInOneView {

    @InjectPresenter
    AllInOnePresenter _presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_recycler)
    RecyclerView recycler;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.viewpager)
    ViewPager pager;

    private SearchAdapter mSearchAdapter;

    private TextView.OnEditorActionListener mHideSearchKeyboard = (view, action, event) -> {
        if (action == EditorInfo.IME_ACTION_DONE
                || action == EditorInfo.IME_ACTION_NEXT) {
            InputMethodManager manager = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        return true;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_in_one);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        mSearchAdapter = new SearchAdapter();
        recycler.setAdapter(mSearchAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        PagerAdapter adapter = new SimplePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        setupSearch(searchItem);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _presenter.detachView(this);
    }

    @Override
    public void onSearchResults(List<String> strings) {
        mSearchAdapter.replace(strings);
        if (mSearchAdapter.getItemCount() > 0) {
            recycler.setVisibility(View.VISIBLE);
            tabs.setVisibility(View.GONE);
        } else {
            recycler.setVisibility(View.GONE);
            tabs.setVisibility(View.VISIBLE);
        }
    }

    private void setupSearch(MenuItem searchItem) {
        final EditText searchingView = ((SearchingView) searchItem.getActionView()).editText();
        searchingView.setOnEditorActionListener(mHideSearchKeyboard);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Editable txt = searchingView.getText();
                searchingView.setText(txt);
                searchingView.setSelection(txt.length());
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                tabs.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
                return true;
            }
        });

        searchItem.collapseActionView(); // always close after restart

        Observable<String> obs = RxTextView.afterTextChangeEvents(searchingView)
                .debounce(1, TimeUnit.SECONDS)
                .map(textViewAfterTextChangeEvent -> {
                    Editable editable = textViewAfterTextChangeEvent.editable();
                    if (editable != null) {
                        return editable.toString().trim();
                    } else {
                        return "";
                    }
                })
                .filter(s -> s.length() > 3);

        _presenter.connectSearch(obs);
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
