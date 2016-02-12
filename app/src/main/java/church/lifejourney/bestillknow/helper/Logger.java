package church.lifejourney.bestillknow.helper;

import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

/**
 * Created by bdavis on 1/28/16.
 */
public class Logger {
	public static void debug(Object from, String message) {
		Log.d(tag(from), message);
	}

	public static void error(Object from, String message, Throwable throwable) {
		Log.e(tag(from), message, throwable);
		Crashlytics.logException(throwable);
	}

	private static String tag(Object from) {
		if (from == null) {
			return "null";
		}
		if (from instanceof String) {
			return (String) from;
		}
		String simpleName = from.getClass().getSimpleName();
		if (TextUtils.isEmpty(simpleName)) {
			return from.getClass().getName();
		} else {
			return simpleName;
		}
	}
}
