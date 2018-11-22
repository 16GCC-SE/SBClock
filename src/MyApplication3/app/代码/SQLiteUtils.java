package com.example.administrator.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2018/6/21.
 */

public class SQLiteUtils {
    public static final String DATABASE_NAME = "memorandum_db";
    public static final String DATETIME = "datetime";
    public static final String CONTENT = "content";
//SQLiteDatabase是一个可以进行增(Create)、查(Retrieve)、改(Update)、删(Delete)数据，即CRUD操作的类。
    public static DatabaseHelper createDBHelper(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context,DATABASE_NAME);
        return dbHelper;
    }

    public void insert(DatabaseHelper dbHelper,UserInfo user) {
        //添加时间、内容
        ContentValues values = new ContentValues();
        values.put("datetime", user.getDatetime());
        values.put("content",user.getContent());
        values.put("alerttime",user.getAlerttime());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.insert("user", null, values);
        db.close();
    }


    public void update(DatabaseHelper dbHelper) {
//更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", "GMY");
        db.update("user", values, "id=?", new String[]{"1"});
        db.close();
    }

    public void delete(DatabaseHelper dbHelper,String datetime){
        //删除数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.delete("users",null,null);

        db.execSQL("DELETE FROM " + "user" + " WHERE datetime="+ datetime);
        db.close();
    }
}

