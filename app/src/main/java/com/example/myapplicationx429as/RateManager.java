package com.example.myapplicationx429as;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RateManager {
    private final DBHelper dbHelper;

    public RateManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    /** 插入或覆盖一条汇率记录 **/
    public void add(RateItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues v = new ContentValues();
            v.put("currency_name", item.getCurrencyName());
            v.put("rate", item.getRate());
            v.put("date", item.getDate());
            db.insert(DBHelper.TB_NAME, null, v);
        } finally {
            db.close();
        }
    }

    /** 查询指定币种当天记录 **/
    public RateItem find(String currencyName, String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TB_NAME, null,
                "currency_name=? AND date=?",
                new String[]{currencyName, date},
                null, null, null);
        try {
            if (c.moveToFirst()) {
                RateItem item = new RateItem();
                item.setId(c.getInt(c.getColumnIndexOrThrow("_id")));
                item.setCurrencyName(c.getString(c.getColumnIndexOrThrow("currency_name")));
                item.setRate(c.getFloat(c.getColumnIndexOrThrow("rate")));
                item.setDate(c.getString(c.getColumnIndexOrThrow("date")));
                return item;
            }
            return null;
        } finally {
            c.close();
            db.close();
        }
    }

    /** 列出指定日期的所有汇率 **/
    public List<RateItem> list(String date) {
        List<RateItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TB_NAME, null,
                "date=?", new String[]{date},
                null, null, null);
        try {
            int idxId   = c.getColumnIndexOrThrow("_id");
            int idxName = c.getColumnIndexOrThrow("currency_name");
            int idxRate = c.getColumnIndexOrThrow("rate");
            int idxDate = c.getColumnIndexOrThrow("date");
            while (c.moveToNext()) {
                RateItem r = new RateItem();
                r.setId(c.getInt(idxId));
                r.setCurrencyName(c.getString(idxName));
                r.setRate(c.getFloat(idxRate));
                r.setDate(c.getString(idxDate));
                list.add(r);
            }
        } finally {
            c.close();
            db.close();
        }
        return list;
    }
}
