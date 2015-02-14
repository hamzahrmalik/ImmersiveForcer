package com.hamzahrmalik.immersiveforcer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;

public class Force_Immersive_Toggle extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_force__immersive__toggle);

		@SuppressWarnings("deprecation")
		SharedPreferences pref = getSharedPreferences("pref",
				Context.MODE_WORLD_READABLE);
		Editor editor = pref.edit();
		editor.putBoolean("master", !pref.getBoolean("master", false));
		editor.apply();
		Toast.makeText(
				this,
				(pref.getBoolean("master", false) ? "Enabled Force Immersive"
						: "Disabled Force Immersive"), Toast.LENGTH_SHORT)
				.show();
		finish();
	}
}
