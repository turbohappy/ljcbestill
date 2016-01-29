package church.lifejourney.bestillknow.download;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by bdavis on 1/27/16.
 */
@Root
public class RSS {
    @Element
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }
}
