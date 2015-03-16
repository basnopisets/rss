package oleg.osipenko.lentarssreader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.annimon.stream.Stream;

import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import oleg.osipenko.lentarssreader.network.GazetaRssService;
import oleg.osipenko.lentarssreader.rss_model.Item;
import oleg.osipenko.lentarssreader.network.LentaRssService;
import oleg.osipenko.lentarssreader.rss_model.Rss;
import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity{

    private static final String LOG_TAG = "rss-reader";
    private CountDownLatch mLatch;
    private TreeMap<Date, Item> mFeedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFeedItems = new TreeMap<>();
        mLatch = new CountDownLatch(1);

        RestAdapter lentaAdapter = new RestAdapter.Builder()
                .setEndpoint("http://lenta.ru")
                .setConverter(new SimpleXMLConverter(false))
                //.setLogLevel(RestAdapter.LogLevel.FULL)
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
                        Log.d(LOG_TAG, "completed");
                        mLatch.countDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "some error happened " + e.getMessage());
                    }

                    @Override
                    public void onNext(Rss rss) {
                        Log.d(LOG_TAG, "on next");
                        Stream.of(rss.getChannel().getItem())
                                .forEach(item -> {
                                    Log.d(LOG_TAG, item.toString());
                                    mFeedItems.put(item.getDate(), item);
                                });
                    }
                });

        new Thread(() -> {
            try {
                mLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Stream.of(mFeedItems.values())
                        .forEach(a -> Log.d(LOG_TAG, a.getSource() + " " + a.getDate().toString()));
            }
        }).start();
    }
}
