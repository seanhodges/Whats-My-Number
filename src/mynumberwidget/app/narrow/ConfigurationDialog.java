package mynumberwidget.app.narrow;

import mynumberwidget.app.AbstractConfigurationDialog;
import mynumberwidget.app.R;
import mynumberwidget.app.WidgetUpdateHelper;
import android.widget.RadioButton;

public class ConfigurationDialog extends AbstractConfigurationDialog {
	
	protected int applyThemeSetting() {
		// Get the theme setting
		RadioButton themeDark = (RadioButton)findViewById(R.id.theme_dark);
		int theme = WidgetUpdateHelper.THEME_NARROW_LIGHT;
		if (themeDark.isChecked()) {
			theme = WidgetUpdateHelper.THEME_NARROW_DARK;
		}
		
		return theme;
	}
	
}
