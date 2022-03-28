package ru.samsung.itschool.dbgame;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	/*
	 * TABLES: ------- RESULTS SCORE INTEGER USER VARCHAR
	 */
	private Context context;
	private String DB_NAME = "game.db";

	private SQLiteDatabase db;

	private static DBManager dbManager;

	public static DBManager getInstance(Context context) {
		if (dbManager == null) {
			dbManager = new DBManager(context);
		}
		return dbManager;
	}

	private DBManager(Context context) {
		this.context = context;
		db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		createTablesIfNeedBe(); 
	}

	void addResult(String username, int score) {
//		db.execSQL("INSERT INTO RESULTS VALUES ('" + username + "', " + score
//				+ ");");
		ContentValues contentValues = new ContentValues();
		contentValues.put("username",username);
		contentValues.put("score",score);
		db.insert("RESULTS",null,contentValues);
	}
	// player one 150
	// запрос
	// INSERT INTO RESULTS VALUES('player one', 150);


	ArrayList<Result> getAllResults() {

		ArrayList<Result> data = new ArrayList<Result>();
//		Cursor cursor = db.rawQuery("SELECT * FROM RESULTS;", null);
		Cursor cursor = db.rawQuery("SELECT * FROM RESULTS WHERE USERNAME=? ORDER BY ?",new String[]{"player1", "score"});
		Cursor cursor2 = db.query("RESULTS",new String[]{"SCORE","USERNAME"},"SCORE > ? and USERNAME = ?",new String[]{"500","player1"},null,null,"SCORE DESC");
		boolean hasMoreData = cursor.moveToFirst();

		while (hasMoreData) {
			String name = cursor.getString(cursor.getColumnIndex("USERNAME"));
			int score = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("SCORE")));
			data.add(new Result(name, score));
			hasMoreData = cursor.moveToNext();
		}

		return data;
	}

	ArrayList<Result> getSumByUsers(String a) {
//		SELECT USERNAME,MAX(SCORE) AS M FROM RESULTS GROUP BY USERNAME ORDER BY M DESC
		ArrayList<Result> data = new ArrayList<Result>();
		Cursor cursor = db.query("RESULTS",new String[]{"USERNAME","MAX(SCORE) AS M"},null,null,"USERNAME",null,"M DESC");
		boolean hasMoreData = cursor.moveToFirst();

		while (hasMoreData) {
			String name = cursor.getString(cursor.getColumnIndex("USERNAME"));
			int score = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("M")));
			data.add(new Result(name, score));
			hasMoreData = cursor.moveToNext();
		}

		return data;
	}


	private void createTablesIfNeedBe() {
		db.execSQL("CREATE TABLE IF NOT EXISTS RESULTS (USERNAME TEXT, SCORE INTEGER);");
	}
}
