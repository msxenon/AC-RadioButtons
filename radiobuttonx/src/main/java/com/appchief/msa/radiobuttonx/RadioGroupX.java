package com.appchief.msa.radiobuttonx;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by mac on 3/21/18.
 */

public class RadioGroupX extends FrameLayout{
    private int choicer_width;
    private List<TextView> textViews;
    boolean isWorking = false;
    private LinearLayout choicer;
    private int selectedColor,defaultColor;
    private RadioGroupSelectListener radioGroupSelectListener;
    private LinearLayout linearLayout;
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
        int[] x = new int[] {R.attr.default_color,R.attr.selected_color};
        TypedArray a = context.obtainStyledAttributes(attrs, x,0,0);
        defaultColor = a.getColor(R.styleable.RadioGroupX_default_color,Color.parseColor("#9e9e9e"));
        selectedColor = a.getColor(R.styleable.RadioGroupX_selected_color,Color.parseColor("#026100"));
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
                int padd1 = convertDpToPx(1);
                for (int i =0;i<textViews.size();i++){
                    TextView x = textViews.get(i);
                     LinearLayout.LayoutParams xLayoutParams = new LinearLayout.LayoutParams(tvWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                    xLayoutParams.weight = 1;
                    xLayoutParams.gravity = Gravity.CENTER;
                     x.setLayoutParams(xLayoutParams);
                     x.setPadding(padd3,0,padd3,0);
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
                            animateChoicer(finalI,view.getId());
                        }
                    });
                    x.setTextColor((i==0)?selectedColor:defaultColor);
                   // Log.e("test",i+" ");
                }


                choicer = new LinearLayout(context);
                choicer.setLayoutParams(new LayoutParams(choicer_width,getHeight()));
                choicer.setBackgroundResource(R.drawable.rounded_rect_white);

               // choicer.setPadding(padd1,padd3,padd1,padd3);
                addView(choicer,0);
                //Log.e("test",convertDpToPx(20)+" "+tvWidth+" "+choicer_width);

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

    private void animateChoicer(final int pos, final int id) {
        if (isWorking)
            return;
        int newTranslationX = pos * choicer_width;
        int oldTranslationX = choicerPosition * choicer_width;
        int from= oldTranslationX,to = newTranslationX;
        /*if (pos == 0){
            from = choicerM;
            to = 0;
        }*/
         if (choicerPosition == pos)
            return;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from,to);
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

    private int convertPxToDp(int px){
        return Math.round(px/(Resources.getSystem().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }
}
