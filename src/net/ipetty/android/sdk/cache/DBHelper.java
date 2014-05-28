/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 * @author Administrator
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

    private static final int VERSION = 1;

    private static final String NAME = "cache.db";

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate:" + db.toString());
        StringBuilder str = new StringBuilder();
        str.append("create table IF NOT EXISTS [http](");
        str.append("[id] TEXT PRIMARY KEY,");	//MD5(url)
        str.append("[url] TEXT,");				//url含参数
        str.append("[value] TEXT,");			//返回值
        str.append("[etag] TEXT,");				//etag
        str.append("[hits] BIGINT,");			//命中次数
        str.append("[put_on] BIGINT,");			//保存或更新时间戳（毫秒数）
        str.append("[expire_on] BIGINT,");		//过期时间戳（毫秒数）
        str.append("[class_type] TEXT);");		//Value所对应的Java类型
        db.execSQL(str.toString());
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
        Log.d(TAG, "onUpgrade:" + db.toString());
        db.execSQL("DROP TABLE IF EXISTS [http]");
        onCreate(db);
    }

}
