package com.hamzahrmalik.immersiveforcer;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookLoadPackage {

	XSharedPreferences pref;

	@Override
	public void handleLoadPackage(final LoadPackageParam lpparam)
			throws Throwable {
		pref = new XSharedPreferences(Main.class.getPackage().getName(), "pref");
		XposedHelpers.findAndHookMethod(Activity.class, "onResume",
				new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {
						Activity activity = (Activity) param.thisObject;
						pref.reload();
						if (pref.getBoolean(lpparam.packageName, false)
								&& pref.getBoolean("master", true)) {
							Window window = activity.getWindow();
							window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
							View decorView = window.getDecorView();
							if (Build.VERSION.SDK_INT >= 19) {
								decorView
										.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
												| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
												| View.SYSTEM_UI_FLAG_IMMERSIVE);
							}
						} else {
							activity.setImmersive(false);
							Window window = activity.getWindow();

							View decorView = window.getDecorView();
							if (Build.VERSION.SDK_INT >= 19) {
								decorView
										.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
							}
						}
					}
				});
	}
}
