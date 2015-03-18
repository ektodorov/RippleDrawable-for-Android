package com.blogspot.techzealous.rippledrawablecomp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button mButton1;
	private Button mButton2;
	
	private RippleDrawableComp mRipple;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mButton1 = (Button)findViewById(R.id.button1);
		mButton2 = (Button)findViewById(R.id.button2);
		
		getActionBar().hide();
		
		mRipple = new RippleDrawableComp(Color.BLACK, 100, 20, 500, getResources().getDrawable(R.drawable.ic_launcher), mButton2);
		mRipple.init();
		mButton2.setBackgroundDrawable(mRipple);
	}
}
