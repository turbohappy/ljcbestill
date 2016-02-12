package church.lifejourney.bestillknow.activity;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import church.lifejourney.bestillknow.BuildConfig;
import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.db.Migration;
import church.lifejourney.bestillknow.helper.Logger;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by bdavis on 2/7/16.
 */
public class Application extends android.app.Application {
	@Override
	public void onCreate() {
		super.onCreate();
		initializeCrashlytics();
		initializeRealm();
	}

	private void initializeCrashlytics() {
		Crashlytics crashlyticsKit = new Crashlytics.Builder()
				.core(new CrashlyticsCore.Builder().disabled(BuildConfig
						.DEBUG).build())
				.build();
		Fabric.with(this, crashlyticsKit);
	}

	private void initializeRealm() {
		RealmConfiguration realmConfiguration = Application.realmConfig(this);
		Realm.setDefaultConfiguration(realmConfiguration);
		Logger.debug(this, "Application created, realm at [" + realmConfiguration.getPath() + "]");
		if (getResources().getInteger(R.integer.clear_db_on_start) == 1) {
			Logger.debug(this, "Cleared database!!!!!!!!!!!!");
			Realm.deleteRealm(realmConfiguration);
		}
	}

	public static RealmConfiguration realmConfig(Context context) {
		return new RealmConfiguration.Builder(context).name(RealmConfiguration
				.DEFAULT_REALM_NAME).schemaVersion(1).migration(new Migration()).build();
	}
}
