package com.example.myapplicationx429as;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "myrate.db";
    private static final int VERSION = 1;
    public static final String TB_NAME = "tb_rates";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // date 字段存储 yyyy-MM-dd 格式的日期
        String sql = "CREATE TABLE " + TB_NAME + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "currency_name TEXT, "
                + "rate REAL, "
                + "date TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如需升级表结构，在这里处理
    }
}
