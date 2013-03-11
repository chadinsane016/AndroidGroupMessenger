package edu.buffalo.cse.cse486586.groupmessenger.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

public class GroupMessengerProvider extends ContentProvider 
{

	// URI for this provider
	
	public static final String contentUri="content://edu.buffalo.cse.cse486586.groupmessenger.provider";
	public static HashMap<String, String> msgProjMap;	
	String TAG="Inserted into database";
	//database for groupMessenger
	private SQLiteDatabase groupMessengerDB;
	groupMessengerDBHelper createDatabase;
	private static final String dbName = "GroupMessengerDB";
	private static final int dbVersion = 10;
	private static final String tableName = "GroupMessengerTable";
	private static final int messages = 1;
	
	
	//column names
	public static String msgId="msgId";
	public static String key="key";
	public static String value="value";
	
	
	private static UriMatcher uriMatcher;
	
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(contentUri, tableName, 1);
	/*	msgProjMap = new HashMap<String, String>();
		msgProjMap.put(key, key);
		msgProjMap.put(value, value);
		*/
	}
	
	
	private static final String createTable = "CREATE TABLE " + tableName + "("

			
			
			+key + " LONGTEXT," + value	+ " LONGTEXT );";
	
	// DB Helper class
	
	private static class groupMessengerDBHelper extends SQLiteOpenHelper
	{

		public groupMessengerDBHelper(Context context) {
			super(context, dbName, null, dbVersion);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(createTable);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + tableName);
			onCreate(db);
			
		}
		
	
	}
	
	
	
		
	
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri iuri, ContentValues values) {
				// TODO Auto-generated method stub
		// TODO Auto-generated method stub
			
		SQLiteDatabase writeDB= createDatabase.getWritableDatabase();
		
		//if (uriMatcher.match(iuri) != 1)
		//			throw new IllegalArgumentException(contentUri);
				
				
			

				
				long rowID = writeDB.insert(tableName, null, values);
				if (rowID > 0) {
					Uri uri = ContentUris.withAppendedId(Uri.parse(contentUri), rowID);
					getContext().getContentResolver().notifyChange(uri,null);
					Log.v(TAG,  "Inserted");
					return uri;
				}
				throw new SQLException("Failed to insert into " + iuri);
		
	
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Context context = getContext();
		context.deleteDatabase(dbName);
		createDatabase= new groupMessengerDBHelper(context);
			
		//groupMessengerDB= createDatabase.getWritableDatabase();
		//return ( groupMessengerDB == null) ? false : true;
	return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection,String selection, String[] selectionArgs,String sort) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(tableName);
		
		SQLiteDatabase readDB= createDatabase.getReadableDatabase();
		String whereclause= key +"=" + "'"+selection+"'";
		
		
		// execute the query against the database
		Cursor cursor = qb.query(readDB, projection,whereclause,selectionArgs, null, null, sort);
		
		// notify the content resolver about the change
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
		
	}
		
	

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
