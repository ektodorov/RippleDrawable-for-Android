package com.blogspot.techzealous.rippledrawablecomp;

import java.lang.ref.WeakReference;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;

public class RippleDrawableComp extends Drawable {

	//private static final String LOG = "RippleDrawableComp";
	private WeakReference<View> mWeakView;
	private Drawable mDrawable;
	private Paint mPaint;
	private Paint mPaintInner;
	private int mColor;
	private int mRadius;
	private int mXDown;
	private int mYDown;
	private boolean mIsRipple;
	private int mMaxRadius;
	private int mWidth;
	private int mHeight;
	private int mDuration;
	
	public RippleDrawableComp(int aColor, int aAlpha, int aInitialRadius, int aDuration, Drawable aDrawable, View aView)
	{
		mPaint = new Paint();
		mPaint.setColor(aColor);
		mPaint.setAlpha(aAlpha);
		mPaint.setAntiAlias(true);
		mPaintInner = new Paint();
		mPaintInner.setColor(Color.WHITE);
		mColor = aColor;
		mRadius = aInitialRadius;
		mDuration = aDuration;
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
			}
		});
	}
	
	public void setBounds(Rect aBounds)
	{
		int width = aBounds.right;
		int height = aBounds.bottom;
		if(width < height) {mMaxRadius = width;} else {mMaxRadius = height;}
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
				invalidateSelf();
			}
		});
		animation.setDuration(aDuration);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.start();
	}
	
	@Override
	public void draw(Canvas aCanvas) 
	{
		if(mIsRipple) {
			mDrawable.draw(aCanvas);
			aCanvas.drawCircle(mXDown, mYDown, mRadius, mPaint);
			//aCanvas.drawCircle(mXDown, mYDown, mRadius - 20, mPaintInner);
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
