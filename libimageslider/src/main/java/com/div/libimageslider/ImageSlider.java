package com.div.libimageslider;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import org.jetbrains.annotations.Nullable;

public class ImageSlider extends View {


    int midPoint;
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


        midPoint = getDefaultWidth()/2;

    }


    @Override
    protected void onDraw(Canvas canvas) {


        //Initialize RectOne
        mRectOne.left = 0;
        mRectOne.top = 0;
        //Right will change only
        mRectOne.right = midPoint;
        mRectOne.bottom = mSliderHeight;


        //Initialize RectTwo

        //Left will change only
        mRectTwo.left = midPoint;
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


    int posX,posY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {



        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                posX = (int)event.getX();
                posY = (int)event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                Log.d(TAG, "onTouchEvent: X: "+event.getX());

                if(posX < event.getX())
                    swipeRight();
                else if(posX > event.getX())
                    swipeLeft();
                break;
            case MotionEvent.ACTION_UP:
                decideIt();
                break;
            default:
                return false;
        }

        postInvalidate();
        return true;
    }



    private void swipeRight(){
        midPoint+=10;
        //postInvalidate();
    }


    private void swipeLeft(){
        midPoint-=10;
        //postInvalidate();
    }



    private void animate(int dest){

        ValueAnimator animation = ValueAnimator.ofInt(midPoint,dest);
        animation.setDuration(300);
        animation.start();

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                // You can use the animated value in a property that uses the
                // same type as the animation. In this case, you can use the
                // float value in the translationX property.
                int animatedValue =(int)updatedAnimation.getAnimatedValue();
                midPoint = animatedValue;
               // Log.d(TAG, "onAnimationUpdate: "+midPoint);
                postInvalidate();
            }
        });


    }







    private void decideIt(){


        int temp;

        if(midPoint > getDefaultWidth()/2){

            temp = (3*getDefaultWidth())/4;
            if(midPoint > temp) {
                //animate to FULL
               animate(getDefaultWidth());
            }else{
                //animate to MID
                animate(getDefaultWidth()/2);
            }
        }else{
            temp = getDefaultWidth()/4;
            if(midPoint < temp) {
                //animate to FULL
                animate(0);
            }else{
                //animate to MID
                animate(getDefaultWidth()/2);
            }
        }
    }

}