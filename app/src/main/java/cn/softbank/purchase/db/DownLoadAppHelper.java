package cn.softbank.purchase.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DownLoadAppHelper extends SQLiteOpenHelper {
	
	private static final String DA_NAME = "downloadApp.db";
	
	private Dao dao;
	
	public DownLoadAppHelper(Context context,Dao dao) {
		super(context, DA_NAME, null, 1);
		this.dao = dao;
	}

	/**
	 * 在download.db数据库下创建一个download_info表存储下载信息
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(dao.getCreateTable());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
