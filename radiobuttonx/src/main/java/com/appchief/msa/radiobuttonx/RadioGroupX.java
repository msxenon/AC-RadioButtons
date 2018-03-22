package com.appchief.msa.radiobuttonx;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by Mohammed AlFatla on 3/21/18.
 */

public class RadioGroupX extends FrameLayout{
    private int choicer_width;
    private List<TextView> textViews;
    boolean isWorking = false;
    private ImageView choicer;
    private int selectedColor,defaultColor;
    private RadioGroupSelectListener radioGroupSelectListener;
    private LinearLayout linearLayout;
    private int choicer_drawable;
    private int choicer_margin = 0;
    public void setRadioGroupSelectListener(RadioGroupSelectListener radioGroupSelectListener){
        this.radioGroupSelectListener = radioGroupSelectListener;
    }
    private void notifyListener(int oldPos,int newPos,int viewId){
        if (radioGroupSelectListener != null)
            radioGroupSelectListener.itemSelected(oldPos,newPos,viewId);
    }
    public RadioGroupX(Context context) {
        super(context);
        init(context,null);

    }

    public RadioGroupX(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);

    }

    public RadioGroupX(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RadioGroupX(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }
    void init(final Context context,AttributeSet attrs){
         TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RadioGroupX,0,0);
        defaultColor = a.getColor(R.styleable.RadioGroupX_default_color,Color.WHITE);
        selectedColor = a.getColor(R.styleable.RadioGroupX_selected_color,Color.GREEN);
        choicer_margin =   a.getDimensionPixelSize(R.styleable.RadioGroupX_choicer_margin,0);
        choicer_drawable = a.getResourceId(R.styleable.RadioGroupX_choicer_drawable,R.drawable.rounded_rect_white);
        a.recycle();
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RadioGroupX.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                linearLayout = new LinearLayout(context);
                linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                linearLayout.setOrientation(HORIZONTAL);
                int childs = getChildCount();
                choicer_width = (getWidth()/childs);

                textViews = new ArrayList<>();
                for (int i = 0; i < childs; i++){
                    View xV =  getChildAt(i);
                    if (xV instanceof TextView) {
                        textViews.add((TextView) xV);

                    }

                }
                removeAllViews();
                addView(linearLayout);
                linearLayout.setWeightSum(childs);
                linearLayout.requestLayout();
                int tvWidth = choicer_width ;
                int padd3 = convertDpToPx(3);
                int padd5 = convertDpToPx(5);
                for (int i =0;i<textViews.size();i++){
                    TextView x = textViews.get(i);
                     LinearLayout.LayoutParams xLayoutParams = new LinearLayout.LayoutParams(tvWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                    xLayoutParams.weight = 1;
                    xLayoutParams.gravity = Gravity.CENTER;
                     x.setLayoutParams(xLayoutParams);
                     x.setPadding(padd5,0,padd5,0);
                    x.requestLayout();
                    linearLayout.addView(x,xLayoutParams);
                    x.setGravity(Gravity.CENTER);
                    x.setBackgroundColor(Color.TRANSPARENT);
                    x.setEllipsize(TextUtils.TruncateAt.END);
                    x.setMaxLines(1);
                    final int finalI = i;
                    x.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            animateChoicer(finalI,view.getId(),false);
                        }
                    });
                    x.setTextColor(defaultColor);
                   // Log.e("test",i+" ");
                }


                choicer = new ImageView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(choicer_width,getHeight());
                 choicer.setLayoutParams(layoutParams);
                choicer.setImageResource(choicer_drawable);
                choicer.setPadding(choicer_margin,choicer_margin,choicer_margin,choicer_margin);
                addView(choicer,0);
                if (choicerPosition==0)
                    animateChoicer(choicerPosition,0,true);

            }


        });

    }
    int choicerPosition = 0;
    public void setTextViewsText(String[] strings){
        for (int i =0;i<linearLayout.getChildCount();i++){
            ((TextView) linearLayout.getChildAt(i)).setText(strings[i]);
        }
    }
    public boolean isWorking() {
        return isWorking;
    }

    private void animateChoicer(final int pos, final int id,boolean force) {
        if (isWorking)
            return;
        int newTranslationX = pos * choicer_width;
        int from= choicerPosition * choicer_width;

        if (choicerPosition == pos && !force)
            return;

         if (force){
             choicer.setTranslationX(newTranslationX);

             ((TextView) linearLayout.getChildAt(choicerPosition)).setTextColor(defaultColor);
             ((TextView) linearLayout.getChildAt(pos)).setTextColor(selectedColor);
             choicerPosition = pos;
             return;
         }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, newTranslationX);
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float x = (Float) valueAnimator.getAnimatedValue();
                choicer.setTranslationX(x);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
               isWorking = true;
                ((TextView) linearLayout.getChildAt(choicerPosition)).setTextColor(defaultColor);
                ((TextView) linearLayout.getChildAt(pos)).setTextColor(selectedColor);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                isWorking = false;
                notifyListener(choicerPosition,pos,id);
                choicerPosition = pos;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.start();

    }
    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));

    }


    public void setSelectedItem(final int i) {
        if (linearLayout != null) {
            animateChoicer(i, linearLayout.getChildAt(i).getId(), true);
            return;
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RadioGroupX.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                animateChoicer(i,linearLayout.getChildAt(i).getId(),true);

            }
        });
    }
}
