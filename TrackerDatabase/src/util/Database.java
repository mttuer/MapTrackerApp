package util;

import interfaces.TrackerDB;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import dataWrappers.GPS;
import dataWrappers.DBMarker;
import dataWrappers.Route;

public class Database implements TrackerDB{
	SQLiteDatabase db;
	static final String DatabaseName = "DB_TRACKER";
	final String tbGPS = "CREATE TABLE TB_GPS_DATA (_ID INTEGER PRIMARY KEY, _UPLOADED BOOLEAN, routeID INTEGER, " +
					"time INTEGER, latitude REAL, longitude REAL)";
	
	final String tbMarker = "CREATE TABLE TB_MARKER_DATA (_ID INTEGER PRIMARY KEY, _UPLOADED BOOLEAN, routeID INTEGER, gpsID INTEGER, " +
	"picPath TEXT, vidPath TEXT, audioPath TEXT, comment TEXT, time INTEGER, longitude "+
	"INTEGER, latitude INTEGER)";

	final String tbRoute = "CREATE TABLE TB_ROUTES (_ID INTEGER PRIMARY KEY, _UPLOADED INTEGER, routeID INTEGER, name TEXT, location TEXT, "+
	"startTime INTEGER, endTime INTEGER, countDatraPoints INTEGER)";
	static final String DATABASE_NAME = "a";
	static final int DATABASE_VERSION = 1;
	static final String DATABASE_CREATE ="";
	Context ctx;
	DatabaseHelper dbHelper;
	
	public Database(Context context){
		this.ctx = context;
	}
	 

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public Database open() throws SQLException {
        dbHelper = new DatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

	 private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DatabaseName, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("TAG", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
	 
	@Override
	public boolean addGPSData(List<GPS> gps, long routeID) {
		if(gps.isEmpty())
			return false;
		
		ContentValues cv = new ContentValues();
		db.beginTransaction();
		
		cv.put("routeID", routeID);
		for(GPS g: gps){
			cv.put("latitude", g.latitude);
			cv.put("longitude", g.longitude);
			cv.put("time", g.time);
			
			g.GPS_ID = db.insert("TB_GPS_DATA", null, cv);
		}
		
		db.setTransactionSuccessful();
		db.endTransaction();
		
		return true;
	}
	
	@Override
	public List<GPS> getGPSData(long routeID) {
		LinkedList<GPS> gps = new LinkedList<GPS>();
		
		Cursor c = db.query(false, "TB_GPS_DATA", new String[]{"latitude","longitude","time"}, 
				"routeID = " + routeID,null, null, null, null, null, null);
		c.moveToFirst();
		c.moveToPrevious();
		
		while(c.moveToNext()){
			GPS g = new GPS();
			g.latitude = c.getDouble(1);
			g.longitude = c.getDouble(2);
			g.time = c.getLong(3);
			g.routeID = routeID;
			gps.add(g);
		}
		
		return gps;
	}

	@Override
	public boolean deleteGPS(int gpsID) {
		return db.delete("TB_GPS_DATA"	, "_ID = " + gpsID, null) == 1;
	}

	@Override
	public boolean deleteRouteGPS(long routeID) {
		return db.delete("TB_GPS_DATA", "routeID = " + routeID, null) > 0;
	}

	@Override
	public long addNewRoute(List<GPS> gps, List<DBMarker> markers, Long timeStart,
			Long timeEnd, String notes, String routeName, String location) {
		
		ContentValues cv = new ContentValues();
		cv.put("timeStart", timeStart);
		cv.put("timeEnd", timeEnd);
		if(routeName != null)
			cv.put("name", routeName);
		if(location != null)
			cv.put("location", location);
		cv.put("countDataPoints", gps.size());
		long routeID = db.insert("TB_ROUTES", null, cv);
		
		this.addGPSData(gps, routeID);
		this.addMarkers(markers, routeID);
		
		return routeID;
	}

	@Override
	public boolean editRoute(long routeID, List<GPS> gps, List<DBMarker> markers,
			Long timeStart, Long timeEnd, String notes, String routeName, String location) {
		boolean truth = true;
		
		ContentValues cv = new ContentValues();
		if(timeStart != null)
			cv.put("timeStart", timeStart);
		if(timeEnd != null)
			cv.put("timeEnd", timeEnd);
		if(routeName != null)
			cv.put("name", routeName);
		if(location != null)
			cv.put("location", location);
		if(gps != null)
			cv.put("countDataPoints", gps.size());
		if(cv.size() > 0)
			truth &= db.update("TB_ROUTES", cv, null, null) > 0;
		truth &= this.addGPSData(gps, routeID);
		truth &= this.addMarkers(markers,routeID);
		
		return truth;
	}

	@Override
	public boolean deleteRoute(long routeID) {

		boolean ret = db.delete("TB_ROUTES", "_ID = " + routeID, null) > 0;
		if(ret){
			db.delete("TB_GPS_DATA", "routeID = " + routeID, null);
			db.delete("TB_MARKER_DATA", "routeID = " + routeID, null);
		}
		return ret;
	}

	@Override
	public List<Route> getAllRoutes() {
		LinkedList<Route> routes = new LinkedList<Route>();
		
		Cursor c = db.query(false, "TB_ROUTES", new String[]{"routeID","timeStart",
				"timeEnd","notes","routeName","location","countDataPoints"},null 
				,null, null, null, null, null, null);
		c.moveToFirst();
		c.moveToPrevious();
		
		while(c.moveToNext()){
			Route r = new Route();
			r.routeID = c.getLong(1);
			r.timeStart = c.getLong(2);
			r.timeEnd = c.getLong(3);
			r.notes = c.getString(4);
			r.routeName = c.getString(5);
			r.location = c.getString(6);
			r.countDataPoints = c.getInt(7);
			routes.add(r);
		}
		
		return routes;
	}

	@Override
	public Route getRoute(long routeID) {
		Cursor c = db.query(false, "TB_ROUTES", new String[]{"routeID","timeStart",
				"timeEnd","notes","routeName","location","countDataPoints"},
				"_ID = " + routeID, null, null, null, null, null, null);
		c.moveToFirst();
		Route r = new Route();
		r.routeID = c.getLong(1);
		r.timeStart = c.getLong(2);
		r.timeEnd = c.getLong(3);
		r.notes = c.getString(4);
		r.routeName = c.getString(5);
		r.location = c.getString(6);
		r.countDataPoints = c.getInt(7);
		
		return r;
	}

	@Override
	public boolean addMarkers(List<DBMarker> markers, long routeID) {
		if(markers.isEmpty())
			return false;
		
		ContentValues cv = new ContentValues();
		db.beginTransaction();
		
		cv.put("routeID", routeID);
		for(DBMarker m: markers){
			cv.put("time", m.timeStamp);
			cv.put("gpsID", m.gps.GPS_ID);
			cv.put("longitude", m.gps.longitude);
			cv.put("latitude", m.gps.latitude);
			cv.put("text", m.text);
			if(m.hasVid())
				cv.put("vidPath", m.videoLink);
			if(m.hasAudio()){
				cv.put("audioPath", m.audioLink);
			}
			if(m.hasPic()){
				cv.put("picPath", m.pictureLink);
			}
			
			m.markerID = db.insert("TB_ROUTES", null, cv);
		}
		
		db.setTransactionSuccessful();
		db.endTransaction();
		
		return true;
	}

	@Override
	public List<DBMarker> getMarkers(long routeID) {
		LinkedList<DBMarker> markers = new LinkedList<DBMarker>();
		
		Cursor c = db.query(false, "TB_MARKER_DATA", new String[]{"_ID","routeID","pictureLink","videoLink","audioLink",
				"text","time","longitude","latitude","gpsID"}, 
				"routeID = " + routeID, null, null, null, null, null, null);
		c.moveToFirst();
		c.moveToPrevious();
		
		while(c.moveToNext()){
			DBMarker m = new DBMarker();
			GPS gps = new GPS();
			m.markerID = c.getLong(1);
			m.routeID = c.getLong(2);
			m.pictureLink = c.getString(3);
			m.videoLink = c.getString(4);
			m.audioLink = c.getString(5);
			m.text = c.getString(6);
			m.timeStamp = c.getLong(7);
			gps.latitude = c.getDouble(8);
			gps.longitude = c.getDouble(9);
			gps.GPS_ID = c.getLong(10);
			m.gps = gps;
			markers.add(m);
		}
		
		return markers;
	}

	@Override
	public DBMarker getMarker(int markerID) {
		
		Cursor c = db.query(false, "TB_MARKER_DATA", new String[]{"_ID","routeID","pictureLink","videoLink","audioLink",
				"text","time","longitude","latitude","gpsID"}, 
				"_ID = " + markerID, null, null, null, null, null, null);
		c.moveToFirst();
		DBMarker m = new DBMarker();
		GPS gps = new GPS();
		m.markerID = c.getLong(1);
		m.routeID = c.getLong(2);
		m.pictureLink = c.getString(3);
		m.videoLink = c.getString(4);
		m.audioLink = c.getString(5);
		m.text = c.getString(6);
		m.timeStamp = c.getLong(7);
		gps.latitude = c.getDouble(8);
		gps.longitude = c.getDouble(9);
		gps.GPS_ID = c.getLong(10);
		m.gps = gps;
		
		
		return m;
	}

	@Override
	public boolean deleteMarker(int markerID) {
		return db.delete("TB_MARKER_DATA"	, "_ID = " + markerID, null) == 1;
	}

	@Override
	public boolean deleteMarkers(long routeID) {
		return db.delete("TB_MARKER_DATA"	, "routeID = " + routeID, null) == 1;
	}


	@Override
	public boolean addMarkerPicture(int markerID, String mediaLink) {
		ContentValues cv = new ContentValues();
		cv.put("picPath", mediaLink);
		return db.update("TB_MARKER_DATA", cv, "_ID = " + markerID, null) == 1;
	}

	@Override
	public boolean addMarkerText(int markerID, String text) {

		ContentValues cv = new ContentValues();
		cv.put("text", text);
		return db.update("TB_MARKER_DATA", cv, "_ID = " + markerID, null) == 1;
	}

	@Override
	public boolean addMarkerVideo(int markerID, String mediaLink) {

		ContentValues cv = new ContentValues();
		cv.put("vidPath", mediaLink);
		return db.update("TB_MARKER_DATA", cv, "_ID = " + markerID, null) == 1;
	}

	@Override
	public boolean addMarkerAudio(int markerID, String mediaLink) {

		ContentValues cv = new ContentValues();
		cv.put("audioPath", mediaLink);
		return db.update("TB_MARKER_DATA", cv, "_ID = " + markerID, null) == 1;
	}

}
