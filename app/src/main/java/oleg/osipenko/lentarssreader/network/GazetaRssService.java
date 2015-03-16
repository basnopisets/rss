package oleg.osipenko.lentarssreader.network;

import oleg.osipenko.lentarssreader.rss_model.Rss;
import retrofit.http.GET;
import rx.Observable;

/**
 * Created by basnopisets on 16.03.15.
 */
public interface GazetaRssService {

    @GET("/export/rss/lenta.xml")
    Observable<Rss> getGazetaItems();
}
