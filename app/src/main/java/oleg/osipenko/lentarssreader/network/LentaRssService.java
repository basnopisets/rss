package oleg.osipenko.lentarssreader.network;

import oleg.osipenko.lentarssreader.rss_model.Rss;
import retrofit.http.GET;
import rx.Observable;

/**
 * Created by basnopisets on 15.03.15.
 */
public interface LentaRssService {

    @GET("/rss")
    Observable<Rss> getLentaItems();
}
