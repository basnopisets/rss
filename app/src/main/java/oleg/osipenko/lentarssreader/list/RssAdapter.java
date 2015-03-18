package oleg.osipenko.lentarssreader.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import oleg.osipenko.lentarssreader.R;
import oleg.osipenko.lentarssreader.rss_model.Item;

/**
 * Created by basnopisets on 17.03.15.
 */
public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder> {

    private ArrayList<Item> mValues;

    public RssAdapter() {
        mValues = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mValues.get(position);
        holder.mTvTitle.setText(item.getTitle());
        holder.mTvDescription.setText(item.getDescription());
        if (!TextUtils.isEmpty(item.getImageUrl())) {
            holder.mIvFeed.setVisibility(View.VISIBLE);
            Picasso.with(holder.getContext())
                    .load(item.getImageUrl())
                    .into(holder.mIvFeed);
        } else {
            holder.mIvFeed.setImageResource(0);
            holder.mIvFeed.setVisibility(View.GONE);
        }
        holder.mTvSource.setText(item.getSource());
        if (item.isExpanded()) {
            holder.mTvDescription.setVisibility(View.VISIBLE);
        } else {
            holder.mTvDescription.setVisibility(View.GONE);
        }
        holder.getParent().setOnClickListener((View v) -> {
            if (item.isExpanded()) {
                item.setExpanded(false);
            } else {
                item.setExpanded(true);
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void putValues(TreeMap<Date, Item> items) {
        mValues.addAll(items.values());
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_feed)
        ImageView mIvFeed;
        @InjectView(R.id.tv_title)
        TextView mTvTitle;
        @InjectView(R.id.tv_description)
        TextView mTvDescription;
        @InjectView(R.id.tv_source)
        TextView mTvSource;

        private final View mParent;

        public ViewHolder(View itemView) {
            super(itemView);
            mParent = itemView;
            ButterKnife.inject(this, itemView);
        }

        public Context getContext() {
            return mParent.getContext();
        }

        public View getParent() {
            return mParent;
        }
    }
}
