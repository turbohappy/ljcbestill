package church.lifejourney.bestillknow.db;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by bdavis on 2/7/16.
 */
public class Migration implements RealmMigration {
	@Override
	public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
		RealmSchema schema = realm.getSchema();

		if (oldVersion < 1) {
			schema.get("Devotional").addField("unread", boolean.class);
		}
	}
}
