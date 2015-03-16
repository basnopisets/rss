package oleg.osipenko.lentarssreader.network;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by basnopisets on 15.03.15.
 */
public interface LentaRssService {

    @GET("/rss")
    void getLentaItems(Callback<Rss> cb);
}
