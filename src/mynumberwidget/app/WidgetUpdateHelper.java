package mynumberwidget.app;

import mynumberwidget.app.action.CopyToClipboard;
import mynumberwidget.app.action.DisplayPhoneNumber;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public final class WidgetUpdateHelper {
	
	public static int THEME_WIDE_LIGHT = R.layout.my_number_widget_wide_light;
	public static int THEME_WIDE_DARK = R.layout.my_number_widget_wide_dark;
	public static int THEME_NARROW_LIGHT = R.layout.my_number_widget_narrow_light;
	public static int THEME_NARROW_DARK = R.layout.my_number_widget_narrow_dark;
	
	public static void updateWidget(Context context, String phoneNumber, int theme) {
		// Redraw the widget now
		RemoteViews views = new RemoteViews(context.getPackageName(), theme);
		
		if (theme == THEME_NARROW_LIGHT || theme == THEME_NARROW_DARK) {
			doNarrowWidgetUpdate(context, phoneNumber, views);
		}
		else {
			doWideWidgetUpdate(context, phoneNumber, views);
		}
	}

	private static void doNarrowWidgetUpdate(Context context, String phoneNumber, RemoteViews views) {
		// Attach a click listener to display the phone number
		Intent action = new Intent(context.getApplicationContext(), DisplayPhoneNumber.class);
		action.putExtra("PHONE_NUMBER", phoneNumber);
		PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, action, PendingIntent.FLAG_CANCEL_CURRENT);
		views.setOnClickPendingIntent(R.id.container_narrow, pendingIntent);
		Log.v(context.getClass().getName(), "DisplayPhoneNumber intent set, phone number = " + phoneNumber);
		
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		ComponentName me = new ComponentName(context, mynumberwidget.app.narrow.MyNumberWidget.class);
		mgr.updateAppWidget(me, views);
	}

	private static void doWideWidgetUpdate(Context context, String phoneNumber, RemoteViews views) {
		// Attach a click listener to perform copy-to-clipboard
		Intent action = new Intent(context.getApplicationContext(), CopyToClipboard.class);
		action.putExtra("PHONE_NUMBER", phoneNumber);
		PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, action, PendingIntent.FLAG_CANCEL_CURRENT);
		views.setOnClickPendingIntent(R.id.container_wide, pendingIntent);
		Log.v(context.getClass().getName(), "CopyToClipboard intent set, phone number = " + phoneNumber);
		
		// Display the phone number on a wide widget
		views.setTextViewText(R.id.phone_number_show, phoneNumber);
		
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		ComponentName me = new ComponentName(context, mynumberwidget.app.wide.MyNumberWidget.class);
		mgr.updateAppWidget(me, views);
	}
}
