package com.infosys.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.infosys.R;
import com.infosys.base.classes.BaseFragmentActivity;
import com.infosys.webservices.Constants;
import com.infosys.webservices.MySharedPreferences;

import org.json.JSONArray;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 6/18/2017.
 */

public class FeedActivity extends BaseFragmentActivity {
    private FeedFragment frag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setUi();
    }

    private void setUi() {
        setTitleOnAction("Feed", "", false);
        frag = FeedFragment.newInstance();
        replaceFragment(frag, FeedFragment.TAG, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        Map<String, ?> json = MySharedPreferences.newInstance().getMap(this);
        if (json != null && json.size() > 0) {
            Object jRows = json.get(Constants.rows);
            if(jRows == null)
                return;

            if (jRows instanceof JSONArray) {
                JSONArray jArr = (JSONArray) jRows;
                if (frag != null) {
                    frag.setFeedsOnAdapter(jArr);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        MySharedPreferences.newInstance().clearShareFile(this);
        super.onDestroy();
    }
}
