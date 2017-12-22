package com.turlir.abakgists.allinone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.turlir.abakgists.R;
import com.turlir.abakgists.base.App;
import com.turlir.abakgists.base.BaseActivity;
import com.turlir.abakgists.widgets.DividerDecorator;
import com.turlir.abakgists.widgets.SearchingView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Func1;

public class AllInOneActivity extends BaseActivity {

    @Inject
    AllInOnePresenter _presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.viewpager)
    ViewPager pager;

    @BindView(R.id.search_recycler)
    RecyclerView recycler;

    private SearchAdapter mSearchAdapter;

    private TextView.OnEditorActionListener mHideSearchKeyboard = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_DONE:
                case EditorInfo.IME_ACTION_NEXT:
                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (manager != null) {
                        manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
            }
            return true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_in_one);
        ButterKnife.bind(this);

        App.getComponent().inject(this);
        _presenter.attach(this);

        setSupportActionBar(toolbar);

        PagerAdapter adapter = new TwoPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        DividerDecorator decorator = new DividerDecorator(
                this,
                R.drawable.divider,
                DividerDecorator.VERTICAL,
                DividerDecorator.DOUBLE_DIVIDER
        );
        recycler.addItemDecoration(decorator);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mSearchAdapter = new SearchAdapter();
        recycler.setAdapter(mSearchAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        invalidateOptionsMenu();
    }

    @Override
    protected void onStop() {
        super.onStop();
        _presenter.detachSearch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _presenter.detach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        setupSearch(searchItem);
        return true;
    }

    private void setupSearch(MenuItem searchItem) {
        final EditText searchingView = ((SearchingView) MenuItemCompat.getActionView(searchItem)).editText();
        searchingView.setOnEditorActionListener(mHideSearchKeyboard);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
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
                .map(new Func1<TextViewAfterTextChangeEvent, String>() {
                    @Override
                    public String call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                        return textViewAfterTextChangeEvent.editable().toString().trim();
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.length() > 3;
                    }
                });

        _presenter.connectSearch(obs);
    }

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

}