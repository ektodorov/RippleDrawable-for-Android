package com.blogspot.techzealous.rippledrawablecomp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView mTextView;
	private Button mButton2;
	private RelativeLayout mRelativeLayout;
	private Button mButton3;
	
	private RippleDrawableComp mRipple;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTextView = (TextView)findViewById(R.id.textView1);
		mButton2 = (Button)findViewById(R.id.button2);
		mRelativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout1);
		mButton3 = (Button)findViewById(R.id.button3);
		
		getActionBar().hide();
		
		RippleDrawableComp rippleTextView = new RippleDrawableComp(Color.BLACK, 200, 20, 1000, 
				getResources().getDrawable(R.drawable.ic_launcher), mTextView);
		rippleTextView.init();
		mTextView.setBackgroundDrawable(rippleTextView);
		
		mRipple = new RippleDrawableComp(Color.BLACK, 190, 20, 1100, getResources().getDrawable(R.drawable.xml_rect_green), mButton2);
		mRipple.init();
		mButton2.setBackgroundDrawable(mRipple);
		
		RippleDrawableComp rippleRelativeLayout = new RippleDrawableComp(Color.BLACK, 150, 20, 1200, 
				getResources().getDrawable(R.drawable.ic_launcher), mRelativeLayout);
		rippleRelativeLayout.init();
		mRelativeLayout.setBackgroundDrawable(rippleRelativeLayout);
		
		RippleDrawableComp rippleButton3 = new RippleDrawableComp(Color.BLACK, 190, 20, 1100, 
				getResources().getDrawable(R.drawable.ic_launcher), mButton3);
		rippleButton3.init();
		mButton3.setBackgroundDrawable(rippleButton3);
	}
}
