package oleg.osipenko.lentarssreader.network;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by basnopisets on 16.03.15.
 */
public interface GazetaRssService {

    @GET("/export/rss/lenta.xml")
    void getGazetaItems(Callback<Rss> cb);
}
