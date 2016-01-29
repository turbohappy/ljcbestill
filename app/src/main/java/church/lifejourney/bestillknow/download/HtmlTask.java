package church.lifejourney.bestillknow.download;

import android.os.AsyncTask;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import church.lifejourney.bestillknow.helper.Logger;

/**
 * Created by bdavis on 1/27/16.
 */
public class HtmlTask extends AsyncTask<String, Void, String> {

    private WebView webView;

    public HtmlTask(WebView webView) {
        this.webView = webView;
    }

    private Exception exception;

    protected String doInBackground(String... urls) {
        try {
            String url = urls[0];
            Logger.debug(this, "Reading html from " + url);
            Document doc = Jsoup.connect(url).get();
            String output = "<!DOCTYPE html>\n<html lang=\"en-US\">";
            output += generateHead(doc);
            output += generateBody(doc);
            output += "</html>";
            return output;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(String html) {
        // TODO: check this.exception

        webView.loadData(html,"text/html","UTF-8");
    }

    private String generateBody(Document doc) {
        String output = "<body>\n";

        doc.select("div.entry-meta-bottom").remove();
        doc.select("div#comments").remove();
        doc.select("div#loop-nav-singlular-post").remove();

        Element primary = doc.select("div#primary").first();
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