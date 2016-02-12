package church.lifejourney.bestillknow.db;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import church.lifejourney.bestillknow.activity.Application;
import church.lifejourney.bestillknow.download.RSSItem;
import church.lifejourney.bestillknow.helper.DevotionalParser;
import church.lifejourney.bestillknow.helper.Logger;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by bdavis on 2/3/16.
 */
public class DevotionalDb {
	private Realm realm;

	public DevotionalDb() {
		this.realm = Realm.getDefaultInstance();
	}

	public DevotionalDb(Context context) {
		this.realm = Realm.getInstance(Application.realmConfig(context));
	}

	public void addChangeListener(RealmChangeListener listener) {
		realm.addChangeListener(listener);
	}

	public void close() {
		this.realm.close();
	}

	public List<Devotional> readDevotionals() {
		return realm.where(Devotional.class).findAllSorted("pubDate", Sort.DESCENDING);
	}

	public List<Devotional> readUnreadDevotionals() {
		return realm.where(Devotional.class).equalTo("unread", true).findAll();
	}

	public Devotional readDevotional(String guid) {
		RealmResults<Devotional> results = realm.where(Devotional.class)
				.equalTo("guid", guid).findAll();
		if (results == null || results.size() == 0) {
			return null;
		} else if (results.size() == 1) {
			return results.get(0);
		} else {
			throw new RuntimeException("There should never be multiple " +
					"entries with the same guid");
		}
	}

	public void markDevotionalRead(Devotional devotional) {
		realm.beginTransaction();
		devotional.setUnread(false);
		realm.commitTransaction();
	}

	@NonNull
	public List<Devotional> saveIfNew(List<RSSItem> items, boolean markUnread) {
		List<Devotional> written = new ArrayList<>();

		realm.beginTransaction();

		for (RSSItem item : items) {
			Devotional devotional = readDevotional(item.getGuid());

			if (devotional == null) {
				written.add(parseDevotionalAndWriteToDbWithoutCommitting(item, markUnread));
			}
		}

		if (written.size() > 0) {
			realm.commitTransaction();
		} else {
			realm.cancelTransaction();
		}

		return written;
	}

	private Devotional parseDevotionalAndWriteToDbWithoutCommitting(RSSItem item, boolean markUnread) {
		Devotional devUnwritten = new DevotionalParser().parse(item);
		devUnwritten.setUnread(markUnread);

		return realm.copyToRealm(devUnwritten);
	}

	public void deleteFirstXDevotionals(int number) {
		realm.beginTransaction();

		RealmResults<Devotional> devotionals = realm.where(Devotional.class).findAllSorted("pubDate", Sort.DESCENDING);
		for (int i = 0; i < number; i++) {
			devotionals.first().removeFromRealm();
		}

		realm.commitTransaction();
		Logger.debug(this, String.format("Removed first %s devotionals", number));
	}
}
