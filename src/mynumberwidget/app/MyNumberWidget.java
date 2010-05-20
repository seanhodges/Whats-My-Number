package mynumberwidget.app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;

public class MyNumberWidget extends AppWidgetProvider {

	protected class AppWidgetData {
		String phoneNumber;
		int theme;
	}
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		AppWidgetData data = getDataFromConfigFile(context);
		WidgetUpdateHelper.updateWidget(context, data.phoneNumber, data.theme);
	}

	private AppWidgetData getDataFromConfigFile(Context context) {
		String configFile = context.getResources().getString(R.string.phone_number_config);
		SharedPreferences prefs = context.getSharedPreferences(configFile, Context.MODE_PRIVATE);
		
		AppWidgetData data = new AppWidgetData();
		data.phoneNumber = prefs.getString(ConfigurationDialog.PHONE_NUMBER_PREF_KEY, "");
		data.theme = prefs.getInt(ConfigurationDialog.THEME_PREF_KEY, WidgetUpdateHelper.THEME_LIGHT);
		return data;
	}
	
}