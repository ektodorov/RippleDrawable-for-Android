package com.blogspot.techzealous.rippledrawablecomp;

import java.lang.ref.WeakReference;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class RippleDrawableComp extends Drawable {

	private static final String LOG = "RippleDrawableComp";
	private final static int kThresholdStartFade = 30;
	
	private WeakReference<View> mWeakView;
	private Drawable mDrawable;
	private Paint mPaint;
	private Paint mPaintOverlay;
	private int mColor;
	private int mRadius;
	private int mXDown;
	private int mYDown;
	private boolean mIsRipple;
	private int mMaxRadius;
	private int mWidth;
	private int mHeight;
	private int mDuration;
	private int mAlpha;
	
	private Bitmap mBitmapOverlay;
	private Canvas mCanvasOverlay;
	
	public RippleDrawableComp(int aColor, int aAlpha, int aInitialRadius, int aDuration, Drawable aDrawable, View aView)
	{
		mPaint = new Paint();
		mPaint.setColor(aColor);
		mPaint.setAlpha(aAlpha);
		mPaint.setAntiAlias(true);
		mPaintOverlay = new Paint();
		//mPaintInner.setColor(Color.WHITE);
		mPaintOverlay.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		mCanvasOverlay = new Canvas();
		mColor = aColor;
		mRadius = aInitialRadius;
		mDuration = aDuration;
		mAlpha = aAlpha;
		mDrawable = aDrawable;
		mWeakView = new WeakReference<View>(aView);
	}
	
	public void init() 
	{
		View view = mWeakView.get();
		if(view == null) {return;}
		view.post(new Runnable() {
			@Override
			public void run() {
				View view = mWeakView.get();
				if(view == null) {return;}
				mWidth = view.getWidth();
				mHeight = view.getHeight();
				setBounds(new Rect(0, 0, mWidth, mHeight));
				
				mBitmapOverlay = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
				mCanvasOverlay.setBitmap(mBitmapOverlay);
			}
		});
	}
	
	public void setBounds(Rect aBounds)
	{
		int width = aBounds.right;
		int height = aBounds.bottom;
		//set the mMaxRadius as the shorter or longer side of the view
		if(width > height) {mMaxRadius = width;} else {mMaxRadius = height;}
		mDrawable.setBounds(aBounds);
		
		View view = mWeakView.get();
		if(view == null) {return;}
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					int downX = (int)event.getX();
					int downY = (int)event.getY();
					ripple(downX, downY, mMaxRadius, mDuration);
				} else if(event.getAction() == MotionEvent.ACTION_UP) {
					v.performClick();
				}
				return false;
			}
		});
	}
	
	public void setRadius(int aRadius)
	{
		mRadius = aRadius;
		invalidateSelf();
	}
	
	public void setDrawable(Drawable aDrawable) {mDrawable = aDrawable;}
	public void setView(View aView) {mWeakView = new WeakReference<View>(aView);}
	public void setDuration(int aDuration) {mDuration = aDuration;}
	
	public void ripple(int aX, int aY, int aSize, int aDuration)
	{
		RadialGradient rg = new RadialGradient(aX, aY, mRadius, Color.TRANSPARENT, mColor, TileMode.CLAMP);
		mPaint.setShader(rg);
		mPaint.setAlpha(mAlpha);
		
		mIsRipple = true;
		mXDown = aX;
		mYDown = aY;
		mRadius = 0;
		
		ValueAnimator animation = ValueAnimator.ofInt(mRadius, mMaxRadius);
		animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator aValueAnimator) 
			{
				int radius = (Integer)aValueAnimator.getAnimatedValue();
				mRadius = radius;
				if(mMaxRadius == radius) {mIsRipple = false;}
				
				/* Decrease the mPaint's alpha when we are at 70% of the size of the loop. */
				//we are saving us *100 and /100 when calculating the percents (no need to multiply by 100, just to then delete by 100)
				float percent = 1.0f - ((float)radius / (float)mMaxRadius);
				if((int)percent < kThresholdStartFade) {
					int alpha = (int)(mAlpha * percent);
					mPaint.setAlpha(alpha);
				}
				
				invalidateSelf();
			}
		});
		animation.setDuration(aDuration);
		animation.start();
	}
	
	@Override
	public void draw(Canvas aCanvas) 
	{
		if(mIsRipple) {
			mDrawable.draw(aCanvas);
			aCanvas.drawCircle(mXDown, mYDown, mRadius, mPaint);
		} else {
			mDrawable.draw(aCanvas);
		}
	}

	@Override
	public int getOpacity() 
	{
		return 0;
	}

	@Override
	public void setAlpha(int aAlpha) 
	{

	}

	@Override
	public void setColorFilter(ColorFilter aColorFilter) 
	{
		
	}

}
