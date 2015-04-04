package com.blogspot.techzealous.rippledrawablecomp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class RelativeLayoutComp extends RelativeLayout {

//	public RelativeLayoutComp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//	}
	private Paint mPaint;

	public RelativeLayoutComp(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
	}

	public RelativeLayoutComp(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
	}

	public RelativeLayoutComp(Context context) {
		super(context);
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
	}

	@Override
	protected void onDraw(Canvas aCanvas)
	{
		//super.onDraw(aCanvas);
		//Paint paint = new Paint();
		//mPaint.setColor(Color.GREEN);
		//paint.setStrokeWidth(20);
		//paint.setAlpha(255);
		Log.i("RelativeLayoutComp", "onDraw");
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		aCanvas.drawCircle(50, 50, 200, mPaint);
	}
	
	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
