package com.brighterbrain.goodstracker;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhandler extends SQLiteOpenHelper {

	private static final String DBNAME = "assetkit.db";
	private static final int DB_VERSION_CODE = 1;
	private static final String ASSET_TABLE = "asset";
	private static final String _ID = "Id";
	private static final String NAME = "Name";
	private static final String DESCRIPTION = "Description";
	private static final String IMAGE_URL = "Image";
	private static final String LOCATION = "Location";
	private static final String COST = "Cost";

	private static final String CREATE_ASSET_TABLE_STRING = "CREATE TABLE IF NOT EXISTS "
			+ ASSET_TABLE
			+ "("
			+ _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ NAME
			+ " TEXT,"
			+ DESCRIPTION
			+ " TEXT,"
			+ IMAGE_URL
			+ " TEXT,"
			+ LOCATION + " TEXT," + COST + " TEXT)";
	private static final String DELETE_ASSET_TABLE_STRING = "DELETE TABLE IF EXISTS "
			+ ASSET_TABLE;

	private static final String GET_ALL_ASSETS_LIST = "SELECT * FROM "
			+ ASSET_TABLE;
	private static final String GET_ASSETS_BY_ID = "SELECT * FROM "
			+ ASSET_TABLE + " WHERE " + _ID + "=";

	public DBhandler(Context context) {
		super(context, DBNAME, null, DB_VERSION_CODE);

	}

	public long addAsset(Asset asset) {
		long result = 0;

		SQLiteDatabase db = getWritableDatabase();

		ContentValues newAsset = new ContentValues();
		newAsset.put(NAME, asset.getName());
		newAsset.put(DESCRIPTION, asset.getDescription());
		newAsset.put(IMAGE_URL, asset.getImageUrl());
		newAsset.put(LOCATION, asset.getLocation());
		newAsset.put(COST, asset.getCost());
		result = db.insert(ASSET_TABLE, null, newAsset);
		db.close();

		return result;
	}

	public ArrayList<Asset> getAllAsset() {
		ArrayList<Asset> allAssets = new ArrayList<Asset>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery(GET_ALL_ASSETS_LIST, null);

		if (cursor.moveToFirst()) {

			do {
				Asset asset = new Asset();
				asset.set_ID(cursor.getString(cursor.getColumnIndex(_ID)));
				asset.setName(cursor.getString(cursor.getColumnIndex(NAME)));
				asset.setDescription(cursor.getString(cursor
						.getColumnIndex(DESCRIPTION)));
				asset.setImageUrl(cursor.getString(cursor
						.getColumnIndex(IMAGE_URL)));
				asset.setLocation(cursor.getString(cursor
						.getColumnIndex(LOCATION)));
				asset.setCost(cursor.getString(cursor.getColumnIndex(COST)));
				allAssets.add(asset);

			} while (cursor.moveToNext());
		}

		return allAssets;

	}

	public int updateAsset(Asset asset) {

		SQLiteDatabase db = getWritableDatabase();

		ContentValues newAsset = new ContentValues();
		newAsset.put(NAME, asset.getName());
		newAsset.put(DESCRIPTION, asset.getDescription());
		newAsset.put(IMAGE_URL, asset.getImageUrl());
		newAsset.put(LOCATION, asset.getLocation());
		newAsset.put(COST, asset.getCost());

		return db.update(ASSET_TABLE, newAsset, _ID + "=?",
			new String[]{asset.get_ID()});
/*
		return db.update(ASSET_TABLE, newAsset, _ID + "=1",
				null);*/



	}

	public void deleteAsset(Asset asset) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(ASSET_TABLE, _ID + " = ?",
				new String[] { String.valueOf(asset.get_ID()) });
		db.close();

	}

	public Asset getAssetByID(String id) {

		SQLiteDatabase db = getReadableDatabase();
		Asset asset = null;

		Cursor cursor = db
				.rawQuery(GET_ASSETS_BY_ID + String.valueOf(id), null);

		if (null != cursor) {

			cursor.moveToFirst();

			asset = new Asset();
			asset.set_ID(cursor.getString(cursor.getColumnIndex(_ID)));
			asset.setName(cursor.getString(cursor.getColumnIndex(NAME)));
			asset.setDescription(cursor.getString(cursor
					.getColumnIndex(DESCRIPTION)));
			asset.setImageUrl(cursor.getString(cursor.getColumnIndex(IMAGE_URL)));
			asset.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
			asset.setCost(cursor.getString(cursor.getColumnIndex(COST)));

		}

		return asset;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		try {
			db.execSQL(CREATE_ASSET_TABLE_STRING);

		} catch (Exception e) {

		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		try {
			db.execSQL(DELETE_ASSET_TABLE_STRING);

		} catch (Exception e) {

		}

	}

}
