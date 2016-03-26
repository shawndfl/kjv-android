package bible.kjv.com;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import bible.kjv.com.data.BookItemData;
import bible.kjv.com.data.BookNameManager;
import bible.kjv.com.data.IUserData;
import bible.kjv.com.data.LogEntry;
import bible.kjv.ui.BookItemAdapter;
import bible.kjv.ui.DialogBoxReset;

public class MainActivity extends BaseActivity implements DialogBoxReset.NoticeDialogListener {

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";
	
	List<BookItemData> mBooks = new ArrayList<BookItemData>();
	private BaseAdapter mAdapter;
	
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	  
	    // Get ListView object from xml
	    mListView = (ListView) findViewById(R.id.list);
	 
	    // Define a new Adapter
	    // First parameter - Context
	    // Second parameter - Layout for the row
	    // Third parameter - ID of the TextView to which the data is written
	    // Forth - the Array of data
	    mAdapter = new BookItemAdapter(this, this,
	    		android.R.layout.simple_list_item_1,
	    		mBooks);
	 
	    // Assign adapter to ListView
	    mListView.setAdapter(mAdapter); 
	 
	    // ListView Item Click Listener
	    mListView.setOnItemClickListener(new OnItemClickListener() {
	
	       @Override
	       public void onItemClick(AdapterView<?> parent, View view,
	          int position, long id) {
	         
	        // ListView Clicked item index
	        Intent intent = new Intent(view.getContext(), SelectChapterActivity.class);
	        intent.putExtra(EXTRA_BOOK, position);
	        startActivity(intent);
	        	    
	       } 
	    });
	    
	    mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				BookItemData item = (BookItemData) mListView.getItemAtPosition(position);
			    // Create an instance of the dialog fragment and show it
				DialogBoxReset dialog = new DialogBoxReset(
						item.BookName, 
						BookNameManager.GetId(item.BookName),
						DialogBoxReset.ALL_CHAPTERS);
				
				dialog.show(getFragmentManager(), "NoticeDialogFragment");

				return true;
			}
		});
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		refreshBooks();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
    		case bible.kjv.com.R.id.item_favorite: {
	    		Intent intent = new Intent(this, FavoriteActivity.class);	
	    		intent.putExtra(MainActivity.EXTRA_BOOK, IUserData.ALL_BOOKS);
		        startActivity(intent);
	    		return true;
    		}
    		case bible.kjv.com.R.id.item_log: {
	    		Intent intent = new Intent(this, LogActivity.class);	
	    		intent.putExtra(MainActivity.EXTRA_BOOK, IUserData.ALL_BOOKS);
		        startActivity(intent);
	    		return true;
    		}
    		case bible.kjv.com.R.id.item_next: {
    			LogEntry entry = BookIf.GetNextBookChapter();
    			Intent intent = new Intent(this, ReaderActivity.class);
	 	        intent.putExtra(MainActivity.EXTRA_BOOK, entry.BookIndex);
	 	        intent.putExtra(MainActivity.EXTRA_CHAPTER, entry.Chapter);
	 	        intent.putExtra(MainActivity.EXTRA_VERSE, 0);
	 	        startActivity(intent);    		
	    		return true;
    		}
        }
        return super.onOptionsItemSelected(item);
    }
    
	private void refreshBooks()
	{	
		mBooks.clear();
		List<BookItemData> temp = BookIf.GetBookItems();
		mBooks.addAll(temp);
		
		setTitle("Bible (" + BookIf.GetPercentReadFromLog() +")" +
		(BookIf.IsLogFiltered() ? "*" : ""));
		
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDialogPositiveClick(DialogBoxReset dialog) {
		
		BookIf.ResetLastRead(dialog.GetBookIndex());
		refreshBooks();
	}

	@Override
	public void onDialogNegativeClick(DialogBoxReset dialog) {
		
		//NOP
		ClipboardManager clipboard = (ClipboardManager)
		        getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("simple text","Hello, World!");
		clipboard.setPrimaryClip(clip);
	}
}