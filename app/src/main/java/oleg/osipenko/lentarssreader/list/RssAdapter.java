package oleg.osipenko.lentarssreader.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import oleg.osipenko.lentarssreader.rss_model.Item;

/**
 * Created by basnopisets on 17.03.15.
 */
public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder> {

    private ArrayList<Item> mValues;
    private ItemClickListener mItemClickListener;

    public RssAdapter(ItemClickListener itemClickListener) {

        mValues = new ArrayList<>();
        mItemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mValues.get(position).getDate().toString());
        holder.setOnClickListener((View v) -> mItemClickListener.itemClicked(position));
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

        @InjectView(android.R.id.text1)
        TextView mTextView;

        private final View mParent;

        public ViewHolder(View itemView) {
            super(itemView);
            mParent = itemView;
            ButterKnife.inject(this, itemView);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mParent.setOnClickListener(listener);
        }
    }

    public interface ItemClickListener {
        public void itemClicked(int position);
    }
}
