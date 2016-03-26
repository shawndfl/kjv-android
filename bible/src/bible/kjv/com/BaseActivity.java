package bible.kjv.com;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import bible.kjv.com.data.BookContents;
import bible.kjv.com.data.BookDatabase;
import bible.kjv.com.data.FontSize;
import bible.kjv.com.data.HighLightSort;
import bible.kjv.com.data.HighLightState;
import bible.kjv.com.data.LogFilter;

public abstract class BaseActivity extends ActionBarActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";
	public final static String EXTRA_BOOK = "bible.kjv.com.BOOK";
	public final static String EXTRA_CHAPTER = "bible.kjv.com.CHAPTER";
	public final static String EXTRA_VERSE = "bible.kjv.com.VERSE";
	public final static String EXTRA_FILTER = "bible.kjv.com.FILTER";
	public final static String EXTRA_FAV_SORT = "bible.kjv.com.FAV_SORT";
	public final static String EXTRA_FONT_SIZE = "bible.kjv.com.FONT_SIZE";
	public final static String EXTRA_LOG_FILTER = "bible.kjv.com.LOG_FILTER";
	protected HighLightState mFilter;
	protected HighLightSort mSort;
	
	protected int mBookIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    
	    //Setup the chapter loader
	    if( !BookIf.IsReady())
	    {
	    	BookIf.Initialize(
	    		new BookDatabase(getApplicationContext()),
	    		new BookContents(getApplicationContext()));
	    }
	    
	    // Check whether we're recreating a previously destroyed instance
	    if (savedInstanceState != null) 
	    {
	    	mFilter = HighLightState.FromInteger(
					savedInstanceState.getInt(EXTRA_FILTER, 
							HighLightState.ToInteger(HighLightState.All)));
	    	
	    	mSort = HighLightSort.FromInteger(
					savedInstanceState.getInt(EXTRA_FAV_SORT, 
							HighLightSort.ToInteger(HighLightSort.Book)));
	    	
	    	BookIf.SetFontSize(FontSize.FromInteger(
					savedInstanceState.getInt(EXTRA_FONT_SIZE, 
							FontSize.ToInteger(FontSize.Small))));
	    	
	    	BookIf.SetLogFilter(LogFilter.FromInteger(
	    			savedInstanceState.getInt(EXTRA_LOG_FILTER, 
	    					LogFilter.ToInteger(LogFilter.All))));
	    }
	    else
	    {
	    	mFilter = HighLightState.All;
	    	mSort = HighLightSort.Book;
	    }
	    
	}
	
    @Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's current game state
	    savedInstanceState.putInt(EXTRA_FONT_SIZE, 
	    		FontSize.ToInteger(BookIf.GetFontSize()));
	    
	    // Save the user's current game state
	    savedInstanceState.putInt(EXTRA_FILTER, 
	    		HighLightState.ToInteger(mFilter));
	    
	    savedInstanceState.putInt(EXTRA_FAV_SORT, 
	    		HighLightSort.ToInteger(mSort));
	    
	    savedInstanceState.putInt(EXTRA_LOG_FILTER, 
    			LogFilter.ToInteger(BookIf.GetLogFilter()));
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
}