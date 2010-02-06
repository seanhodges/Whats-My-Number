package mynumberwidget.app;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ConfigurationDialog extends Activity implements OnClickListener {
	
	private SharedPreferences prefs;
	private static boolean firstTime = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.configuration_dialog);
		
		String configFile = getResources().getString(R.string.phone_number_config);
		prefs = getSharedPreferences(configFile, MODE_PRIVATE);
		
		populatePhoneNumberBox();
			
		Button saveButton = (Button)findViewById(R.id.config_save_button);
		saveButton.setOnClickListener(this);
	}

	private void populatePhoneNumberBox() {
		EditText phoneNumberBox = (EditText)findViewById(R.id.phone_number_edit);
		
		if (prefs.getAll().size() == 0) {
			
			// Get the phone number from the telephony manager, and display it
			TelephonyManager info = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
			String phoneNumber = info.getLine1Number();
			
			if (phoneNumber != null && phoneNumber.length() > 0) {
				phoneNumberBox.setText(phoneNumber);
			}
			else {
				// Phone number not found on SIM card
				if (firstTime) {
					Toast toast = Toast.makeText(this, R.string.no_phone_number, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
				}
			}
		}
		else {
			// Use last saved phone number in preferences
			String phoneNumber = prefs.getString("phone_number", "");
			phoneNumberBox.setText(phoneNumber);
		}
		
		firstTime = false;
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.config_save_button:
				savePhoneNumber();
				break;
		}
	}
	
	private void savePhoneNumber() {
		
		// Save the phone number to the preferences
		EditText phoneNumberBox = (EditText)findViewById(R.id.phone_number_edit);
		String phoneNumber = phoneNumberBox.getText().toString();
		Editor editor = prefs.edit();
		editor.putString("phone_number", phoneNumber);
		editor.commit();
		
		updateWidget(phoneNumber);
		
		// Return success to the appwidget
		int appWidgetId = -1;
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
		    appWidgetId = extras.getInt(
		            AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			setResult(RESULT_OK, resultValue);
		}
		finish();
	}

	private void updateWidget(String phoneNumber) {
		
		// Redraw the widget now
		ComponentName me = new ComponentName(this, MyNumberWidget.class);
		AppWidgetManager mgr = AppWidgetManager.getInstance(this);
		RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.main);
		views.setTextViewText(R.id.phone_number_show, phoneNumber);
		mgr.updateAppWidget(me, views);
		
		if (mgr.getAppWidgetIds(me).length == 0) {
			// Instruct the user how to add the widget, if one has not been placed already
			Toast toast = Toast.makeText(this, R.string.add_widget_instruction, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
}
