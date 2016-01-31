package church.lifejourney.bestillknow.download;

import android.os.AsyncTask;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import church.lifejourney.bestillknow.helper.Logger;

/**
 * Created by bdavis on 1/27/16.
 */
public class HtmlTask extends AsyncTask<String, Void, String> {

    private TextView contentView;

    public HtmlTask(TextView contentView) {
        this.contentView = contentView;
    }

    private Exception exception;

    protected String doInBackground(String... urls) {
        try {
            String url = urls[0];
            Logger.debug(this, "Reading html from " + url);
            Document doc = Jsoup.connect(url).get();
            return generateBody(doc);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(String html) {
        // TODO: check this.exception

        contentView.setText(Html.fromHtml(html));
        contentView.setMovementMethod(new ScrollingMovementMethod());
    }

    private String generateBody(Document doc) {
        String output = "<body>\n";

        Element primary = doc.select("div.entry-content").first();
        primary.select("p.no-break").remove();
        output += primary.outerHtml();
        output += "\n</body>";
        return output;
    }

    private String generateHead(Document doc) {
        String output = "<head>\n<meta charset=\"UTF-8\">\n";

        for (Element stylesheet : doc.select("link[rel=\"stylesheet\"]")) {
            output += stylesheet.outerHtml() + "\n";
        }
        for (Element stylesheet : doc.select("style")) {
            output += stylesheet.outerHtml() + "\n";
        }

        output += "<style>body { min-width: 0px !important; }</style>";

        output += "\n</head>";
        return output;
    }
}