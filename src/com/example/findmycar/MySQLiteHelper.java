package com.example.findmycar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
  public static final String TABLE_LOCATION = "locations";
  public static final String COLUMN_ID = "ID";
  public static final String COLUMN_LATITUDE = "lattitude";
  public static final String COLUMN_LONGITUDE = "longitude";
  

  private static final String DATABASE_NAME = "location.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table " +TABLE_LOCATION + " (" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_LATITUDE + " REAL, " + COLUMN_LONGITUDE + " REAL);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS ");
    onCreate(db);
  }
  
  public long createParkedLocation(ParkedLocation pl)
  {
	  SQLiteDatabase db = this.getWritableDatabase();
	  ContentValues values = new ContentValues();
	  values.put(COLUMN_LATITUDE, pl.getLattitude());
	  values.put(COLUMN_LONGITUDE, pl.getLongitude());
	  
	  long location_id = db.insert(TABLE_LOCATION, null, values);
	  
	  return location_id;
  }
  
  public ParkedLocation getParkedLocation(long location_id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    String selectQuery = "SELECT  * FROM " + TABLE_LOCATION + " WHERE "
	            + COLUMN_ID + " = " + location_id;
	    
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    if (c != null)
	        c.moveToFirst();
	 
	    ParkedLocation pl = new ParkedLocation(0,0);
	    pl.setID(c.getInt(c.getColumnIndex(COLUMN_ID)));
	    pl.setLatitude((c.getDouble(c.getColumnIndex(COLUMN_LATITUDE))));
	    pl.setLongitude(c.getDouble(c.getColumnIndex(COLUMN_LONGITUDE)));
	 
	    return pl;
	}
  
  public int updateParkedLocation(ParkedLocation pl) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
		values.put(COLUMN_LATITUDE, pl.getLattitude());
		values.put(COLUMN_LONGITUDE, pl.getLongitude());
	 
	    // updating row
	    return db.update(TABLE_LOCATION, values, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(pl.getID()) });
	}
  
  public int getParkedLocationSize() {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    String selectQuery = "SELECT  * FROM " + TABLE_LOCATION;
	    
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    if (c != null)
	        c.moveToFirst();
	    else
	    	return 0;
	    
	    return c.getCount();
	}
  
  public void closeDB() {
      SQLiteDatabase db = this.getReadableDatabase();
      if (db != null && db.isOpen())
          db.close();
  } 
} 
