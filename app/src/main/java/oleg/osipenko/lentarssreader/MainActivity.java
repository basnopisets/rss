package oleg.osipenko.lentarssreader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.annimon.stream.Stream;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import oleg.osipenko.lentarssreader.network.GazetaRssService;
import oleg.osipenko.lentarssreader.network.Item;
import oleg.osipenko.lentarssreader.network.LentaRssService;
import oleg.osipenko.lentarssreader.network.Rss;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CountDownLatch latch = new CountDownLatch(2);

        TreeMap<Date, Item> feedItems = new TreeMap<>();

        RestAdapter lentaAdapter = new RestAdapter.Builder()
                .setEndpoint("http://lenta.ru")
                .setConverter(new SimpleXMLConverter(false))
                //.setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        LentaRssService lentaService = lentaAdapter.create(LentaRssService.class);
        lentaService.getLentaItems(new Callback<Rss>() {
            @Override
            public void success(Rss rssResponse, Response response) {
                List<Item> items = rssResponse.getChannel().getItem();
                for (Item item : items) {

                    feedItems.put(item.getDate(), item);
                }
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("aaaa", error.getMessage() + " " + error.getUrl() + " " + error.getResponse
                        ().getReason());
            }
        });

        RestAdapter gazetaAdapter = new RestAdapter.Builder()
                .setEndpoint("http://www.gazeta.ru")
                .setConverter(new SimpleXMLConverter(false))
                .build();
        GazetaRssService gazetaService = gazetaAdapter.create(GazetaRssService.class);
        gazetaService.getGazetaItems(new Callback<Rss>() {
            @Override
            public void success(Rss rssResponse, Response response) {
                List<Item> items = rssResponse.getChannel().getItem();
                for (Item item : items) {
                    feedItems.put(item.getDate(), item);
                }
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("aaaa", error.getMessage() + " " + error.getUrl() + " " + error.getResponse
                        ().getReason());
            }
        });

        new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Stream.of(feedItems.values())
                        .forEach(a -> Log.d("aaaa", a.getSource() + " " + a.getDate().toString()));
            }
        }).start();
    }

}
