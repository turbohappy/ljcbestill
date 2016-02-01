package church.lifejourney.bestillknow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.helper.DevotionalContentParser;
import church.lifejourney.bestillknow.helper.DevotionalImageGetter;
import church.lifejourney.bestillknow.helper.DevotionalLinkMethod;
import church.lifejourney.bestillknow.helper.DevotionalTagHandler;

public class ShowDevotionalActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_devotional);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DevotionalContentParser.Sections sections = new DevotionalContentParser().splitContent(getIntent().getStringExtra("content"));
		setupIntroSection(sections.before, findViewById(R.id.dev_intro_view));
		setupPassageSection(sections.scripture, findViewById(R.id.dev_passage_view));
		setupContentSection(sections.after, findViewById(R.id.dev_content_view));
	}

	private void setupIntroSection(String html, View view) {
		setContent(html, (TextView) view);
	}

	private void setupPassageSection(String html, View view) {
		String modifiedHtml = fixPassageLinks(html);
		setContent(modifiedHtml, (TextView) view);
	}

	private void setupContentSection(String html, View view) {
		setContent(html, (TextView) view);
	}

	private void setContent(String html, TextView target) {
		target.setText(Html.fromHtml(html, new DevotionalImageGetter(), new DevotionalTagHandler()));
		target.setMovementMethod(new DevotionalLinkMethod(this));
	}

	private String fixPassageLinks(String content) {

		Matcher matcher = Pattern.compile("(<a href=\"http://www.biblegateway.com/passage/\\?search=[^&]+&amp;version=)").matcher(content);
		if (matcher.find()) {
			String bgLink = matcher.group();
			String nrsvLink = bgLink + "NRSV";
			Matcher matcher2 = Pattern.compile("<a href=\"http://bible.oremus.org/\\?ql=[0-9]+").matcher(content);
			if (matcher2.find()) {
				content = matcher2.replaceAll(nrsvLink);
			}
		}
		return content;
	}
}
