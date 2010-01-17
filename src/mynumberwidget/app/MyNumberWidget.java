package mynumberwidget.app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;

public class MyNumberWidget extends AppWidgetProvider {

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
		
		// Get the phone number, and display it
		String phoneNumber = getNumberFromConfigFile(context);
		
		if (phoneNumber == null || phoneNumber.length() == 0) {
			// Get the phone number from the telephony manager, and display it
			// This maintains backwards compatibility with older installs 
			phoneNumber = getNumberFromTelephonyManager(context);
		}
		if (phoneNumber == null || phoneNumber.length() == 0) {
			phoneNumber = "No phone number configured";
		}
		
		views.setTextViewText(R.id.phone_number_show, phoneNumber);
		appWidgetManager.updateAppWidget(appWidgetIds, views);
	}

	private String getNumberFromConfigFile(Context context) {
		String configFile = context.getResources().getString(R.string.phone_number_config);
		SharedPreferences prefs = context.getSharedPreferences(configFile, Context.MODE_PRIVATE);
		return prefs.getString("phone_number", "");
	}

	private String getNumberFromTelephonyManager(Context context) {
		TelephonyManager info = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return info.getLine1Number();
	}
	
}