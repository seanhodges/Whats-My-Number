package mynumberwidget.app;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
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
import android.widget.RadioButton;
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
				saveConfig();
				break;
		}
	}
	
	private void saveConfig() {
		
		// Get the phone number setting
		EditText phoneNumberBox = (EditText)findViewById(R.id.phone_number_edit);
		String phoneNumber = phoneNumberBox.getText().toString();
		
		// Get the theme setting
		RadioButton themeDark = (RadioButton)findViewById(R.id.theme_dark);
		int theme = WidgetUpdateHelper.THEME_LIGHT;
		if (themeDark.isChecked()) {
			theme = WidgetUpdateHelper.THEME_DARK;
		}
		
		// Save the phone number to the preferences
		Editor editor = prefs.edit();
		editor.putString("phone_number", phoneNumber);
		editor.putInt("theme", theme);
		editor.commit();
		
		WidgetUpdateHelper.updateWidget(this, phoneNumber, theme);
		
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
}
