package church.lifejourney.bestillknow.rss;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;

import java.util.Date;

/**
 * Created by bdavis on 1/27/16.
 */
public class Item {
    @Element(required=false)
    private String title;

    @Element(required=false)
    private String link;

//    @Element(required=false)
//    private Date pubDate;

    @Element(required=false)
    private String description;

    @Namespace(prefix = "content")
    @Element(name = "encoded")
    private String content;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

//    public Date getPubDate() {
//        return pubDate;
//    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }
}
