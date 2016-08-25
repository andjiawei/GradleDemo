package com.netsite.galleryanimation.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by QYer on 2016/8/16.
 */
public class FoldLayout extends ViewGroup {
    private static final int NUM_OF_POINT = 8;
    /**
     * 图片的折叠后的总宽度
     */
    private float mTranslateDis;

    protected float mFactor = 1f;//折叠角度

    private int mNumOfFolds = 2;//折叠成几片

    private Matrix[] mMatrices = new Matrix[mNumOfFolds];

    private Paint mSolidPaint;

    private Paint mShadowPaint;
    private Matrix mShadowGradientMatrix;
    private LinearGradient mShadowGradientShader;

    private float mFlodWidth;
    private float mTranslateDisPerFlod;

    public FoldLayout(Context context)
    {
        this(context, null);
    }

    public FoldLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        for (int i = 0; i < mNumOfFolds; i++)
        {
            mMatrices[i] = new Matrix();
        }

        mSolidPaint = new Paint();
        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Paint.Style.FILL);
//      mShadowGradientShader = new LinearGradient(0, 0, 0.5f, 0, Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mShadowGradientShader = new LinearGradient(0, 0, 0, 0.5f, Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mShadowPaint.setShader(mShadowGradientShader);
        mShadowGradientMatrix = new Matrix();
        this.setWillNotDraw(false);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        View child = getChildAt(0);
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(child.getMeasuredWidth(), child.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        View child = getChildAt(0);
        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());

        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);
        updateFold();
    }

    private void updateFold()
    {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        mTranslateDis =  h * mFactor;
        mFlodWidth=h/mNumOfFolds;
        mTranslateDisPerFlod =  mTranslateDis / mNumOfFolds;;

        int alpha = (int) (255 * (1 - mFactor));
        mSolidPaint.setColor(Color.argb((int) (alpha * 0.8F), 0, 0, 0));

        mShadowGradientMatrix.setScale(1, mFlodWidth);
        mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);
        mShadowPaint.setAlpha(alpha);

        float depth = (float) (Math.sqrt(mFlodWidth * mFlodWidth - mTranslateDisPerFlod * mTranslateDisPerFlod) / 2);

        float[] src = new float[NUM_OF_POINT];//原坐标点
        float[] dst = new float[NUM_OF_POINT];//目标坐标点

        for (int i = 0; i < mNumOfFolds; i++)
        {
            mMatrices[i].reset();
//            src[0] = i * mFlodWidth;//一个折叠的宽度
//            src[1] = 0;
//            src[2] = src[0] + mFlodWidth;
//            src[3] = 0;
//            src[4] = src[2];
//            src[5] = h;
//            src[6] = src[0];
//            src[7] = src[5];

            src[0] = 0;
            src[1] = i * mFlodWidth;//一个折叠的高度

            src[2] = w;
            src[3] = i*mFlodWidth;

            src[4] = w;
            src[5] = src[1]+mFlodWidth;

            src[6] = 0;
            src[7] = src[5];

            boolean isEven = i % 2 == 0;
//            dst[0] = i * mTranslateDisPerFlod;
//            dst[1] = isEven ? 0 : depth;
//
//            dst[2] = dst[0] + mTranslateDisPerFlod;
//            dst[3] = isEven ? depth : 0;
//
//            dst[4] = dst[2];
//            dst[5] = isEven ? h - depth : h;
//
//            dst[6] = dst[0];
//            dst[7] = isEven ? h : h - depth;

//            mTranslateDis = h * mFactor;
//            mTranslateDisPerFlod = mTranslateDis / mNumOfFolds;//单个fold的高度
//            mFlodWidth=h/mNumOfFolds;
            depth=(float) (Math.sqrt(mFlodWidth * mFlodWidth - mTranslateDisPerFlod * mTranslateDisPerFlod) / 2);
            Log.e("depth", "updateFold: "+depth );
            dst[0] = isEven?0:depth;
            dst[1] = i * mTranslateDisPerFlod;

            dst[2] = isEven?w:w-depth;
            dst[3] = dst[1];

            dst[4] = isEven?w-depth:w;
            dst[5] =  dst[3]+mTranslateDisPerFlod;

            dst[6] = isEven?depth:0;
            dst[7] = dst[5];

            for (int y = 0; y < 8; y++)
            {
                dst[y] = Math.round(dst[y]);
            }

            mMatrices[i].setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        }
    }

    private Canvas mCanvas = new Canvas();
    private Bitmap mBitmap;
    private boolean isReady;

    @Override
    protected void dispatchDraw(Canvas canvas)
    {

        if (mFactor == 0)
            return;
        if (mFactor == 1)
        {
            super.dispatchDraw(canvas);
            return;
        }
//        canvas.rotate(90,getMeasuredWidth()/2,getMeasuredHeight()/2);//原控件按照竖直计算 需要旋转和平移下
//        canvas.translate(-getMeasuredHeight()/2+getMeasuredWidth()/2,getMeasuredWidth()/2-getMeasuredHeight()/2);
//        canvas.translate(0,getMeasuredWidth());
//        canvas.rotate(-90);//原控件按照竖直计算 需要旋转和平移下
//        canvas.rotate(-180,getMeasuredWidth()/2,getMeasuredHeight()/2);

        for (int i = 0; i < mNumOfFolds; i++)
        {
            canvas.save();

            canvas.concat(mMatrices[i]);
//            canvas.clipRect(mFlodWidth * i, 0, mFlodWidth * i + mFlodWidth, getHeight());
            canvas.clipRect(0, mFlodWidth * i, getWidth(),mFlodWidth * i+mFlodWidth);

            if (isReady)
            {
                canvas.drawBitmap(mBitmap, 0, 0, null);
            } else
            {
                // super.dispatchDraw(canvas);
                super.dispatchDraw(mCanvas);
                canvas.drawBitmap(mBitmap, 0, 0, null);
                isReady = true;
            }
            canvas.translate(0, mFlodWidth * i);
            if (i % 2 == 0)
            {
                canvas.drawRect(0, 0, getWidth(), mFlodWidth, mSolidPaint);
            } else
            {
                canvas.drawRect(0, 0, getWidth(), mFlodWidth, mShadowPaint);
            }
            canvas.restore();
        }
    }
    //...dispatchDraw

    public void setFactor(float factor)
    {
        if(factor<0.05){//最后消失时候 会突然全屏一下 这里判断过滤下
            setVisibility(VISIBLE);
            return;
        }
        this.mFactor = factor;
        updateFold();
        invalidate();
    }

    public float getFactor()
    {
        return mFactor;
    }

    public void startAnamation(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                setFactor(value);
            }
        });
        valueAnimator.start();
    }
}
