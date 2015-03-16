package oleg.osipenko.lentarssreader.network;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Created by basnopisets on 16.03.15.
 */
@Element
public class Enclosure {

    @Attribute
    String url;

    public String getUrl() {
        return url;
    }
}
