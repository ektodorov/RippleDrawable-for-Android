package com.blogspot.techzealous.rippledrawablecomp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button mButtonSettings;
	private TextView mTextView;
	private Button mButton2;
	private RelativeLayout mRelativeLayout;
	private Button mButton3;
	
	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mButtonSettings = (Button)findViewById(R.id.buttonSettingsMain);
		mTextView = (TextView)findViewById(R.id.textView1);
		mButton2 = (Button)findViewById(R.id.button2);
		mRelativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout1);
		mButton3 = (Button)findViewById(R.id.button3);

		ActionBar actionBar = getActionBar();
		if(actionBar != null) {
			actionBar.hide();
		}
		mPrefs = getSharedPreferences(ConstantsRD.PREFS_FILENAME, Context.MODE_PRIVATE);
		
		mButtonSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(i);
			}
		});
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		setup();
	}
	
	private void setup()
	{
		int color = mPrefs.getInt(ConstantsRD.PREF_COLOR, Color.BLACK);
		int alpha = mPrefs.getInt(ConstantsRD.PREF_ALPHA, 150);
		int radius = mPrefs.getInt(ConstantsRD.PREF_RADIUS, 20);
		int duration = mPrefs.getInt(ConstantsRD.PREF_DURATION, 1000);
		boolean isUseGradient = mPrefs.getBoolean(ConstantsRD.PREF_IS_USE_GRADIENT, false);
		boolean isUseFadeOut = mPrefs.getBoolean(ConstantsRD.PREF_IS_USE_FADEOUT, false);

		RippleDrawableComp rippleTextView = new RippleDrawableComp(color, alpha, radius, duration,
				getResources().getDrawable(R.drawable.ic_launcher), mTextView, isUseGradient, isUseFadeOut);
		rippleTextView.init();
		mTextView.setBackgroundDrawable(rippleTextView);

		RippleDrawableComp rippleButton2 = new RippleDrawableComp(color, alpha, radius, duration, 
				getResources().getDrawable(R.drawable.xml_rect_green), mButton2, isUseGradient, isUseFadeOut);
		rippleButton2.init();
		mButton2.setBackgroundDrawable(rippleButton2);
		
		RippleDrawableComp rippleRelativeLayout = new RippleDrawableComp(color, alpha, radius, duration, 
				null, mRelativeLayout, isUseGradient, isUseFadeOut);
		rippleRelativeLayout.init();
		mRelativeLayout.setBackgroundDrawable(rippleRelativeLayout);
		
		RippleDrawableComp rippleButton3 = new RippleDrawableComp(color, alpha, radius, duration, 
				getResources().getDrawable(R.drawable.ic_launcher), mButton3, isUseGradient, isUseFadeOut);
		rippleButton3.init();
		mButton3.setBackgroundDrawable(rippleButton3);
	}
}
