package com.hamzahrmalik.immersiveforcer;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class Settings extends ActionBarActivity {

	CheckBox showToggleActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		showToggleActivity = (CheckBox) findViewById(R.id.show_toggle_activity);
	}

	public void apply(View v) {
		PackageManager pm = getPackageManager();
		if (showToggleActivity.isChecked())
			pm.setComponentEnabledSetting(new ComponentName(this,
					Force_Immersive_Toggle.class),
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
		else
			pm.setComponentEnabledSetting(new ComponentName(this,
					Force_Immersive_Toggle.class),
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);
		Toast.makeText(this, "Settings applied", Toast.LENGTH_SHORT).show();
		finish();
	}
}
