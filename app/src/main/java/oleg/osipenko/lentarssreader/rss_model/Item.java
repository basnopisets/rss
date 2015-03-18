package oleg.osipenko.lentarssreader.rss_model;

import org.simpleframework.xml.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by basnopisets on 15.03.15.
 */
@Element(name = "item")
public class Item {

    @Element
    String title;

    @Element
    String link;

    @Element(data = true)
    String description;

    @Element
    String pubDate;

    @Element(required = false)
    Enclosure enclosure;

    boolean expanded;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        if (null != this.getEnclosure()) {
            return this.getEnclosure().getUrl();
        } else {
            return "";
        }
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public String getSource() {
        if (this.link.contains("gazeta.ru")) {
            return "gazeta.ru";
        } else {
            return "lenta.ru";
        }
    }

    public Date getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.US);
        Date date = null;
        try {
            date = sdf.parse(this.pubDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String toString() {
        return "Item{" +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + getDate() + '\'' +
                ", source='" + getSource() + '\'' +
                ", expanded='" + expanded + '\'' +
                '}';
    }


}
