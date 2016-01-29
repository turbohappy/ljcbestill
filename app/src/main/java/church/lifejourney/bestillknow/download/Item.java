package church.lifejourney.bestillknow.download;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.convert.Convert;

import java.util.Date;

/**
 * Created by bdavis on 1/27/16.
 */
public class Item {
    @Element(required=false)
    protected String title;

    @Element(required=false)
    protected String link;

    @Element(required=false)
    @Convert(PubDateConverter.class)
    protected Date pubDate;

    @Namespace(prefix = "dc")
    @Element(required=false)
    protected String creator;

//    @Element(required=false)
//    private String category;
    //TODO: causing exception which seems odd

    @Element(required=false)
    protected String description;

    @Namespace(prefix = "content")
    @Element(name = "encoded")
    protected String content;

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