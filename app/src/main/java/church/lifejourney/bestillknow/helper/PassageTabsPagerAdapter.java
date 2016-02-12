package church.lifejourney.bestillknow.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import church.lifejourney.bestillknow.db.Devotional;
import church.lifejourney.bestillknow.db.Passage;
import church.lifejourney.bestillknow.db.PassageDb;
import church.lifejourney.bestillknow.download.Translation;

/**
 * Created by bdavis on 2/1/16.
 */
public class PassageTabsPagerAdapter extends FragmentPagerAdapter {
	private PassageDb passageDb;
	private Devotional devotional;
	private List<Passage> passages;

	public PassageTabsPagerAdapter(FragmentManager fm, Devotional devotional) {
		super(fm);
		this.devotional = devotional;
		this.passageDb = new PassageDb();
		this.passages = passageDb.readPassages(devotional.getGuid());
	}

	@Override
	public Fragment getItem(int position) {
		String currTranslation = translations()[position].getCode();
		if (currTranslation != null) {
			for (Passage passage : passages) {
				if (currTranslation.equals(passage.getTranslation())) {
					return PassageFragment.newInstance(passage.getContent());
				}
			}
			//guess we didn't have it already, create fragment capable of getting it
			return PassageFragment.newInstance(devotional.getGuid(), devotional.getLinkStub(), currTranslation);
		} else {
			Logger.error(this, "No translation specified", null);
			return PassageFragment.newInstance("");
		}
	}

	@Override
	public int getCount() {
		return translations().length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return translations()[position].getName();
	}

	private Translation[] translations() {
		return Translation.values();
	}
}
