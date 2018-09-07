package com.infosys.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.infosys.R;
import com.infosys.databinding.ItemFeedBinding;

import java.util.ArrayList;


/**
 * Created by Tarun on 9/07/18.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {


    private final FeedFragment fragment;
    private final LayoutInflater inflator;
    private ArrayList<Feed> list;
    private ItemFeedBinding binding;

    public FeedAdapter(FeedFragment fragment) {
        this.fragment = fragment;
        inflator = LayoutInflater.from(fragment.getActivity());
    }

    public void setList(ArrayList<Feed> list) {
        this.list = list;
    }

    public ArrayList<Feed> getList() {
        return list;
    }

    public void clearList(){
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(inflator, R.layout.item_feed, parent, false);
        binding.setAdapter(this);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Feed feed = list.get(position);
        holder.binding.setFeed(feed);
        holder.binding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final ItemFeedBinding binding;

        public ViewHolder(ItemFeedBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
