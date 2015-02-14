package com.hamzahrmalik.immersiveforcer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

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

public class Help extends ActionBarActivity implements AdListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		AdRegistration.setAppKey("8795e9c21c3b4edaa265c5c8cacfc43f");
		AdLayout adView = (AdLayout) findViewById(R.id.ad);
		AdTargetingOptions adOptions = new AdTargetingOptions();
		adView.loadAd(adOptions);
		adView.setListener(this);
	}

	public void xda(View v) {
		Intent browserIntent = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("http://forum.xda-developers.com/showthread.php?p=51669191"));
		startActivity(browserIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.help, menu);
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
