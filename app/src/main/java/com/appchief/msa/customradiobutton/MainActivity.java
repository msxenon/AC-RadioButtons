package com.appchief.msa.customradiobutton;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.appchief.msa.radiobuttonx.RadioGroupSelectListener;
import com.appchief.msa.radiobuttonx.RadioGroupX;

/**
 * Created by mac on 3/21/18.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioGroupX radioGroupX = findViewById(R.id.radioGroup);
        radioGroupX.setSelectedItem(1);
        radioGroupX.setRadioGroupSelectListener(new RadioGroupSelectListener() {
            @Override
            public void itemSelected(int newPostion, int oldPosition, int viewId) {

            }
        });
    }
}
