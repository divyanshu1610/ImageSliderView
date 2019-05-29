package com.div.libimageslider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import org.jetbrains.annotations.Nullable;

public class ImageSlider extends View {



    GestureDetector gestureDetector;

    int mRight;
    private static final int FIRST_COLOR_DEF = Color.GREEN;
    private static final int SECOND_COLOR_DEF = Color.RED;
    private static final String TAG ="ImageSlider";
    //Colors
    private int mFirstColor;
    private int mSecondColor;


    //Rects
    private Rect mRectOne;
    private Rect mRectTwo;


    //Paints
    private Paint mPaintOne;
    private Paint mPaintTwo;

    //Height
    private int mSliderHeight;




    public ImageSlider(Context context) {
        super(context);
        initImageSlider(null);
    }


    public ImageSlider(Context context,@Nullable  AttributeSet attrs) {
        super(context, attrs);

        initImageSlider(attrs);

    }


    public ImageSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImageSlider(attrs);

    }

    public ImageSlider(Context context,@Nullable  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initImageSlider(attrs);


    }


    private void initImageSlider(@Nullable  AttributeSet set) {

        gestureDetector = new GestureDetector(getContext(), new GestureListener());

        if (set == null)
            return;



        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mSliderHeight = getHeight();
                postInvalidate();

            }
        });


        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.ImageSlider);

        mFirstColor = ta.getColor(R.styleable.ImageSlider_firstColor, FIRST_COLOR_DEF);
        mSecondColor = ta.getColor(R.styleable.ImageSlider_secondColor, SECOND_COLOR_DEF);
        mSliderHeight = getDefaultHeight();

        //Initialize Paint
        mPaintOne = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOne.setColor(mFirstColor);

        mPaintTwo = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTwo.setColor(mSecondColor);


        mRectOne = new Rect();
        mRectTwo = new Rect();

        ta.recycle();


        mRight = getDefaultWidth()/2;

    }


    @Override
    protected void onDraw(Canvas canvas) {


        //Initialize RectOne
        mRectOne.left = 0;
        mRectOne.top = 0;
        mRectOne.right = mRight;
        mRectOne.bottom = mSliderHeight;


        //Initialize RectTwo
        mRectTwo.left = mRight;
        mRectTwo.top = 0;
        mRectTwo.right = getDefaultWidth();
        mRectTwo.bottom = mSliderHeight;

        canvas.drawRect(mRectOne,mPaintOne);
        canvas.drawRect(mRectTwo,mPaintTwo);

    }



    //Returns the default height of ImageSlider as Half Of Screen Size
    private int getDefaultHeight(){
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }


    //Returns the default Width of ImageSlider as Half Of Screen Size
    private int getDefaultWidth(){
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }




    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        // event when double tap occurs


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            Log.d(TAG, "onFling: X="+velocityX+" Y:"+velocityY);

            Log.d(TAG, "onFling: e1="+e1);
            Log.d(TAG, "onFling: e2="+e2);
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                mRight = mRight+100;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }

        gestureDetector.onTouchEvent(event);
       postInvalidate();
        return true;
    }

}