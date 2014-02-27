package com.cameronneale.gogps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.DisplayMetrics;
import android.util.Log;

import com.cameronneale.gogps.model.Place;
import com.cameronneale.gogps.model.Visit;

public class ChangeDB extends SQLiteOpenHelper {
	//データベースの作成
    private static final int DATABASE_VERSION = 32;
    private static final String DATABASE_NAME = "FavePlaces";
    public Resources mres;
    public Resources nres;
    public AssetManager asst;
    public DisplayMetrics disp;
    public Configuration conf;
    public static String rFox;
    public ChangeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mres = context.getResources();
        asst = mres.getAssets();
        disp = mres.getDisplayMetrics();
        conf = mres.getConfiguration();
        nres = new Resources(asst,disp,conf);
        
    }
    
    @SuppressLint("SimpleDateFormat")
	@Override
    public void onCreate(SQLiteDatabase db) {
    	String DB_CREATE_PLACES =
    			"CREATE TABLE places (" +
    			"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
    			"name TEXT, " +
    			"description TEXT, " +
    			"longitude TEXT, " +
    			"latitude TEXT, " +
    			"def INTEGER, " +
    			"color INTEGER )";
			String DB_CREATE_VISITS = 
    			"CREATE TABLE visits (vid INTEGER PRIMARY KEY AUTOINCREMENT, " +
    			"vlongitude TEXT, " +
    			"vlatitude TEXT, " +
    			"date TEXT, " +
    			"memo TEXT, " +
    			"gosa INTEGER, " +
    			"nloc TEXT, " +
    			"vcol INTEGER )";
        db.execSQL(DB_CREATE_PLACES);
        db.execSQL(DB_CREATE_VISITS);

		//初期目的地と履歴
        
        String ADD_NEW = "INSERT INTO places(name, description, latitude, longitude, def, color) VALUES ('"+nres.getString(R.string.redFox)+"', '"+nres.getString(R.string.workLoc)+"', '35.694523', '139.693402', 1, 1)";
        String ADD_NEW2 = "INSERT INTO places(name, description, latitude, longitude, def, color) VALUES ('"+nres.getString(R.string.starbucks)+"', '"+nres.getString(R.string.cafe)+"', '35.6926763','139.6969873', 0, 192)";
        SimpleDateFormat dfd = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        String ADD_NEW3 = "INSERT INTO visits(date, vlatitude, vlongitude, memo, gosa, vcol) VALUES ('"+dfd.format(c.getTime())+" 08:37 AM', '35.694503', '139.693227', '"+nres.getString(R.string.punchIn)+"', 10, 250)";
        String ADD_NEW4 = "INSERT INTO visits(date, vlatitude, vlongitude, memo, gosa, vcol) VALUES ('"+dfd.format(c.getTime())+" 01:30 PM', '35.6920763','139.6969573', '"+nres.getString(R.string.breakTime)+"', 100, 250)";
        String ADD_NEW5 = "INSERT INTO visits(date, vlatitude, vlongitude, memo, gosa, vcol) VALUES ('"+dfd.format(c.getTime())+" 06:10 PM', '35.694416', '139.693339', '"+nres.getString(R.string.punchOut)+"', 20, 250)";
        db.execSQL(ADD_NEW);
        db.execSQL(ADD_NEW2);
        db.execSQL(ADD_NEW3);
        db.execSQL(ADD_NEW4);
        db.execSQL(ADD_NEW5);

    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS places");
		db.execSQL("DROP TABLE IF EXISTS visits");
		this.onCreate(db);
	}
	
	//データベース操作ファンクション
	private static final String TABLE_PLACES = "places";
	private static final String TABLE_VISITS = "visits";
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_DESC = "description";
	private static final String KEY_LONG = "longitude";
	private static final String KEY_LAT = "latitude";
	private static final String KEY_DEF = "def";
	private static final String KEY_COL = "color";
	private static final String KEY_VID = "vid";
	private static final String KEY_VLO = "vlongitude";
	private static final String KEY_VLA = "vlatitude";
	private static final String KEY_DATE = "date";
	private static final String KEY_MEMO = "memo";
	private static final String KEY_GOSA = "gosa";
	private static final String KEY_NLOC = "nloc";
	private static final String KEY_VCOL = "vcol";
	
	
	//場所を追加する
	public void addPlace(Place place){
		Log.d("addPlace",place.toString());
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, place.getName());
		values.put(KEY_DESC, place.getDesc());
		values.put(KEY_LONG, place.getLong());
		values.put(KEY_LAT, place.getLat());
		values.put(KEY_DEF, place.isDefault());
		values.put(KEY_COL, place.getColor());
		db.insert(TABLE_PLACES, null, values);
		
		db.close();
	}
	
	public void addVisit(Visit visit) {
		Log.d("addVisit",visit.toString());
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_VLO, visit.getLongitude());
		values.put(KEY_VLA, visit.getLatitude());
		values.put(KEY_DATE, visit.getDate());
		values.put(KEY_MEMO, visit.getMemo());
		values.put(KEY_GOSA, visit.getGosa());
		values.put(KEY_NLOC, visit.getNearestLocation());
		values.put(KEY_VCOL, visit.getVcol());
		db.insert(TABLE_VISITS, null, values);
		db.close();
	}
	
	//場所を納得
	public Place getPlace(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		
	Cursor cursor = db.query(TABLE_PLACES, null, " id = ?", new String[]{String.valueOf(id)}, null, null, null);
	
	if (cursor!=null) cursor.moveToFirst();
	
	Place place = new Place();
	place.setId(Integer.parseInt(cursor.getString(0)));
	place.setName(cursor.getString(1));
	place.setDesc(cursor.getString(2));
	place.setLong(cursor.getString(3));
	place.setLat(cursor.getString(4));
	place.setDefault(Integer.parseInt(cursor.getString(5)));
	place.setColor(Integer.parseInt(cursor.getString(6)));
	
	Log.d("getPlace("+id+")", place.toString());
	return place;
	}
	
	public Visit getVisit(int vid){
		SQLiteDatabase db = this.getReadableDatabase();
		
	Cursor cursor = db.query(TABLE_VISITS, null, " vid = ?", new String[]{String.valueOf(vid)}, null, null, null);
	
	if (cursor!=null) cursor.moveToFirst();
	
	Visit visit = new Visit();
	visit.setId(Integer.parseInt(cursor.getString(0)));
	visit.setLongitude(cursor.getString(1));
	visit.setLatitude(cursor.getString(2));
	visit.setDate(cursor.getString(3));
	visit.setMemo(cursor.getString(4));
	visit.setGosa(Integer.parseInt(cursor.getString(5)));
	visit.setNearestLocation(cursor.getString(6));
	visit.setVcol(Integer.parseInt(cursor.getString(7)));
	
	Log.d("getVisit("+vid+")", visit.toString());
	return visit;
	}
	
	public List<Place> getAllPlaces() {
		List<Place> places = new LinkedList<Place>();
		
		String query = "SELECT * FROM " + TABLE_PLACES;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Place place = null;
		if (cursor.moveToFirst()) {
			do {
				place = new Place();
				place.setId(Integer.parseInt(cursor.getString(0)));
				place.setName(cursor.getString(1));
				place.setDesc(cursor.getString(2));
				place.setLong(cursor.getString(3));
				place.setLat(cursor.getString(4));
				place.setDefault(Integer.parseInt(cursor.getString(5)));
				place.setColor(Integer.parseInt(cursor.getString(6)));
				
				places.add(place);
			} while (cursor.moveToNext());
		}
		
		Log.d("getAllPlaces()", places.toString());
		
		return places;
	}
	
	public List<Visit> getAllVisits() {
		List<Visit> visits = new LinkedList<Visit>();
		
		String query = "SELECT * FROM " + TABLE_VISITS;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Visit visit = null;
		if (cursor.moveToFirst()) {
			do {
				visit = new Visit();
				visit.setId(Integer.parseInt(cursor.getString(0)));
				visit.setDate(cursor.getString(3));
				visit.setMemo(cursor.getString(4));
				visit.setLongitude(cursor.getString(1));
				visit.setLatitude(cursor.getString(2));
				visit.setGosa(Integer.parseInt(cursor.getString(5)));
				visit.setNearestLocation(cursor.getString(6));
				visit.setVcol(Integer.parseInt(cursor.getString(7)));
				
				visits.add(visit);
			} while (cursor.moveToNext());
		}
		
		Log.d("getAllVisits()", visits.toString());
		
		return visits;
	}
	
	public int updatePlace(Place place){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, place.getName());
		values.put(KEY_DESC, place.getDesc());
		values.put(KEY_LONG, place.getLong());
		values.put(KEY_LAT, place.getLat());
		values.put(KEY_DEF,String.valueOf(place.isDefault()));
		values.put(KEY_COL,String.valueOf(place.getColor()));
		
		int i = db.update(TABLE_PLACES, values, KEY_ID+" = ?", new String[] {String.valueOf(place.getId())});
		
		db.close();
		
		return i;
	}
	
	public int updateVisit(Visit visit){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_DATE, visit.getDate());
		values.put(KEY_MEMO, visit.getMemo());
		values.put(KEY_VLO, String.valueOf(visit.getLongitude()));
		values.put(KEY_VLA, String.valueOf(visit.getLatitude()));
		values.put(KEY_GOSA,visit.getGosa());
		values.put(KEY_NLOC, String.valueOf(visit.getNearestLocation()));
		values.put(KEY_VCOL,visit.getVcol());
		
		int i = db.update(TABLE_VISITS, values, KEY_VID+" = ?", new String[] {String.valueOf(visit.getId())});
		
		db.close();
		
		return i;
	}
	
	public void deletePlace(Place place){
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_PLACES, KEY_ID+" = ?", new String[] {String.valueOf(place.getId())});
		db.close();
		
		Log.d("deletePlace",place.toString());
	}
	
	public void deleteVisit(Visit visit){
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_VISITS, KEY_VID+" = ?", new String[] {String.valueOf(visit.getId())});
		db.close();
		
		Log.d("deleteVisit",visit.toString());
	}
}