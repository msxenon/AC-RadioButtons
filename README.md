# AC-RadioButtons
AppChief Radio Buttons a stylish radio group buttons is very easy to use
Code in XML
    <com.appchief.msa.radiobuttonx.RadioGroupX
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:layout_centerVertical="true"
        RadioGroupX:default_color="#535353"
        android:id="@+id/radioGroup"
        RadioGroupX:selected_color="@color/colorPrimary">

        <TextView
            android:id="@+id/textView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="APPChief" />

        <TextView
            android:id="@+id/textView3"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="AppChief 1" />

        <TextView
            android:id="@+id/textView2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="AppChief 2018/3/21" />
    </com.appchief.msa.radiobuttonx.RadioGroupX>
    
    Using the listener
    RadioGroupX radioGroupX = findViewById(R.id.radioGroup);
        radioGroupX.setRadioGroupSelectListener(new RadioGroupSelectListener() {
            @Override
            public void itemSelected(int newPostion, int oldPosition, int viewId) {
                
            }
        });
