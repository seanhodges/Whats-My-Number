package mynumberwidget.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public final class WidgetUpdateHelper {
	
	public static int THEME_LIGHT = 0;
	public static int THEME_DARK = 1;
	
	public static void updateWidget(Context context, String phoneNumber, int theme) {
		
		// Set the theme - this is a bit clunky as appwidgets are restricted to swapping out the whole layout
		// instead of tweaking existing ones, but it works
		int appWidgetLayout = R.layout.my_number_widget_light;
		if (THEME_DARK == theme) {
			appWidgetLayout = R.layout.my_number_widget_dark;
		}
		
		// Redraw the widget now
		ComponentName me = new ComponentName(context, MyNumberWidget.class);
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		RemoteViews views = new RemoteViews(context.getPackageName(), appWidgetLayout);
		
		// Attach a click listener to perform copy-to-clipboard
		Intent action = new Intent(context.getApplicationContext(), CopyToClipboard.class);
		action.putExtra("PHONE_NUMBER", phoneNumber);
		PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, action, PendingIntent.FLAG_CANCEL_CURRENT);
		views.setOnClickPendingIntent(R.id.container, pendingIntent);
		Log.v(context.getClass().getName(), "Pending intent set, phone number = " + phoneNumber);
		
		// Set the phone number text
		views.setTextViewText(R.id.phone_number_show, phoneNumber);
		
		mgr.updateAppWidget(me, views);
	}
}
