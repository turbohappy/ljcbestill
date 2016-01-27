package church.lifejourney.bestillknow.rss;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.convert.Convert;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by bdavis on 1/27/16.
 */
public class Item {
    @Element(required=false)
    private String title;

    @Element(required=false)
    private String link;

    @Element(required=false)
    @Convert(PubDateConverter.class)
    private Date pubDate;

    @Namespace(prefix = "dc")
    @Element(required=false)
    private String creator;

//    @Element(required=false)
//    private String category;
    //TODO: causing exception which seems odd

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

    public Date getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public String getCreator() {
        return creator;
    }

//    public String getCategory() {
//        return category;
//    }
}
