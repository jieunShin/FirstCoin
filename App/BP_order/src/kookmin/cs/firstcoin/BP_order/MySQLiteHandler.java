package kookmin.cs.firstcoin.BP_order;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class MySQLiteHandler {
	MySQLiteOpenHelper helper;
	SQLiteDatabase db;

	public MySQLiteHandler(Context ctx) {
		helper = new MySQLiteOpenHelper(ctx, "product.sqlite", null, 1);
	}

	// �����ͺ��̽� open
	public static MySQLiteHandler open(Context ctx) {
		return new MySQLiteHandler(ctx);
	}

	// �����ͺ��̽� close
	public void close() {
		helper.close();
	}

	// ������ ����
	public void insert(String name, String price, String orderNum) {
		db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("price", price);
		values.put("orderNum", orderNum);
		db.insert("product", null, values);
	}

	// ������ ����
	public void update(String name, String price, String orderNum) {
		db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("price", price);
		values.put("orderNum", orderNum);
		db.update("product", values, "name=?", new String[] { name });
	}

	// ������ ����
	public void delete(String name) {
		db = helper.getWritableDatabase();

		db.delete("product", "name=?", new String[] { name });
	}

	// ������ ��ȸ
	public Cursor select() {
		db = helper.getReadableDatabase();

		Cursor c = db.query("product", null, null, null, null, null, null);
		return c;
	}
}
