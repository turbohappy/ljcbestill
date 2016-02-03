package church.lifejourney.bestillknow.db;

import java.util.List;

import church.lifejourney.bestillknow.download.RSSItem;
import church.lifejourney.bestillknow.helper.DevotionalParser;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bdavis on 2/3/16.
 */
public class DevotionalDb {
	private Realm realm;

	public DevotionalDb() {
		this.realm = Realm.getDefaultInstance();
	}

	public List<Devotional> readDevotionals() {
		RealmResults<Devotional> results = realm.where(Devotional.class)
				.findAll();
		return results;
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

	public Devotional saveOrGetStored(RSSItem item) {
		Devotional devotional = readDevotional(item.getGuid());

		if (devotional == null) {
			devotional = parseDevotionalAndWriteToDb(item);
		}

		return devotional;
	}

	private Devotional parseDevotionalAndWriteToDb(RSSItem item) {
		Devotional devUnwritten = new DevotionalParser().parse(item);

		realm.beginTransaction();
		Devotional devWritten = realm.copyToRealm(devUnwritten);
		realm.commitTransaction();

		return devWritten;
	}
}
