package church.lifejourney.bestillknow.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import church.lifejourney.bestillknow.R;
import church.lifejourney.bestillknow.activity.DevListActivity;
import church.lifejourney.bestillknow.activity.ShowDevotionalActivity;
import church.lifejourney.bestillknow.db.Devotional;

import static android.support.v4.app.NotificationCompat.*;

/**
 * Created by bdavis on 2/9/16.
 */
public class NotificationCreator {
	private static int NOTIFICATION_ID = 1;
	private static int SINGLE_INTENT = 1;
	private static int MULTI_INTENT = 2;

	public void notify(Context context, List<Devotional> devotionals) {
		Builder builder = createBuilder(context);

		if (devotionals.size() == 1) {
			single(context, devotionals.get(0), builder);
		} else {
			multi(context, devotionals, builder);
		}

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context
				.NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, builder.build());
	}

	private Builder createBuilder(Context context) {
		return new Builder(context)
				.setSmallIcon(R.drawable.ic_notify).setColor(context.getResources().getColor(R.color.colorPrimaryDark))
				.setAutoCancel(true).setPriority(PRIORITY_HIGH).setCategory(CATEGORY_EMAIL)
				.setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE | DEFAULT_LIGHTS);
	}

	private void single(Context context, Devotional devotional, Builder builder) {
		builder.setContentTitle(devotional.getTitle()).setContentText(devotional.getCreator());
		builder.setNumber(1);

		Intent resultIntent = new Intent(context, ShowDevotionalActivity.class);
		resultIntent.putExtra("guid", devotional.getGuid());

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addNextIntent(new Intent(context, DevListActivity.class));
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(SINGLE_INTENT, PendingIntent
				.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(resultPendingIntent);
	}

	private void multi(Context context, List<Devotional> devotionals, Builder builder) {
		String title = String.format("%d new devotionals", devotionals.size());
		Set<String> creators = new HashSet<>();
		builder.setContentTitle(title);
		builder.setNumber(devotionals.size());

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addNextIntent(new Intent(context, DevListActivity.class));
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(MULTI_INTENT, PendingIntent
				.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(resultPendingIntent);

		InboxStyle inboxStyle = new InboxStyle();
		inboxStyle.setBigContentTitle(title);

		for (Devotional devotional : devotionals) {
			creators.add(devotional.getCreator());
			inboxStyle.addLine(devotional.getTitle());
		}

		String creatorsCs = TextUtils.join(", ", creators);
		builder.setContentText(creatorsCs);
		inboxStyle.setSummaryText(creatorsCs);
		builder.setStyle(inboxStyle);
	}
}
