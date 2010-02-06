package mynumberwidget.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class CopyToClipboard extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Perform copy-to-clipboard, using phone number in extras
		String phoneNumber = getIntent().getExtras().getString("PHONE_NUMBER");
		ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		clipboard.setText(phoneNumber);
		Log.v(getClass().getName(), "Clipboard copy complete, phone number = " + phoneNumber);
		
		// Close activity immediately
		Toast toast = Toast.makeText(getApplicationContext(), "Phone number copied to clipboard", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		finish();
	}
}