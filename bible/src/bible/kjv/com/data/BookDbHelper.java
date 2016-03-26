package bible.kjv.com.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * BookDbHelper is used to access the user database. This database
 * holds information about the last time a chapter is read and
 * highlighted verses.
 *
 */
public class BookDbHelper extends SQLiteOpenHelper {
	
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "UserData.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String DATE_TYPE = " DATETIME";
    
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_BOOKLOG =
        "CREATE TABLE " + DBT.BookLogEntry.TABLE_NAME + " (" +
        		DBT.BookLogEntry._ID + " INTEGER PRIMARY KEY," +
        		DBT.BookLogEntry.COLUMN_BOOKID + INT_TYPE + COMMA_SEP +
        		DBT.BookLogEntry.COLUMN_CHAPTER + INT_TYPE + COMMA_SEP +
        		DBT.BookLogEntry.COLUMN_LASTREAD + DATE_TYPE +
        		" )";
    
    private static final String SQL_CREATE_HIGHLIGHT =
	        "CREATE TABLE " + DBT.BookHighLightEntry.TABLE_NAME + " (" +
	        		DBT.BookHighLightEntry._ID + " INTEGER PRIMARY KEY," +
	        		DBT.BookHighLightEntry.COLUMN_BOOKID + INT_TYPE + COMMA_SEP +
	        		DBT.BookHighLightEntry.COLUMN_CHAPTER + INT_TYPE + COMMA_SEP +
	        		DBT.BookHighLightEntry.COLUMN_VERSE + INT_TYPE + COMMA_SEP +
	        		DBT.BookHighLightEntry.COLUMN_STATE + INT_TYPE + COMMA_SEP +
	        		DBT.BookHighLightEntry.COLUMN_DATE + DATE_TYPE +
	        		" )";
    
    private static final String SQL_CREATE_HIGHLIGHT_temp =
	        "CREATE TABLE " + DBT.BookHighLightEntry.TABLE_NAME + "_tmp (" +
	        		DBT.BookHighLightEntry._ID + " INTEGER PRIMARY KEY," +
	        		DBT.BookHighLightEntry.COLUMN_BOOKID + INT_TYPE + COMMA_SEP +
	        		DBT.BookHighLightEntry.COLUMN_CHAPTER + INT_TYPE + COMMA_SEP +
	        		DBT.BookHighLightEntry.COLUMN_VERSE + INT_TYPE + COMMA_SEP +
	        		DBT.BookHighLightEntry.COLUMN_STATE + INT_TYPE + COMMA_SEP +
	        		DBT.BookHighLightEntry.COLUMN_DATE + DATE_TYPE +
	        		" )";
    
    private static final String SQL_CREATE_READSTATE=
    		"CREATE TABLE "+ DBT.BookReadEntry.TABLE_NAME + " ("+
    				DBT.BookReadEntry._ID + " INTEGER PRIMARY KEY," +
    				DBT.BookReadEntry.COLUMN_BOOKID + INT_TYPE + COMMA_SEP +
    				DBT.BookReadEntry.COLUMN_CHAPTER + INT_TYPE + COMMA_SEP +
    				DBT.BookReadEntry.COLUMN_LASTREAD + DATE_TYPE +
    				" )";
    
    private static final String SQL_CREATE_PROPERTIES=
    		"CREATE TABLE "+ DBT.PropertyEntry.TABLE_NAME + " ("+
    				DBT.PropertyEntry._ID + " INTEGER PRIMARY KEY," +
    				DBT.PropertyEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
    				DBT.PropertyEntry.COLUMN_VALUE + TEXT_TYPE + COMMA_SEP +
    				DBT.PropertyEntry.COLUMN_MODIFIED + DATE_TYPE +
    				" )";
    	
    private static final String SQL_CREATE_DATE = 
    		"ALTER TABLE " +  DBT.BookHighLightEntry.TABLE_NAME +
    		" ADD " + DBT.BookHighLightEntry.COLUMN_DATE + DATE_TYPE;
    

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BOOKLOG);
        db.execSQL(SQL_CREATE_HIGHLIGHT);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       

    	switch (newVersion)
    	{
	    	case 3:
	    		//version 3 adds the highlight table
	    		db.execSQL(SQL_CREATE_HIGHLIGHT);
	    	case 4:
	    		db.execSQL(SQL_CREATE_DATE);
	    	case 5:
	    	case 6:
	    		db.execSQL(SQL_CREATE_HIGHLIGHT_temp);
	    		db.execSQL("insert into Highlight_tmp select * from Highlight");
	    		db.execSQL("drop table Highlight");
	    		db.execSQL("alter table Highlight_tmp Rename to Highlight");
	    	case 7:
	    		db.execSQL(SQL_CREATE_PROPERTIES);
	    		db.execSQL(SQL_CREATE_READSTATE);
	    		
    	}
    }
}