package com.example.administrator.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;

public class TimeSetDialog extends Dialog {
    Button dateSetButton,positiveButton,negativeButton;//定义设定按钮和取消按钮
    TimePicker timePicker;
    Calendar calendar;
    String date,alerttime=null;
    private TimeSetDialog timeSetDialog = null;

    private void init(){
        calendar.setTimeInMillis(System.currentTimeMillis());
        dateSetButton.setText(Utils.toDateString(calendar));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//获取小时
        int minute = calendar.get(Calendar.MINUTE);//获取分钟
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
    }
    public TimeSetDialog(Context context) {
        super(context);
        setContentView(R.layout.activity_time_set_dialog);

        timeSetDialog = this;
        //this.setTitle("");

        calendar = Calendar.getInstance();
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        dateSetButton = (Button)findViewById(R.id.dateButton);
        positiveButton = (Button)findViewById(R.id.positiveButton);
        negativeButton = (Button)findViewById(R.id.negativeButton);

        init();
        //用DatePickerDialog设置日期范围
        dateSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //设置年、月、日
                        calendar.set(year, monthOfYear, dayOfMonth);
                        date = Utils.toDateString(calendar);
                        dateSetButton.setText(date);
                    }}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //设置positiveButton，设定闹钟
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());//
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                alerttime = calendar.getTimeInMillis()+"";
                timeSetDialog.cancel();
            }
        });
        //设置negativeButton，取消设置闹钟
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSetDialog.cancel();
            }
        });
    }
}
