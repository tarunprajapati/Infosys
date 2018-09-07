package com.infosys.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infosys.R;
import com.infosys.base.classes.BaseFragment;
import com.infosys.databinding.FragFeedsBinding;
import com.infosys.managers.ApiManager;
import com.infosys.webservices.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 22/7/16.
 */
public class FeedFragment extends BaseFragment {
    public static final String TAG = "Order List";

    private FeedAdapter feedAdapter;
    Context context;
    private FragFeedsBinding binding;

    public FeedFragment() {
    }

    public static FeedFragment newInstance() {
        FeedFragment fr = new FeedFragment();
        return fr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_feeds, container, false);
        setHasOptionsMenu(true);
        setTitleOnAction("Feeds", "", false);
        recycleViewSetting();
        feedAdapter = new FeedAdapter(this);
        getFeeds();
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_scrolling, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                return true;

            case R.id.action_refresh:
                if (feedAdapter != null)
                    feedAdapter.clearList();

                getFeeds();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getFeeds() {
        ApiManager.getInstance().getFeeds(this);
    }

    private void checkEmpty() {
        if (feedAdapter.getList() == null || feedAdapter.getList().size() == 0) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvEmpty.setVisibility(View.GONE);
        }
        binding.tvEmpty.invalidate();

    }

    private void setOrderList(ArrayList<Feed> list) {
        feedAdapter.setList(list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(feedAdapter);
        binding.recyclerView.setNestedScrollingEnabled(false);
        feedAdapter.notifyDataSetChanged();

        checkEmpty();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public Boolean callBackFromApi(Object object, int requestCode) {
        if (super.callBackFromApi(object, requestCode)) {
            JSONObject jObj = (JSONObject) object;
            if (requestCode == Constants.FEEDS) {
                if (getActivity() == null)
                    return true;

                String t = jObj.optString(Constants.title);
                setTitleOnAction(t, "", false);

                JSONArray jFeeds = jObj.optJSONArray(Constants.rows);
                setFeedsOnAdapter(jFeeds);
            }
        }

        checkEmpty();
        return true;
    }

    public void setFeedsOnAdapter(JSONArray jObj) {
        if (jObj != null && jObj.length() > 0) {
            TypeToken<List<Feed>> token = new TypeToken<List<Feed>>() {
            };

            ArrayList<Feed> orders = new Gson().fromJson(jObj.toString(), token.getType());
            setOrderList(orders);
        }
    }

    private void recycleViewSetting() {
        final int VERTICAL_ITEM_SPACE = 30;
        binding.recyclerView.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(llm);

        binding.recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
    }

    class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }


}
