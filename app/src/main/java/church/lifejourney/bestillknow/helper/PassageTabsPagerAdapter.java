package church.lifejourney.bestillknow.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import church.lifejourney.bestillknow.download.Translation;

/**
 * Created by bdavis on 2/1/16.
 */
public class PassageTabsPagerAdapter extends FragmentPagerAdapter {

	private String urlRoot;

	public PassageTabsPagerAdapter(FragmentManager fm, String urlRoot) {
		super(fm);
		this.urlRoot = urlRoot;
	}

	@Override
	public Fragment getItem(int position) {
		String url = urlRoot + translations()[position].getCode();
		return PassageFragment.newInstance(url);
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
