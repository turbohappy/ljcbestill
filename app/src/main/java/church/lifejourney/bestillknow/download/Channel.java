package church.lifejourney.bestillknow.download;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by bdavis on 1/27/16.
 */
public class Channel {
    @ElementList(inline=true)
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }
}
