package bible.kjv.com.data;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bible.kjv.com.util.Trace;
import android.util.SparseArray;
import bible.kjv.com.BookIf;

/**
 * 
 * BookDatabase implements IBookData so last read and
 * highlighted verses can be accessed by ChapterLoader. 
 *
 */
public class BookDatabase implements IUserData {
	
	private static final String TAG = "BookDatabase";
	public final static int BOOK_COUNT = 66;
	public final static int MAX_CHAPTER_COUNT = 150;
	private final int INVALID_CACHE = -1;
	private FontSize mSize = FontSize.Small;
	private DataBaseHelper mDbHelper;
	private int[] mChapterCountCache;
	private LogFilter mLogfilter = LogFilter.All;
	
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public BookDatabase(Context context) {
    	InitializeDb(context);
    }
    
    /**
     * Initializes the database helper.
     * @param context
     */
    public void InitializeDb(Context context) {
    	mDbHelper = new DataBaseHelper(context); 
    	mDbHelper.InitializeDatabase();
    	InitCache();
    }
	
	/**
	 * Gets a property 
	 * @param name
	 * @return
	 */
	@Override
	public String GetProperty(String name) {
		return "";
	}
	
	/**
	 * Sets a property
	 * @param name
	 * @param Value
	 */
	@Override
	public void SetProperty(String name, String Value){
		
	}
	
	@Override
	public ArrayList<LogEntry> GetLog(int bookIndex) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			DBT.BookLogEntry._ID,
    			DBT.BookLogEntry.COLUMN_BOOKID,
    			DBT.BookLogEntry.COLUMN_CHAPTER,
    			DBT.BookLogEntry.COLUMN_LASTREAD,
    			
    	    };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			DBT.BookLogEntry.COLUMN_LASTREAD + " desc, " +
    			DBT.BookLogEntry.COLUMN_BOOKID + ", " +
    			DBT.BookLogEntry.COLUMN_CHAPTER;

    	String selection ="datetime(timestamp / 1000, 'unixepoch') > date('now', ?) ";
    	
    	//filters by book index
    	if(bookIndex != ALL_BOOKS) {
    		selection += " and " + DBT.BookLogEntry.COLUMN_BOOKID + " = " + bookIndex;
    	}
    	
    	String[] args = new String[]{ LogFilter.QueryArg(mLogfilter) };
    	Cursor cursor = null;
    	try
    	{	
			cursor = db.query(
					DBT.BookLogEntry.TABLE_NAME,  			  // The table to query
					projection,                               // The columns to return
					selection ,                               // The columns for the WHERE clause
					args,	   			                      // The values for the WHERE clause
					null,                                     // don't group the rows
					null,                                     // don't filter by row groups
					sortOrder                                 // The sort order
					);
			
			ArrayList<LogEntry> log = new ArrayList<LogEntry>(cursor.getCount());
			
			while(cursor.moveToNext())
			{
				LogEntry entry = new LogEntry();
				
				entry.BookIndex = cursor.getInt(
					    cursor.getColumnIndexOrThrow(DBT.BookLogEntry.COLUMN_BOOKID)
					);
				
				entry.Chapter = cursor.getInt(
					    cursor.getColumnIndexOrThrow(DBT.BookLogEntry.COLUMN_CHAPTER)
					);
				
				entry.TimeStamp = cursor.getLong(
					    cursor.getColumnIndexOrThrow(DBT.BookLogEntry.COLUMN_LASTREAD)
					);
				
				log.add(entry);
			}
		
			return log;
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}
    
    
    @Override
	public FontSize GetFontSize() {
		return mSize;
	}

	@Override
	public void SetFontSize(FontSize size) {
		//Trace.d(TAG, "Size is " + size);
		mSize = size;
	}

	@Override
	public String GetBookId(int bookIndex) {
		return BookNameManager.GetName(bookIndex);
	}

	@Override
	public HighLightState GetHighLight(int bookIndex, int chapter, int verse) {
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			DBT.BookHighLightEntry._ID,
    			DBT.BookHighLightEntry.COLUMN_STATE,
    	    };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			DBT.BookHighLightEntry.COLUMN_STATE + " DESC";

    	String selection = DBT.BookHighLightEntry.COLUMN_BOOKID + " = ? and " +
    						DBT.BookHighLightEntry.COLUMN_CHAPTER + " = ? and " +
    						DBT.BookHighLightEntry.COLUMN_VERSE + " = ?" ;
    	
    	String[] selectionArgs=  new String[] { "" + bookIndex, "" + chapter , "" + verse};
    	
    	Cursor cursor = null;
    	try 
    	{
			cursor = db.query(
					DBT.BookHighLightEntry.TABLE_NAME,  				  // The table to query
					projection,                               // The columns to return
					selection ,                               // The columns for the WHERE clause
					selectionArgs,	   		                  // The values for the WHERE clause
					null,                                     // don't group the rows
					null,                                     // don't filter by row groups
					sortOrder                                 // The sort order
					);
			
			if(cursor.moveToFirst())
			{
				int state = cursor.getInt(
					    cursor.getColumnIndexOrThrow(DBT.BookHighLightEntry.COLUMN_STATE)
					);
				
				return HighLightState.FromInteger(state);
			}
			else
			{
				return null;
			}
    	}
    	finally
    	{
    		if(cursor != null)
    			cursor.close();
    	}
	}
	
	@Override
	public SparseArray<HighLightState> GetAllHighLights(int bookIndex, int chapter) {			
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			DBT.BookHighLightEntry._ID,
    			DBT.BookHighLightEntry.COLUMN_STATE,
    			DBT.BookHighLightEntry.COLUMN_VERSE
    	    };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			DBT.BookHighLightEntry.COLUMN_VERSE + " DESC";

    	String selection = DBT.BookHighLightEntry.COLUMN_BOOKID + " = ? and " +
    						DBT.BookHighLightEntry.COLUMN_CHAPTER + " = ? ";
    	
    	String[] selectionArgs=  new String[] { "" + bookIndex, "" + chapter};
    	
    	Cursor cursor = null;
    	try
    	{	
			cursor = db.query(
					DBT.BookHighLightEntry.TABLE_NAME,  	  // The table to query
					projection,                               // The columns to return
					selection ,                               // The columns for the WHERE clause
					selectionArgs,	   		                  // The values for the WHERE clause
					null,                                     // don't group the rows
					null,                                     // don't filter by row groups
					sortOrder                                 // The sort order
					);
			
			SparseArray<HighLightState> states = new SparseArray<HighLightState>(cursor.getCount());
			
			while(cursor.moveToNext())
			{
				int verse = cursor.getInt(
					    cursor.getColumnIndexOrThrow(DBT.BookHighLightEntry.COLUMN_VERSE)
					);
				
				int state = cursor.getInt(
					    cursor.getColumnIndexOrThrow(DBT.BookHighLightEntry.COLUMN_STATE)
					);
				
				states.append(verse, HighLightState.FromInteger(state));
			}
		
			return states;
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}
		
	public int GetHighLightCount(HighLightState filter) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			"count("+ DBT.BookHighLightEntry.COLUMN_STATE +") "    			
    	    };

    	String selection;
    	if (filter == HighLightState.All) 
    		selection = DBT.BookHighLightEntry.COLUMN_STATE + " <> 0";
    	else
    		selection = DBT.BookHighLightEntry.COLUMN_STATE + " <> 0 AND " + 
    					DBT.BookHighLightEntry.COLUMN_STATE + " = " + 
    					HighLightState.ToInteger(filter);
    	
    	Cursor cursor = null;
    	try
    	{	
			cursor = db.query(
					DBT.BookHighLightEntry.TABLE_NAME,  				  // The table to query
					projection,                               // The columns to return
					selection ,                               // The columns for the WHERE clause
					null,	   		                          // The values for the WHERE clause
					null,                                     // don't group the rows
					null,                                     // don't filter by row groups
					null		                              // The sort order
					);
			
			int count = 0;
			while(cursor.moveToNext())
			{	
				count = cursor.getInt(1);
			}
			return count;
			
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}

	/**
	 * Used to get a list of all highlights in the whole bible.
	 * @return
	 */
	@Override
	public ArrayList<HighLight> GetHighLights(
			HighLightState filter, HighLightSort sort, int bookIndex)
	{
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			DBT.BookHighLightEntry._ID,
    			DBT.BookHighLightEntry.COLUMN_STATE,
    			DBT.BookHighLightEntry.COLUMN_BOOKID,
    			DBT.BookHighLightEntry.COLUMN_CHAPTER,
    			DBT.BookHighLightEntry.COLUMN_VERSE,
    			DBT.BookHighLightEntry.COLUMN_DATE,
    			
    	    };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			DBT.BookHighLightEntry.COLUMN_BOOKID + ", " +
    			DBT.BookHighLightEntry.COLUMN_CHAPTER + ", " +
    			DBT.BookHighLightEntry.COLUMN_VERSE;
    	
    	if(sort == HighLightSort.Date) {
    		sortOrder =
				DBT.BookHighLightEntry.COLUMN_DATE + " DESC, " +
				DBT.BookHighLightEntry.COLUMN_BOOKID + ", " +
    			DBT.BookHighLightEntry.COLUMN_CHAPTER + ", " +
	    		DBT.BookHighLightEntry.COLUMN_VERSE;
    	}

    	String selection;
    	
    	//filter by state
    	if (filter == HighLightState.All) 
    		selection = DBT.BookHighLightEntry.COLUMN_STATE + " <> 0";
    	else
    		selection = DBT.BookHighLightEntry.COLUMN_STATE + " <> 0 AND " + 
    					DBT.BookHighLightEntry.COLUMN_STATE + " = " + 
    					HighLightState.ToInteger(filter);
    	
    	//filters by book index
    	if(bookIndex != ALL_BOOKS) {
    		selection += " AND " + DBT.BookHighLightEntry.COLUMN_BOOKID + " = " + bookIndex;
    	}
    	
    	Cursor cursor = null;
    	try
    	{	
			cursor = db.query(
					DBT.BookHighLightEntry.TABLE_NAME,  				  // The table to query
					projection,                               // The columns to return
					selection ,                               // The columns for the WHERE clause
					null,	   		                          // The values for the WHERE clause
					null,                                     // don't group the rows
					null,                                     // don't filter by row groups
					sortOrder                                 // The sort order
					);
			
			ArrayList<HighLight> states = new ArrayList<HighLight>(cursor.getCount());
			
			while(cursor.moveToNext())
			{
				HighLight highLight = new HighLight();
				
				highLight.BookIndex = cursor.getInt(
					    cursor.getColumnIndexOrThrow(DBT.BookHighLightEntry.COLUMN_BOOKID)
					);
				
				highLight.Chapter = cursor.getInt(
					    cursor.getColumnIndexOrThrow(DBT.BookHighLightEntry.COLUMN_CHAPTER)
					);
				
				highLight.Verse = cursor.getInt(
					    cursor.getColumnIndexOrThrow(DBT.BookHighLightEntry.COLUMN_VERSE)
					);
				
				highLight.TimeStamp = cursor.getLong(
					    cursor.getColumnIndexOrThrow(DBT.BookHighLightEntry.COLUMN_DATE)
					);
				
				int state = cursor.getInt(
					    cursor.getColumnIndexOrThrow(DBT.BookHighLightEntry.COLUMN_STATE)
					);
				highLight.State = HighLightState.FromInteger(state);				
				
				states.add(highLight);
			}
		
			return states;
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}

	@Override
	public void SetHighlight(int bookIndex, int chapter, int verse, HighLightState state) {
		
		// Gets the data repository in write mode
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();

    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	
    	values.put(DBT.BookHighLightEntry.COLUMN_BOOKID, bookIndex);
    	values.put(DBT.BookHighLightEntry.COLUMN_CHAPTER, chapter);
    	values.put(DBT.BookHighLightEntry.COLUMN_VERSE, verse);
    	values.put(DBT.BookHighLightEntry.COLUMN_STATE, HighLightState.ToInteger(state));
    	
    	HighLightState currentState = GetHighLight(bookIndex, chapter, verse);
    	
    	if(currentState == null)
    	{
    		//set the date once on insert
    		values.put(DBT.BookHighLightEntry.COLUMN_DATE, (new GregorianCalendar()).getTimeInMillis());
    		
    		// Insert the new row, returning the primary key value of the new row
    		db.insert(
    				DBT.BookHighLightEntry.TABLE_NAME,
    				DBT.BookHighLightEntry.COLUMN_NAME_NULLABLE,
    				values);
    	}
    	else
    	{
    		String selection = DBT.BookHighLightEntry.COLUMN_BOOKID + " = ? and " +
					DBT.BookHighLightEntry.COLUMN_CHAPTER + " = ? and " +
					DBT.BookHighLightEntry.COLUMN_VERSE + " = ?" ;
    		
    		String[] selectionArgs=  new String[] {
    				String.valueOf(bookIndex),
    				String.valueOf(chapter) ,
    				String.valueOf(verse)
    				};
    		
    		db.update(
    				DBT.BookHighLightEntry.TABLE_NAME,
    				values,
    				selection,
    				selectionArgs);
    	}
	}

	@Override
	public void SetLastReadDate(int bookIndex, int chapter) {
		SetLastReadDate(bookIndex, chapter, (new GregorianCalendar()).getTimeInMillis());
	}

	@Override
	public void ResetLastReadDate(int bookIndex, int chapter) {
		SetLastReadDate(bookIndex, chapter, 0);
	}
	
	@Override
	public void ResetLastReadDate(int bookIndex) {
		SetLastReadDateForAll(bookIndex);
	}
	
	@Override
	public int GetChaptersRead(int bookIndex) {
		int count = mChapterCountCache[bookIndex];
		if( count != INVALID_CACHE)
			return count;
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			DBT.BookReadEntry._ID,
    			"COUNT (" + DBT.BookReadEntry.COLUMN_LASTREAD + ")",
    	    };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			DBT.BookReadEntry.COLUMN_LASTREAD + " DESC";

    	String selection = DBT.BookReadEntry.COLUMN_BOOKID + " = ? and "+
    					   DBT.BookReadEntry.COLUMN_LASTREAD +" > 0 ";    					
    	
    	String[] selectionArgs=  new String[] { "" + bookIndex };
    	
    	Cursor cursor = null;
    	try
    	{
			cursor = db.query(
					DBT.BookReadEntry.TABLE_NAME,  				  // The table to query
					projection,                               // The columns to return
					selection ,                               // The columns for the WHERE clause
					selectionArgs,	   		                  // The values for the WHERE clause
					null,                                     // don't group the rows
					null,                                     // don't filter by row groups
					sortOrder                                 // The sort order
					);
			
			if(cursor.moveToFirst())
			{
				count = cursor.getInt(1);
				//save cache
				mChapterCountCache[bookIndex] = count;
				return count;
			}
			else
			{
				return 0;
			}
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}
	
	public float GetPercentReadFromLog(int bookIndex) {

		int count = 0;
		float percent = 0;
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	String query = "select distinct a.chapter, b.chapterCount total from booklogs a, books b  " +
    			"where datetime(a.timestamp / 1000, 'unixepoch') > date('now', ?) and " +
    			"a.bookid = ? and a.bookid = b._id";
    	
    	String[] args = new String[]{ 
    			LogFilter.QueryArg(mLogfilter), 
    			Integer.toString(bookIndex)
    			};
    	
    	Cursor cursor = null;
    	
    	try
    	{
			cursor = db.rawQuery(query, args);
			
			if(cursor.moveToFirst()) {
				count = cursor.getCount();
			
				int total = cursor.getInt(
					cursor.getColumnIndexOrThrow("total"));
			
				percent = ((float)count/(float)total);
			}
			return percent;
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}
	
	public float GetPercentReadFromLog() {
		
		int count = 0;
		int total = BookContents.TOTAL_CHAPTER_COUNT;
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	String query = "select distinct bookid, chapter from booklogs " +
    			"where datetime(timestamp / 1000, 'unixepoch') > date('now', ?) ";
    	
    	String[] args = new String[]{ LogFilter.QueryArg(mLogfilter)};
    	Cursor cursor = null;
    	try
    	{
			cursor = db.rawQuery(query, args);
			count = cursor.getCount();
			float percent = ((float)count/(float)total);
			return percent;
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}

	private boolean HasLastReadValue(int bookIndex) {
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
				
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			DBT.BookReadEntry._ID,
    			DBT.BookReadEntry.COLUMN_LASTREAD,
    	    };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			DBT.BookReadEntry.COLUMN_LASTREAD + " DESC";

    	String selection = DBT.BookReadEntry.COLUMN_BOOKID + " = ? ";
    	
    	String[] selectionArgs=  new String[] { Integer.toString(bookIndex) };
    	
    	Cursor cursor = null;
    	try
    	{
			cursor = db.query(
					DBT.BookReadEntry.TABLE_NAME,  				  // The table to query
					projection,                               // The columns to return
					selection ,                               // The columns for the WHERE clause
					selectionArgs,	   		                  // The values for the WHERE clause
					null,                                     // don't group the rows
					null,                                     // don't filter by row groups
					sortOrder                                 // The sort order
					);
			
			if(cursor.moveToFirst())
				return true;
			else
				return false;
		}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}
	
	private void SetLastReadDate(int bookIndex, int chapter, long timeInMs) {
		
		if(timeInMs != 0)
			AddLogEntry(bookIndex, chapter, timeInMs);
		
		// Gets the data repository in write mode
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();

    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	
    	values.put(DBT.BookReadEntry.COLUMN_BOOKID, bookIndex);
    	values.put(DBT.BookReadEntry.COLUMN_CHAPTER, chapter);
    	values.put(DBT.BookReadEntry.COLUMN_LASTREAD, timeInMs);

    	if(!HasLastReadValue(bookIndex))
    	{
    		// Insert the new row, returning the primary key value of the new row
    		db.insert(
    			DBT.BookReadEntry.TABLE_NAME,
    			DBT.BookReadEntry.COLUMN_NAME_NULLABLE,
    	        values);
    	}
    	else
    	{
    		// Insert the new row, returning the primary key value of the new row
    		db.update(
    			DBT.BookReadEntry.TABLE_NAME,
    	        values,
    	        DBT.BookReadEntry.COLUMN_BOOKID + "=?",
    	        new String[]{Integer.toString(bookIndex)});
    	}
    	
    	//reset the last read date if this is the last chapter
    	//boolean[] books = GetChaptersReadList(bookIndex);
    	//if(books != null && books[books.length - 1])
    	//	ResetLastReadDate(bookIndex);
    	
    	//reset the cache
    	mChapterCountCache[bookIndex] = INVALID_CACHE;
	}
	
    private void AddLogEntry(int bookIndex, int chapter, long timeInMs) {
		
		// Gets the data repository in write mode
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();

    	// Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	
    	values.put(DBT.BookLogEntry.COLUMN_BOOKID, bookIndex);
    	values.put(DBT.BookLogEntry.COLUMN_CHAPTER, chapter);
    	values.put(DBT.BookLogEntry.COLUMN_LASTREAD, timeInMs);

		// Insert the new row, returning the primary key value of the new row
		db.insert(
			DBT.BookLogEntry.TABLE_NAME,
			DBT.BookLogEntry.COLUMN_NAME_NULLABLE,
	        values);
	}
	
	/**
	 * Used to reset the last read for all chapters. 
	 * @param bookIndex
	 */
	private void SetLastReadDateForAll(int bookIndex) {
    	
    	// Gets the data repository in write mode
    	SQLiteDatabase db = mDbHelper.getWritableDatabase();
    	
    	db.delete(
    		DBT.BookReadEntry.TABLE_NAME,
    		DBT.BookReadEntry.COLUMN_BOOKID + "= ?",
    		new String[] { Integer.toString(bookIndex) });
    	
    	//reset the cache
    	mChapterCountCache[bookIndex] = INVALID_CACHE;
    	
	}
	
	
	private void InitCache() {
		
		mChapterCountCache = new int[BOOK_COUNT];
		for(int i = 0; i < BOOK_COUNT; i++)
			mChapterCountCache[i] = 0;
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			DBT.BookReadEntry._ID,
    			DBT.BookReadEntry.COLUMN_BOOKID,
    			DBT.BookReadEntry.COLUMN_LASTREAD,
    	    };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			DBT.BookReadEntry.COLUMN_LASTREAD + " DESC";

    	String selection = DBT.BookReadEntry.COLUMN_LASTREAD + " > 0 ";    					
    	
    	Cursor cursor = null;
    	try
    	{
			cursor = db.query(
					DBT.BookReadEntry.TABLE_NAME,  				  // The table to query
					projection,                               // The columns to return
					selection,                                // The columns for the WHERE clause
					null,	  		 		                  // The values for the WHERE clause
					null,                                     // don't group the rows
					null,                                     // don't filter by row groups
					sortOrder                                 // The sort order
					);
			
			while(cursor.moveToNext())
			{
				int BookId = cursor.getInt(1);
				
				//each iteration is a chapter read in a book.
				mChapterCountCache[BookId]++;
			}
			
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}		
	}
	
	@Override
	public LogEntry GetNextBookChapter() { 

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		//create a set of all books
		HashSet<Integer> booksInactive = new HashSet<Integer>();
		for(int i =0; i < BOOK_COUNT; i++) {
			booksInactive.add(i);
		}
		
		LogEntry entry = new LogEntry();
		Cursor cursor = null;
    	try
    	{
    		String query="select bookid, chapter from readstate " +
    				     "group by bookid having max(modify) order by modify";
    		
			cursor = db.rawQuery(query, null);
			int selectedBookIndex = -1;
			int selectedChapter = -1;
			
			//Find the next chapter of an active book
			while(cursor.moveToNext())
			{
				int bookIndex = cursor.getInt(
						cursor.getColumnIndexOrThrow("bookId"));
				int chapter = cursor.getInt(
						cursor.getColumnIndexOrThrow("chapter"));
				
				int chapterCount = BookIf.GetChapterCount(bookIndex);
				
				//if the active book is not finished select it.
				if(chapter < chapterCount - 1)
				{
					Trace.d(TAG, "Selecting... last ch: " + chapter + ", ch count: " + chapterCount);
					selectedBookIndex = bookIndex;
					selectedChapter= chapter + 1;
					break;
				} 
				else
				{
					if(cursor.isLast() && bookIndex < BOOK_COUNT - 1){
						selectedBookIndex = bookIndex + 1;
						selectedChapter= 0;
						Trace.d(TAG, "selecting next book " + bookIndex);
					} else {
						Trace.d(TAG, "skipping " + bookIndex);
						booksInactive.remove(bookIndex);
					}
				}
			}
			
			//all active books read. Read the next book first chapter.
			if(selectedBookIndex == -1 && selectedChapter == -1)
			{
				 Iterator<Integer> i = booksInactive.iterator();
				 for(i = booksInactive.iterator(); i.hasNext(); )
				 {
					 selectedBookIndex =  i.next();
					 selectedChapter = 0; 
					 Trace.d(TAG, "Selecting next book " + selectedBookIndex); 
					 break;
				 }
			}
			
			//Read everything goto the first book first chapter
			if(selectedBookIndex == -1 && selectedChapter == -1)
			{
				 selectedBookIndex =  0;
				 selectedChapter = 0;
				 Trace.d(TAG, "Selecting First book " + selectedBookIndex);
			}
			
			//set the entry
			entry.BookIndex = selectedBookIndex;
			entry.Chapter = selectedChapter; //Make sure the chapter is one based
			
			return entry;
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}

	@Override
	public boolean[] GetBooksRead() {
		
		boolean[] readBooks = new boolean[BOOK_COUNT];
		
		//assume no books read
		for(int i = 0; i < BOOK_COUNT; i++)
			readBooks[i] = false;
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = null;
    	try
    	{
    		String query="select bookid from booklogs a, books b where a.bookid = b._id " +
    					 "group by a.bookid " +
    					 "having count(distinct a.chapter) == b.chapterCount";
    		
			cursor = db.rawQuery(query, null);
			
			
			//Find the next chapter of an active book
			while( cursor.moveToNext())
			{
				
				int bookIndex = cursor.getInt(
						cursor.getColumnIndexOrThrow("bookId"));
				
				readBooks[bookIndex] = true;
			}
			
			return readBooks;
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}

	@Override
	public boolean[] GetChaptersReadList(int bookIndex) {
		
		boolean[] readChapters = null;
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = null;
    	try
    	{
    		String query="select b.chaptercount count, a.chapter ch, a.modify timestamp "+
    				     "from readstate a, books b where a.bookid = b._id and a.bookid = ? " +
    				     "group by a.bookid having max(a.modify) and a.chapter < b.chaptercount -1";
    		
    		String[] values = new String[] {Integer.toString(bookIndex) };
    		
			cursor = db.rawQuery(query, values);
			
			//Find the next chapter of an active book
			while(cursor.moveToNext())
			{
				if(cursor.isFirst()) {
					int chapterCount = cursor.getInt(
						cursor.getColumnIndexOrThrow("count"));
					int count = Math.min(MAX_CHAPTER_COUNT, chapterCount);
				
					readChapters = new boolean[count];
					for(int j = 0; j < count; j++)
						readChapters[j] = false;
				}
				
				
				int chapter = cursor.getInt(
						cursor.getColumnIndexOrThrow("ch"));
				
				readChapters[chapter] = true;
			}
			
			return readChapters;
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}
	}
	
	@Override
	public List<BookItemData> GetBookItems() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String query = "select b._id id, " + 
						"b.name name, " + 
						"strftime('%m-%d-%Y', a.timestamp / 1000, 'unixepoch') date, "+ 
						"count(distinct a.chapter) total, "+ 
						"count(distinct c.chapter) >0 hasReadState, "+ 
						"Round(((1.0*count(distinct a.chapter))  / (1.0*b.chapterCount)) * 100.0) percent " +
						"from booklogs a "+ 
						"inner join books b on b._id = a.bookid "+
						"left join readstate c on c.bookid = a.bookid and c.chapter < b.chaptercount -1 "+
						"where datetime(a.timestamp / 1000, 'unixepoch') > date('now', ?) " +
						"group by a.bookid "+ 
						"union "+ 
						"select _id id,  name, '' , '0' , '0' , '0' from books "+
						"except "+ 
						"select  bookid, name, '', '0',  '0' , '0' from booklogs, books where " +
						"books._id = bookid and datetime(timestamp / 1000, 'unixepoch') >= date('now', ?)"+
						"order by id";
		
		String[] args = new String[]{ LogFilter.QueryArg(mLogfilter), LogFilter.QueryArg(mLogfilter)};
		List<BookItemData> items = new ArrayList<BookItemData>();
		Cursor cursor = null;
    	try
    	{
			cursor = db.rawQuery(query, args);
			
			while(cursor.moveToNext()) {
				BookItemData item = new BookItemData();
				item.BookName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
				item.PercentRead = cursor.getFloat(cursor.getColumnIndexOrThrow("percent"));
				item.Read = (cursor.getInt(cursor.getColumnIndexOrThrow("hasReadState")) == 1);
				items.add(item);
			}
			return items;
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}		
		
	}

	@Override
	public String[] GetChapterReadTimeStamps(int bookIndex) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		String query = "select b.chapterCount count, a.bookid, a.chapter chapter, "+
				"strftime('%m-%d-%Y', a.timestamp / 1000, 'unixepoch') date " +
				"from booklogs a " +
				"join books b on b._id = a.bookid "+ 
				"where a.bookid = ? and datetime(a.timestamp / 1000, 'unixepoch') > date('now', ?) " +
				"group by a.bookid, a.chapter "+ 
				"having max(a.timestamp)  ";
		
		String[] args = new String[]{ 
				Integer.toString(bookIndex), 
				LogFilter.QueryArg(mLogfilter)};
		
		String[] timestamps = null;
		Cursor cursor = null;
    	try
    	{
    		cursor = db.rawQuery(query, args);
			
			//Find the next chapter of an active book
			while(cursor.moveToNext())
			{
				if(cursor.isFirst()) {
					int chapterCount = cursor.getInt(
						cursor.getColumnIndexOrThrow("count"));
					int count = Math.min(MAX_CHAPTER_COUNT, chapterCount);
				
					timestamps = new String[count];
					for(int j = 0; j < count; j++)
						timestamps[j] = "";
				}
				
				
				String timestamp = cursor.getString(
						cursor.getColumnIndexOrThrow("date"));
				
				int chapter = cursor.getInt(
						cursor.getColumnIndexOrThrow("chapter"));
				
				timestamps[chapter] = timestamp;
			}
			return timestamps;
    	}
		finally
		{
			if(cursor != null)
				cursor.close();
		}		
    	
	}

	@Override
	public LogFilter GetLogFilter() {
		return mLogfilter;
	}

	@Override
	public void SetLogFilter(LogFilter filter) {
		mLogfilter = filter;
	}
}
