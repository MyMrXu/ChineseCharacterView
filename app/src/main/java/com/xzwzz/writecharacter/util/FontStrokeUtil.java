package com.xzwzz.writecharacter.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xzwzz.writecharacter.AppContext;
import com.xzwzz.writecharacter.bean.FontStrokeDataBean;
import com.xzwzz.writecharacter.R;

import java.io.File;

/**
 * @author xzwzz
 * @time 2019-08-23
 */
public class FontStrokeUtil {
    private String DB_NAME = "strokes.db";
    private static FontStrokeUtil ourInstance;
    private SQLiteDatabase sqLiteDatabase;

    public static FontStrokeUtil getInstance() {
        if (ourInstance == null) {
            ourInstance = new FontStrokeUtil();
        }
        return ourInstance;
    }

    private FontStrokeUtil() {
    }

    public void init() {
        String dbPath = AppContext.context.getCacheDir().getAbsolutePath() + "/database/" + DB_NAME;
        if (!new File(dbPath).exists()) {
            ResourceUtils.copyFileFromRaw(R.raw.strokes, dbPath);
        }
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        if (sqLiteDatabase == null && query("一") == null) {
            Log.e("xzwzz", "init: the font database init error,try again...");
            new File(dbPath).delete();
            init();
        } else {
            Log.e("xzwzz", "init: the font database init success");
        }
    }


    //查询
    public FontStrokeDataBean query(String font) {
        FontStrokeDataBean characterBean = null;
        try {
            String table = "t_stroke";
            Cursor cursor = sqLiteDatabase.query(table, new String[]{"character", "stroke", "median"}, "character=?", new String[]{font}, null, null, null);
            if (cursor.moveToFirst()) {
                String character = cursor.getString(cursor.getColumnIndex("character"));
                String stroke = cursor.getString(cursor.getColumnIndex("stroke"));
                String median = cursor.getString(cursor.getColumnIndex("median"));
                characterBean = new FontStrokeDataBean(character, stroke, median);
                cursor.moveToNext();
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return characterBean;
    }
}
