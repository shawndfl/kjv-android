package bible.kjv.com;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import bible.kjv.com.data.LogEntry;
import bible.kjv.com.data.LogFilter;
import bible.kjv.ui.LogAdapter;

public class LogActivity extends BaseActivity {

	private ListView mListView;
	private LogAdapter mAdapter;
	private int mBookIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		
	    // Get ListView object from xml
	    mListView = (ListView) findViewById(R.id.list);
	    
	    Intent intent = getIntent();
		mBookIndex = intent.getIntExtra(MainActivity.EXTRA_BOOK, 0);
		
	    // Define a new Adapter
	    // First parameter - Context
	    // Second parameter - Layout for the row
	    // Third parameter - ID of the TextView to which the data is written
	    // Forth - the Array of data
	    mAdapter = new LogAdapter(this, GetLog(mBookIndex));
	 
	    // Assign adapter to ListView
	    mListView.setAdapter(mAdapter); 
	 
	    // ListView Item Click Listener
	    mListView.setOnItemClickListener(new OnItemClickListener() {
	
	       @Override
	       public void onItemClick(AdapterView<?> parent, View view,
	          int position, long id) {
	         
	    	    LogEntry entry = (LogEntry)parent.getItemAtPosition(position);
		    	if(entry != null) {
		    		// ListView clicked item index
			    	   
		    		Intent intent = new Intent(view.getContext(), ReaderActivity.class);
		 	        intent.putExtra(MainActivity.EXTRA_BOOK, entry.BookIndex);
		 	        intent.putExtra(MainActivity.EXTRA_CHAPTER, entry.Chapter);
		 	        intent.putExtra(MainActivity.EXTRA_VERSE, 0);
		 	        startActivity(intent);    		
		    	}
	       } 
	    });
	}
	
	@Override
	public void onStart() {	
		super.onStart();
		
		SetElements();
		
		setTitle("Reading Log" +
				(BookIf.IsLogFiltered() ? "*" : ""));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_menu, menu);
		
		switch(BookIf.GetLogFilter()) {
		case All:
			menu.findItem(R.id.menu_all).setChecked(true);
			break;
		case Over1Year:
			menu.findItem(R.id.menu_1_year).setChecked(true);
			break;
		case Over30Days:
			menu.findItem(R.id.menu_30_days).setChecked(true);
			break;
		case Over60Days:
			menu.findItem(R.id.menu_60_days).setChecked(true);
			break;
		case Over6Months:
			menu.findItem(R.id.menu_6_months).setChecked(true);
			break;
		case Over90Days:
			menu.findItem(R.id.menu_90_days).setChecked(true);
			break;
		default:
			menu.findItem(R.id.menu_all).setChecked(true);
        }
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		LogFilter last = BookIf.GetLogFilter();
		
		int id = item.getItemId();
		if(id == R.id.menu_all ){
			BookIf.SetLogFilter(LogFilter.All);
			item.setChecked(true);
		} else if(id == R.id.menu_30_days ){
			BookIf.SetLogFilter(LogFilter.Over30Days);
			item.setChecked(true);
		} else if(id == R.id.menu_60_days ){
			BookIf.SetLogFilter(LogFilter.Over60Days);
			item.setChecked(true);
		} else if(id == R.id.menu_90_days ){
			BookIf.SetLogFilter(LogFilter.Over90Days);
			item.setChecked(true);
		} else if(id == R.id.menu_6_months ){
			BookIf.SetLogFilter(LogFilter.Over6Months);
			item.setChecked(true);
		} else if(id == R.id.menu_1_year ){
			BookIf.SetLogFilter(LogFilter.Over1Year);
			item.setChecked(true);
		}  
		
		//update the title
		setTitle("Reading Log" +
				(BookIf.IsLogFiltered() ? "*" : ""));
		
		SetElements();
		
		//Only show toast if something changed.
		if(last != BookIf.GetLogFilter()) {
			Toast toast = Toast.makeText(this, "Showing " + mAdapter.getCount() + " items", Toast.LENGTH_LONG);
			toast.show();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void SetElements() {
		mAdapter.SetElements(GetLog(mBookIndex));
	}
	
	private ArrayList<LogEntry> GetLog(int bookIndex) {
	
		ArrayList<LogEntry> log = BookIf.GetLog(bookIndex);
		
		return log;
	}
}
