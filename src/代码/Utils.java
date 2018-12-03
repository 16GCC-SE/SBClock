package com.example.administrator.myapplication;

/**
 * Created by Administrator on 2018/6/21.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.text.format.Time;

public class Utils {
    private static ArrayList<HashMap<String,String>> arraylist = new ArrayList<HashMap<String,String>>();
    private static ArrayList<HashMap<String,String>> showlist = new ArrayList<HashMap<String,String>>();
    private static Long[] tempTimeMillis = null;
    public static void put(HashMap<String,String> map){
        arraylist.add(map);
    }

    public static ArrayList<HashMap<String, String>> getList(){
        return arraylist;
    }

    public static HashMap<String, String> getItem(int position){
        return arraylist.get(position);
    }
//建造下拉时间列表
    public static void sort(){
        int size = arraylist.size();
        for (int i =size-1; i>= 0; i--)
            for (int j = 0; j < i; j++)
                if (Long.parseLong(arraylist.get(j).get("datetime"))< Long.parseLong(arraylist.get(j+1).get("datetime"))) {
                    HashMap<String,String> temp = arraylist.get(j);
                    arraylist.set(j, arraylist.get(j+1));
                    arraylist.set(j+1, temp);
                }
    }
//小时的下拉列表
    public static void MillisToDate(ArrayList<HashMap<String,String>> arraylist){
        int size = arraylist.size();
        tempTimeMillis = new Long[size];
        for(int i=0;i<size;i++)
        {
            String temp = timeTransfer(i);
            arraylist.get(i).remove("datetime");
            arraylist.get(i).put("datetime", temp);
        }
    }
//分钟的下拉列表
    public static void DateToMillis(ArrayList<HashMap<String,String>> arraylist){
        int size = arraylist.size();
        for(int i=0;i<size;i++){
            String temp = String.valueOf(tempTimeMillis[i]);
            arraylist.get(i).remove("datetime");
            arraylist.get(i).put("datetime", temp);
        }
    }
    public static String format(int hourOfDay) {
        String str = "" + hourOfDay;
        if(str.length() == 1){
            str = "0" + str;
        }
        return str;
    }
    //下拉列表表头显示时间和星期
    public static String toDateString(Calendar calendar){
        String day = null;
        switch(calendar.get(Calendar.DAY_OF_WEEK))
        {
            case 1: day = "星期日";break;
            case 2: day = "星期一";break;
            case 3: day = "星期二";break;
            case 4: day = "星期三";break;
            case 5: day = "星期四";break;
            case 6: day = "星期五";break;
            case 7: day = "星期六";break;
        }
        return calendar.get(Calendar.YEAR)+"年"
                +Utils.format(calendar.get(Calendar.MONTH)+1)+"月"
                +calendar.get(Calendar.DAY_OF_MONTH)+"日"
                +""+ day;
    }
    //在主页面内容旁显示建立备忘录的时间
    public static String timeTransfer(int i){
        Time time = new Time();//实例化Time对象
        long tempLong = Long.parseLong(arraylist.get(i).get("datetime"));
        tempTimeMillis[i] = tempLong;
        //����
        time.setToNow();
        String dateNow = time.toString().substring(0,8);
        String timeNow = time.toString().substring(9,13);
        time.set(tempLong);
        String datePast = time.toString().substring(0,8);
        String timePast = time.toString().substring(9,13);
        long tempDate = Long.parseLong(dateNow)-Long.parseLong(datePast);
        long tempTime = Long.parseLong(timeNow)-Long.parseLong(timePast);
        if(tempDate == 0)
            return timePast.substring(0, 2)+":"+timePast.substring(2, 4);
        if(tempDate == 1)
            return "";
        if(tempDate>=2)
            return datePast.substring(4,6)+"/"+datePast.substring(6,8);
        return null;
    }

    public static String timeTransfer(String i){
        Time time = new Time();
        time.setToNow();
        String dateNow = time.toString().substring(0,8);
        String timeNow = time.toString().substring(9,13);
        time.set(Long.parseLong(i));
        String dateSet = time.toString().substring(0,8);
        String timeSet = time.toString().substring(9,13);
        long tempDate = Long.parseLong(dateSet)-Long.parseLong(dateNow);
        long tempTime = Long.parseLong(timeSet)-Long.parseLong(timeNow);
        if(tempDate < 0 || (tempDate ==0 && tempTime<0))
            return "已过期";
        if(tempDate == 0 && tempTime > 0)
            return "今天"+timeSet.substring(0, 2)+":"+timeSet.substring(2, 4);
        if(tempDate == 1)
            return "明天"+timeSet.substring(0, 2)+":"+timeSet.substring(2, 4);
        if(tempDate == 2)
            return "后天"+timeSet.substring(0, 2)+":"+timeSet.substring(2, 4);
        if(tempDate > 2)
            return dateSet.substring(4,6)+"/"+dateSet.substring(6,8);
        return null;
    }
}
