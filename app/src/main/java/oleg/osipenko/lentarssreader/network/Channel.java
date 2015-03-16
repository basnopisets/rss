package oleg.osipenko.lentarssreader.network;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by basnopisets on 15.03.15.
 */
@Element(name = "channel")
public class Channel {

    @ElementList(inline = true)
    List<Item> item;

    public List<Item> getItem() {
        return item;
    }
}