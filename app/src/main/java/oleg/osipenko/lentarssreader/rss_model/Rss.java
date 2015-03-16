package oleg.osipenko.lentarssreader.rss_model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by basnopisets on 15.03.15.
 */
@Root(strict = false)
public class Rss {

    @Element(name = "channel")
    Channel channel;

    public Channel getChannel() {
        return channel;
    }
}
