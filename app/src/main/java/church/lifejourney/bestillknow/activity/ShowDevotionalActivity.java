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
import church.lifejourney.bestillknow.helper.DevotionalContentParser;
import church.lifejourney.bestillknow.helper.DevotionalImageGetter;
import church.lifejourney.bestillknow.helper.DevotionalLinkMethod;
import church.lifejourney.bestillknow.helper.DevotionalTagHandler;
import church.lifejourney.bestillknow.helper.PassageTabsPagerAdapter;

public class ShowDevotionalActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_devotional);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(getIntent().getStringExtra("title"));
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		DevotionalContentParser.Sections sections = new DevotionalContentParser().splitContent(getIntent().getStringExtra("content"));
		if (sections.intro != null) {
			setupIntroSection(sections.intro, findViewById(R.id.dev_intro_view));
			setupPassageSection(sections, findViewById(R.id.dev_passage_view));
		} else {
			findViewById(R.id.dev_intro_view).setVisibility(View.GONE);
			findViewById(R.id.dev_passage_view).setVisibility(View.GONE);
		}
		setupContentSection(sections.content, findViewById(R.id.dev_content_view));
	}

	private void setupIntroSection(String html, View view) {
		setContent(html, (TextView) view);
	}

	private void setupPassageSection(DevotionalContentParser.Sections sections, View view) {
		TabLayout tabs = (TabLayout) view.findViewById(R.id.dev_passage_tab);
		ViewPager pager = (ViewPager) view.findViewById(R.id.dev_passage_pager);
		PassageTabsPagerAdapter adapter = new PassageTabsPagerAdapter(getSupportFragmentManager(), sections.linkStub);
		pager.setAdapter(adapter);
		tabs.setupWithViewPager(pager);
	}

	private void setupContentSection(String html, View view) {
		setContent(html, (TextView) view);
	}

	private void setContent(String html, TextView target) {
		target.setText(Html.fromHtml(html, new DevotionalImageGetter(), new DevotionalTagHandler()));
		target.setMovementMethod(new DevotionalLinkMethod(this));
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
