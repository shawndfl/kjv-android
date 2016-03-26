package bible.kjv.com.data;

import android.provider.BaseColumns;

/**
 * Database Tables (DBT) is used to describe the sql database tables used in this application 
 * @author sdady
 *
 */
public class DBT {
	/**
     *  Inner class that defines the table contents 
     */
    public static abstract class BookLogEntry implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = "NULL";
		public static final String TABLE_NAME = "booklogs";
        public static final String COLUMN_BOOKID = "bookId";
        public static final	String COLUMN_CHAPTER = "chapter";
        public static final String COLUMN_LASTREAD = "timeStamp";         
    }
    
    public static abstract class BookReadEntry implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = "NULL";
		public static final String TABLE_NAME = "readstate";
        public static final String COLUMN_BOOKID = "bookId";
        public static final	String COLUMN_CHAPTER = "chapter";   
        public static final String COLUMN_LASTREAD = "modify"; 
    }
    
    /**
     * Inner class used to define the book highlight table
     * @author sdady
     *
     */
    public static abstract class BookHighLightEntry implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = "NULL";
		public static final String TABLE_NAME = "highlight";
        public static final String COLUMN_BOOKID = "bookId";
        public static final	String COLUMN_CHAPTER = "chapter";
        public static final String COLUMN_VERSE = "verse";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_DATE = "date";
    }
    
    public static abstract class PropertyEntry implements BaseColumns {
        public static final String COLUMN_NAME_NULLABLE = "NULL";
		public static final String TABLE_NAME = "properties";
        public static final String COLUMN_NAME = "name";
        public static final	String COLUMN_VALUE = "value";
        public static final String COLUMN_MODIFIED = "timeStamp";         
    }
}
