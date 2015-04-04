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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class RippleDrawableComp extends Drawable {

	//private static final String LOG = "RippleDrawableComp";
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
	private boolean mIsUseGradient;
	private boolean mIsUseFadeOut;
	
	private Bitmap mBitmapOverlay;
	private Canvas mCanvasOverlay;
	
	/** 
	 * Creates a RippleDrawableComp
	 * @param int aColor - color to use for the ripple effect
	 * @param int aAlpha - alpha to use for the color effect
	 * @param int aInitialRadius - initial radius to use for the ripple effect. How big should the first circle drawn.
	 * @param int aDuration - duration that the ripple effect will have
	 * @param Drawable aDrawable - drawable to use for background of the view
	 * @param View aView - the view to which we are adding the RippleDrawableComp. This RippleDrawableComp object holds a weak reference to this view.
	 * @param boolean aIsUseGradient - if the ripple effect is going to have a RadialGradient filter for the ripple
	 * @param boolean aIsUseFadeOut - if the ripple effect should have a fading effect added to it
	 */
	public RippleDrawableComp(int aColor, int aAlpha, int aInitialRadius, int aDuration, Drawable aDrawable, View aView, 
			boolean aIsUseGradient, boolean aIsUseFadeOut)
	{
		mPaint = new Paint();
		mPaint.setColor(aColor);
		mPaint.setAlpha(aAlpha);
		mPaint.setAntiAlias(true);
		mPaintOverlay = new Paint();
		mPaintOverlay.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		mCanvasOverlay = new Canvas();
		mColor = aColor;
		mRadius = aInitialRadius;
		mDuration = aDuration;
		mAlpha = aAlpha;
		mDrawable = aDrawable;
		mWeakView = new WeakReference<View>(aView);
		mIsUseGradient = aIsUseGradient;
		mIsUseFadeOut = aIsUseFadeOut;
	}
	
	/** 
	 * Initialize the RippleDrawableComp.
	 * You have to always call this method after creating a RippleDrawableComp object.
	 */
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
	
	/** 
	 * Sets the bounds of the RippleDrawableComp object.
	 * We need to know where we are going to draw the ripple.
	 */
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
	
	/** Sets the radius of the RippleDrawableComp. */
	public void setRadius(int aRadius)
	{
		mRadius = aRadius;
		invalidateSelf();
	}
	
	public void setDrawable(Drawable aDrawable) {mDrawable = aDrawable;}
	public void setView(View aView) {mWeakView = new WeakReference<View>(aView);}
	public void setDuration(int aDuration) {mDuration = aDuration;}
	
	/** 
	 * Starts the ripple effect.
	 * @param int aX - x coordinate (relative to the RippleDrawableComp's rect) of center for the ripple.
	 * @param int aY - y coordinate (relative to the RippleDrawableComp's rect) of center for the ripple.
	 * @param int aSize - the maximum size the ripple effect should draws to.
	 * @param int aDuration - duration for the ripple effect. */
	public void ripple(int aX, int aY, final int aSize, int aDuration)
	{
		if(mIsUseGradient) {
			RadialGradient rg = new RadialGradient(aX, aY, mRadius, Color.TRANSPARENT, mColor, TileMode.CLAMP);
			mPaint.setShader(rg);
		}
		mPaint.setAlpha(mAlpha);
		
		mIsRipple = true;
		mXDown = aX;
		mYDown = aY;
		mRadius = 0;
		
		ValueAnimator animation = ValueAnimator.ofInt(mRadius, aSize);
		animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator aValueAnimator) 
			{
				int radius = (Integer)aValueAnimator.getAnimatedValue();
				mRadius = radius;
				if(aSize == radius) {mIsRipple = false;}
				
				/* Decrease the mPaint's alpha when we are at 70% of the size of the loop. */
				//we are saving us *100 and /100 when calculating the percents (no need to multiply by 100, just to then delete by 100)
				if(mIsUseFadeOut) {
					float percent = 1.0f - ((float)radius / (float)mMaxRadius);
					if((int)percent < kThresholdStartFade) {
						int alpha = (int)(mAlpha * percent);
						mPaint.setAlpha(alpha);
					}
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
	public int getOpacity() {return mAlpha;}

	@Override
	public void setAlpha(int aAlpha) {mAlpha = aAlpha;}

	/** This is an empty implementation, it does nothing. Don't use. */
	@Override
	public void setColorFilter(ColorFilter aColorFilter) {/* do nothing */}

}
