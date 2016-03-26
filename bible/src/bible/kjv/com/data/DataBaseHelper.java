package bible.kjv.com.data;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import bible.kjv.com.util.Trace;

public class DataBaseHelper extends SQLiteOpenHelper{
 
	private static final String TAG = "DataBaseHelper";
    private static String DB_NAME = "UserData";
 
    private final Context mContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 6);
        this.mContext = context;
    }	
    
    public void InitializeDatabase() {
    	boolean dbExist = checkDataBase();
		 
    	if(dbExist){
    		Trace.d(TAG, "Database already exists!");
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
        	this.close();
 
    		copyDataBase();
    	}
    }
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try
    	{
    		String myPath = mContext.getDatabasePath(DB_NAME).getPath();
    		
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    	}catch(SQLiteException e){
    		Trace.d(TAG, "Database not found.");
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() {
 
    	Trace.d(TAG, "Starting copy...");
    	//Open your local db as the input stream
    	try{
    		InputStream myInput = mContext.getAssets().open(DB_NAME +".db");
	    	
	    	// Path to the just created empty db
	    	String outFileName = mContext.getDatabasePath(DB_NAME).getPath();
	 
	    	//Open the empty db as the output stream
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	    	
	    	Trace.d(TAG, "Copied data over");
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
    	
    	}catch(Exception ex){
    		Trace.e(TAG, "Error!!!!");
    	}
 
    }

}
