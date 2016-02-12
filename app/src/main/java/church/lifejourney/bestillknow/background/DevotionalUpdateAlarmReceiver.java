package church.lifejourney.bestillknow.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.List;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.db.Devotional;
import church.lifejourney.bestillknow.db.DevotionalDb;
import church.lifejourney.bestillknow.download.LoadDevotionalTask;
import church.lifejourney.bestillknow.download.RSSItem;
import church.lifejourney.bestillknow.helper.Logger;
import church.lifejourney.bestillknow.helper.NotificationCreator;

/**
 * Created by bdavis on 2/4/16.
 */
public class DevotionalUpdateAlarmReceiver extends BroadcastReceiver implements LoadDevotionalTask
		.LoadDevotionalListener {
	private PendingResult pendingResult;
	private DevotionalDb devotionalDb;
	private Context context;
	private NotificationCreator notificationCreator;

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.debug(this, "Received broadcast [" + intent.getAction() + "]");
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			DevotionalUpdateAlarmReceiver.schedule(context);
			return;
		}

		this.devotionalDb = new DevotionalDb();
		this.context = context;
		this.notificationCreator = new NotificationCreator();
		this.pendingResult = goAsync();
		if (context.getResources().getInteger(R.integer.delete_record_on_start) == 1) {
			devotionalDb.deleteFirstXDevotionals(2);
		}
		new LoadDevotionalTask(this).execute(1); // first page
	}

	@Override
	public void devotionalsLoaded(int page, List<RSSItem> items) {
		List<Devotional> devotionalsCreated = devotionalDb.saveIfNew(items, true);

		if (devotionalsCreated.size() > 0) {
			List<Devotional> unreadDevotionals = devotionalDb.readUnreadDevotionals();
			if (unreadDevotionals.size() > 0) {
				notificationCreator.notify(context, unreadDevotionals);
			}
		}

		if (devotionalsCreated.size() >= items.size() && page < 10) {
			new LoadDevotionalTask(this).execute(page + 1); // keep going if we didn't find any we already had
		} else {
			this.devotionalDb.close();
			this.pendingResult.finish();
		}
	}

	public static void schedule(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, DevotionalUpdateAlarmReceiver.class);
		intent.setAction("bestill.intent.action.DEVOTIONAL_UPDATE");
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		long timerInterval = context.getResources().getInteger(R.integer.background_load_interval);
		alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +
				1000, timerInterval, alarmIntent);
		Logger.debug(context, "Starting devotional update alarm to run every " + timerInterval + "ms");
	}
}
