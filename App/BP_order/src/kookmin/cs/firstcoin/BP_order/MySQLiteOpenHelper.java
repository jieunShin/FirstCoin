package kookmin.cs.firstcoin.BP_order;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	public MySQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	// 테이블 생성 코드
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table product ( name text primary key, " + "price text, orderNum text)";
		db.execSQL(sql);
	}

	// 테이블 삭제 코드
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists product";
		db.execSQL(sql);

		onCreate(db);
	}
}
