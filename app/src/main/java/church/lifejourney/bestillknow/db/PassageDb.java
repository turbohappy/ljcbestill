package church.lifejourney.bestillknow.db;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by bdavis on 2/3/16.
 */
public class PassageDb {
	private Realm realm;

	public PassageDb() {
		this.realm = Realm.getDefaultInstance();
	}

	public void close() {
		this.realm.close();
	}

	public List<Passage> readPassages(String devotionalId) {
		RealmResults<Passage> results = realm.where(Passage.class).equalTo("devotionalId", devotionalId).findAll();
		return results;
	}

	public Passage savePassage(Passage psgUnwritten) {
		realm.beginTransaction();
		Passage psgWritten = realm.copyToRealm(psgUnwritten);
		realm.commitTransaction();
		return psgWritten;
	}
}
