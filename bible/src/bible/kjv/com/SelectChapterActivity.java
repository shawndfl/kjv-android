package bible.kjv.com;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import bible.kjv.com.data.BookNameManager;
import bible.kjv.com.data.ChapterItemData;
import bible.kjv.ui.ChapterItemAdapter;
/**
 * 
 * This class is used to select a chapter. This activity will 
 * be called from MainActivity. Once a chapter is selected 
 * an Intent will be fired to launch ReaderActivity.
 *
 */
public class SelectChapterActivity extends BaseActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "SelectChapterActivity";
	
	private ListView mListView;
	private int mBookIndex;
	private List<ChapterItemData> mChapters = new ArrayList<ChapterItemData>();
	private BaseAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_select_chapter);
		
		Intent intent = getIntent();
		mBookIndex = intent.getIntExtra(MainActivity.EXTRA_BOOK, 0);
		
	    setTitle(BookNameManager.GetName(mBookIndex) +
	    		(BookIf.IsLogFiltered() ? "*" : ""));
	    
		// Get ListView object from xml
	    if(mListView == null)
	    	mListView = (ListView) findViewById(R.id.list);
		
	    // Define a new Adapter
	    // First parameter - Context
	    // Second parameter - Layout for the row
	    // Third parameter - ID of the TextView to which the data is written
	    // Forth - the Array of data
		if(mAdapter == null)
			mAdapter = new ChapterItemAdapter(this, mChapters);
	 
	 
	    // Assign adapter to ListView
	    mListView.setAdapter(mAdapter);
	    
	    // ListView Item Click Listener
	    mListView.setOnItemClickListener(new OnItemClickListener() {
	
	       @Override
	       public void onItemClick(AdapterView<?> parent, View view,
	          int position, long id) {	         	   
	          
	        Intent intent = new Intent(view.getContext(), ReaderActivity.class);
	        intent.putExtra(MainActivity.EXTRA_BOOK, mBookIndex);
	        intent.putExtra(MainActivity.EXTRA_CHAPTER, position);
	        intent.putExtra(MainActivity.EXTRA_VERSE, 0);
	        startActivity(intent);
	        	          
	       } 
	    }); 
	    
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		refreshChapters();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chapter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
    		case bible.kjv.com.R.id.item_favorite:{
	    		Intent intent = new Intent(this, FavoriteActivity.class);		
	    		intent.putExtra(MainActivity.EXTRA_BOOK, mBookIndex);
		        startActivity(intent);
	    		return true;
    		}
    		case bible.kjv.com.R.id.item_log: {
	    		Intent intent = new Intent(this, LogActivity.class);	
	    		intent.putExtra(MainActivity.EXTRA_BOOK, mBookIndex);
		        startActivity(intent);
	    		return true;
    		}
        }
        return super.onOptionsItemSelected(item);
    }
	
	private void refreshChapters()
	{
		int chapterCount = BookIf.GetChapterCount(mBookIndex);
		boolean[] chaptersRead = BookIf.GetChaptersReadList(mBookIndex);
		String[] timeStamps = BookIf.GetChapterReadTimeStamps(mBookIndex);
		
		mChapters.clear();
		for(int i = 0; i < chapterCount; i++) {
			ChapterItemData data = new ChapterItemData();
			data.ChapterName = "Chapter " + (i + 1);
			
			if(timeStamps != null)
				data.LastRead = timeStamps[i];
			else
				data.LastRead = ChapterItemData.NOT_READ;
			
			if(chaptersRead != null)
				data.Read = chaptersRead[i];
			else
				data.Read = false;
			
			mChapters.add(data);
		}
		
		String strPercent = BookIf.GetPercentReadFromLog(mBookIndex);
		setTitle(BookNameManager.GetName(mBookIndex)+" (" + strPercent +")" +
				(BookIf.IsLogFiltered() ? "*" : ""));
		
		mAdapter.notifyDataSetChanged();	
	}
}
