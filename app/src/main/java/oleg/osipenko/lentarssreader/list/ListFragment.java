package oleg.osipenko.lentarssreader.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;

import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import oleg.osipenko.lentarssreader.R;
import oleg.osipenko.lentarssreader.network.GazetaRssService;
import oleg.osipenko.lentarssreader.network.LentaRssService;
import oleg.osipenko.lentarssreader.rss_model.Item;
import oleg.osipenko.lentarssreader.rss_model.Rss;
import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by basnopisets on 17.03.15.
 */
public class ListFragment extends Fragment implements RssAdapter.ItemClickListener {


    @InjectView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private TreeMap<Date, Item> mFeedItems;
    private RssAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedItems = new TreeMap<>(Collections.reverseOrder());
        mAdapter = new RssAdapter(this);

        RestAdapter lentaAdapter = new RestAdapter.Builder()
                .setEndpoint("http://lenta.ru")
                .setConverter(new SimpleXMLConverter(false))
                .build();
        LentaRssService lentaService = lentaAdapter.create(LentaRssService.class);

        RestAdapter gazetaAdapter = new RestAdapter.Builder()
                .setEndpoint("http://www.gazeta.ru")
                .setConverter(new SimpleXMLConverter(false))
                .build();
        GazetaRssService gazetaService = gazetaAdapter.create(GazetaRssService.class);

        Observable.merge(
                gazetaService.getGazetaItems(),
                lentaService.getLentaItems())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Rss>() {
                    @Override
                    public void onCompleted() {
                        Log.d(MainActivity.LOG_TAG, "completed " + mFeedItems.size());
                        mAdapter.putValues(mFeedItems);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(MainActivity.LOG_TAG, "some error happened " + e.getMessage());
                    }

                    @Override
                    public void onNext(Rss rss) {
                        Log.d(MainActivity.LOG_TAG, "on next");
                        Stream.of(rss.getChannel().getItem())
                                .forEach(item -> {
                                    mFeedItems.put(item.getDate(), item);
                                });
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.inject(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void itemClicked(int position) {
        Log.d(MainActivity.LOG_TAG, "clicked item " + position);
    }
}
