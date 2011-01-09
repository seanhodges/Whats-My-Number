package mynumberwidget.app.action;

import mynumberwidget.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class DisplayPhoneNumber extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(getClass().getName(), "Displaying phone number");
		
		// Show the phone number using a toast
		String phoneNumber = getIntent().getExtras().getString("PHONE_NUMBER");
		String message = getText(R.string.display_phone_number).toString().replaceAll("\\{PHONE_NUMBER\\}", phoneNumber);
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		
		// Close activity immediately
		finish();
	}
}
