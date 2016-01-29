package church.lifejourney.bestillknow.download;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bdavis on 1/27/16.
 */
public class PubDateConverter implements Converter<Date> {
    @Override
    public Date read(InputNode node) throws Exception {
        String value = node.getValue();
        return dateFormat().parse(value);
    }

    @Override
    public void write(OutputNode node, Date value) throws Exception {
        String str = dateFormat().format(value);
        node.setValue(str);
    }

    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
    }
}
