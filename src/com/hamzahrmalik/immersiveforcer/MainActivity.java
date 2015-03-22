package com.hamzahrmalik.immersiveforcer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdTargetingOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends ActionBarActivity implements AdListener {

	private class ListAdapter extends BaseAdapter {

		ArrayList<PInfo> data;
		LayoutInflater inflater;
		PInfo pinfo;

		public ListAdapter(ArrayList<PInfo> data) {
			this.data = data;
			inflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public PInfo getItem(int pos) {
			return data.get(pos);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		class ViewHolder {
			TextView name, pname;
			ImageView icon;
			CheckBox checkBox;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View vi = convertView;
			if (vi == null) {
				vi = inflater.inflate(R.layout.list_adapter, null);
				ViewHolder holder = new ViewHolder();
				holder.name = (TextView) vi.findViewById(R.id.adapter_name);
				holder.pname = (TextView) vi.findViewById(R.id.adapter_package);
				holder.icon = (ImageView) vi.findViewById(R.id.adapter_icon);
				holder.checkBox = (CheckBox) vi
						.findViewById(R.id.adapter_checkbox);
				holder.checkBox
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								int id = buttonView.getId();
								PInfo curr = data.get(id);
								pref.edit().putBoolean(curr.pname, isChecked)
										.apply();
							}

						});
				vi.setTag(holder);
			}

			ViewHolder holder = (ViewHolder) vi.getTag();

			pinfo = null;
			pinfo = data.get(position);
			holder.name.setText(pinfo.appname);
			holder.pname.setText(pinfo.pname);
			holder.icon.setImageDrawable(pinfo.icon);
			holder.checkBox.setId(position);
			holder.checkBox.setChecked(pref.getBoolean(pinfo.pname, false));

			return vi;
		}

	}

	ListView lv;
	ListAdapter adapter;

	Switch masterSwitch;

	SharedPreferences pref;

	class PInfo {
		private String appname = "";
		private String pname = "";
		private Drawable icon = null;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pref = getSharedPreferences("pref", Context.MODE_WORLD_READABLE);

		lv = (ListView) findViewById(R.id.appList);
		masterSwitch = (Switch) findViewById(R.id.masterswitch);
		masterSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Editor editor = pref.edit();
				editor.putBoolean("master", isChecked);
				editor.apply();
			}

		});
		masterSwitch.setChecked(pref.getBoolean("master", true));

		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Loading apps...");
		dialog.show();
		Thread load = new Thread(new Runnable() {

			@Override
			public void run() {
				final ArrayList<PInfo> list = getApps();
				MainActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						dialog.dismiss();
						adapter = new ListAdapter(list);
						lv.setAdapter(adapter);
					}

				});
			}

		});
		load.start();

		AdRegistration.setAppKey("8795e9c21c3b4edaa265c5c8cacfc43f");
		AdLayout adView = (AdLayout) findViewById(R.id.ad);
		AdTargetingOptions adOptions = new AdTargetingOptions();
		adView.loadAd(adOptions);
		adView.setListener(this);
	}

	public ArrayList<PInfo> getApps() {
		ArrayList<PInfo> list = getInstalledApps();
		lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

		Collections.sort(list, new Comparator<PInfo>() {

			@Override
			public int compare(PInfo lhs, PInfo rhs) {
				return lhs.appname.compareTo(rhs.appname);
			}
		});

		return list;
	}

	public ArrayList<PInfo> getInstalledApps() {
		ArrayList<PInfo> res = new ArrayList<PInfo>();
		List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			PInfo newInfo = new PInfo();

			newInfo.pname = p.packageName;
			newInfo.appname = p.applicationInfo.loadLabel(getPackageManager())
					.toString();
			newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());

			if (!newInfo.pname.contains("com.hamzah.holothemer"))
				res.add(newInfo);
		}
		return res;
	}

	/*
	 * public void save(View v) {
	 * 
	 * int i = 0; while (i < adapter.data.size()) { String pname =
	 * adapter.data.get(i).pname; editor.putBoolean(pname, lv.isItemChecked(i));
	 * Log.d("FE", "package " + pname + ", value: " +
	 * Boolean.toString(lv.isItemChecked(i))); i++; }
	 * 
	 * Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show(); ; finish(); }
	 */

	public void invert(View v) {
		int i = 0;
		while (i < adapter.data.size()) {
			String pname = adapter.data.get(i).pname;
			boolean on = pref.getBoolean(pname, false);
			pref.edit().putBoolean(pname, !on).apply();
			i++;
		}
		adapter.notifyDataSetChanged();
	}

	public void selectAll(View v) {
		int i = 0;
		while (i < adapter.data.size()) {
			String pname = adapter.data.get(i).pname;
			pref.edit().putBoolean(pname, true).apply();
			i++;

		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_help:
			help();
			break;
		case R.id.menu_xda:
			xda();
			break;
		case R.id.menu_settings:
			settings();
			break;
		}
		return true;
	}

	public void help() {
		Intent intent = new Intent(this, Help.class);
		startActivity(intent);
	}

	public void xda() {
		Intent browserIntent = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("http://forum.xda-developers.com/showthread.php?p=51669191"));
		startActivity(browserIntent);
	}

	public void settings() {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAdCollapsed(Ad arg0) {

	}

	@Override
	public void onAdDismissed(Ad arg0) {

	}

	@Override
	public void onAdExpanded(Ad arg0) {

	}

	@Override
	public void onAdFailedToLoad(Ad arg0, AdError arg1) {
		FrameLayout container = (FrameLayout) findViewById(R.id.adview);
		FrameLayout.LayoutParams params = (LayoutParams) findViewById(R.id.ad)
				.getLayoutParams();
		AdView admob = new AdView(this);
		admob.setLayoutParams(params);
		container.removeAllViews();
		container.addView(admob);

		AdRequest adRequest = new AdRequest.Builder().build();
		admob.setAdSize(AdSize.BANNER);

		admob.setAdUnitId("ca-app-pub-5489444403439284/5219537453");
		admob.loadAd(adRequest);
	}

	@Override
	public void onAdLoaded(Ad arg0, AdProperties arg1) {

	}
}
