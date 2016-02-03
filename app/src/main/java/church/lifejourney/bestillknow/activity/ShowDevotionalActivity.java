package church.lifejourney.bestillknow.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.db.Devotional;
import church.lifejourney.bestillknow.db.DevotionalDb;
import church.lifejourney.bestillknow.download.URLImageGetter;
import church.lifejourney.bestillknow.helper.DevotionalLinkMethod;
import church.lifejourney.bestillknow.helper.DevotionalTagHandler;
import church.lifejourney.bestillknow.helper.PassageTabsPagerAdapter;

public class ShowDevotionalActivity extends AppCompatActivity {
	private DevotionalDb devotionalDb;

	public ShowDevotionalActivity() {
		devotionalDb = new DevotionalDb();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_devotional);

		Devotional dev = devotionalDb.readDevotional(getIntent().getStringExtra("guid"));

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(dev.getTitle());
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (dev.getIntro() != null) {
			setupIntroSection(dev.getIntro(), findViewById(R.id.dev_intro_view));
		} else {
			findViewById(R.id.dev_intro_view).setVisibility(View.GONE);
		}
		if (dev.getLinkStub() != null) {
			setupPassageSection(dev, findViewById(R.id.dev_passage_view));
		} else {
			findViewById(R.id.dev_passage_view).setVisibility(View.GONE);
		}
		setupContentSection(dev.getContent(), findViewById(R.id.dev_content_view));
	}

	private void setupIntroSection(String html, View view) {
		setContent(html, (TextView) view);
	}

	private void setupPassageSection(Devotional dev, View view) {
		TextView verses = (TextView) view.findViewById(R.id.dev_passage_verses);
		verses.setText(dev.getPassages());
		TabLayout tabs = (TabLayout) view.findViewById(R.id.dev_passage_tab);
		ViewPager pager = (ViewPager) view.findViewById(R.id.dev_passage_pager);
		PassageTabsPagerAdapter adapter = new PassageTabsPagerAdapter(getSupportFragmentManager(), dev.getLinkStub());
		pager.setAdapter(adapter);
		tabs.setupWithViewPager(pager);
	}

	private void setupContentSection(String html, View view) {
		setContent(html, (TextView) view);
	}

	private void setContent(String html, TextView target) {
		target.setText(Html.fromHtml(html, new URLImageGetter(target, this), new DevotionalTagHandler()));
		target.setMovementMethod(new DevotionalLinkMethod(this));
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
