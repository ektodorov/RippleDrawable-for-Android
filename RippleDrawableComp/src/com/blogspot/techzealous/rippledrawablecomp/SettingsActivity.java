package com.blogspot.techzealous.rippledrawablecomp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends Activity {

	private EditText mEditTextColorAlpha;
	private EditText mEditTextColorRed;
	private EditText mEditTextColorGreen;
	private EditText mEditTextColorBlue;
	private EditText mEditTextAlpha;
	private EditText mEditTextRadius;
	private EditText mEditTextDuration;
	private CheckBox mCheckBoxUseGradient;
	private CheckBox mCheckBoxUseFadeout;
	private Button mButtonCancel;
	private Button mButtonApply;
	
	private int mColor;
	private int mAlpha;
	private int mRadius;
	private int mDuration;
	private boolean mIsUseGradient;
	private boolean mIsUseFadeOut;
	
	private SharedPreferences mPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_activity_layout);
		
		mEditTextColorAlpha = (EditText)findViewById(R.id.editTextColorAlpha);
		mEditTextColorRed = (EditText)findViewById(R.id.editTextColorRed);
		mEditTextColorGreen = (EditText)findViewById(R.id.editTextColorGreen);
		mEditTextColorBlue = (EditText)findViewById(R.id.editTextColorBlue);
		mEditTextAlpha = (EditText)findViewById(R.id.editTextAlpha);
		mEditTextRadius = (EditText)findViewById(R.id.editTextRadius);
		mEditTextDuration = (EditText)findViewById(R.id.editTextDuration);
		mCheckBoxUseGradient = (CheckBox)findViewById(R.id.checkBoxUseGradient);
		mCheckBoxUseFadeout = (CheckBox)findViewById(R.id.checkBoxUseFadeOut);
		mButtonCancel = (Button)findViewById(R.id.buttonCancel);
		mButtonApply = (Button)findViewById(R.id.buttonApply);
		
		mPrefs = getSharedPreferences(ConstantsRD.PREFS_FILENAME, Context.MODE_PRIVATE);
		
		mColor = mPrefs.getInt(ConstantsRD.PREF_COLOR, Color.BLACK);
		mAlpha = mPrefs.getInt(ConstantsRD.PREF_ALPHA, 150);
		mRadius = mPrefs.getInt(ConstantsRD.PREF_RADIUS, 20);
		mDuration = mPrefs.getInt(ConstantsRD.PREF_DURATION, 1000);
		mIsUseGradient = mPrefs.getBoolean(ConstantsRD.PREF_IS_USE_GRADIENT, false);
		mIsUseFadeOut = mPrefs.getBoolean(ConstantsRD.PREF_IS_USE_FADEOUT, false);
		mEditTextColorAlpha.setText(String.valueOf(Color.alpha(mColor)));
		mEditTextColorRed.setText(String.valueOf(Color.red(mColor)));
		mEditTextColorGreen.setText(String.valueOf(Color.green(mColor)));
		mEditTextColorBlue.setText(String.valueOf(Color.blue(mColor)));
		mEditTextAlpha.setText(String.valueOf(mAlpha));
		mEditTextRadius.setText(String.valueOf(mRadius));
		mEditTextDuration.setText(String.valueOf(mDuration));
		mCheckBoxUseGradient.setChecked(mIsUseGradient);
		mCheckBoxUseFadeout.setChecked(mIsUseFadeOut);
		
		mButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mButtonApply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int colorAlpha = Color.alpha(mColor);
				int colorRed = Color.red(mColor);
				int colorGreen = Color.green(mColor);
				int colorBlue = Color.blue(mColor);
				try {
					colorAlpha = Integer.valueOf(mEditTextColorAlpha.getText().toString()).intValue();
					colorRed = Integer.valueOf(mEditTextColorRed.getText().toString()).intValue();
					colorGreen = Integer.valueOf(mEditTextColorGreen.getText().toString()).intValue();
					colorBlue = Integer.valueOf(mEditTextColorBlue.getText().toString()).intValue();
					mAlpha = Integer.valueOf(mEditTextAlpha.getText().toString()).intValue();
					mRadius = Integer.valueOf(mEditTextRadius.getText().toString()).intValue();
					mDuration = Integer.valueOf(mEditTextDuration.getText().toString()).intValue();
					mIsUseGradient = mCheckBoxUseGradient.isChecked();
					mIsUseFadeOut = mCheckBoxUseFadeout.isChecked();
					
					if(colorAlpha > 255) {colorAlpha = 255;} else if(colorAlpha < 0) {colorAlpha = 0;}
					if(colorRed > 255) {colorRed = 255;} else if(colorRed < 0) {colorRed = 0;}
					if(colorGreen > 255) {colorGreen = 255;} else if(colorGreen < 0) {colorGreen = 0;}
					if(colorBlue > 255) {colorBlue = 255;} else if(colorBlue < 0) {colorBlue = 0;}
					if(mAlpha > 255) {mAlpha = 255;} else if(mAlpha < 0) {mAlpha = 0;}
					if(mDuration <= 0) {mDuration = 1000;}
					
					Editor editor = mPrefs.edit();
					editor.putInt(ConstantsRD.PREF_COLOR, Color.argb(colorAlpha, colorRed, colorGreen, colorBlue));
					editor.putInt(ConstantsRD.PREF_ALPHA, mAlpha);
					editor.putInt(ConstantsRD.PREF_RADIUS, mRadius);
					editor.putInt(ConstantsRD.PREF_DURATION, mDuration);
					editor.putBoolean(ConstantsRD.PREF_IS_USE_GRADIENT, mIsUseGradient);
					editor.putBoolean(ConstantsRD.PREF_IS_USE_FADEOUT, mIsUseFadeOut);
					editor.commit();
					
					finish();
				} catch(Exception ex) {
					AlertDialog.Builder adb = new AlertDialog.Builder(SettingsActivity.this);
					adb.setTitle(R.string.error);
					adb.setMessage(R.string.message_error);
					adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {/* do nothing */}
					});
					adb.create().show();
				}				
			}
		});
	}
}
