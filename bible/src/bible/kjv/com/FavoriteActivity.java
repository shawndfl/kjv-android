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
import bible.kjv.com.data.HighLight;
import bible.kjv.com.data.HighLightSort;
import bible.kjv.com.data.HighLightState;
import bible.kjv.ui.FavoriteAdapter;

public class FavoriteActivity extends BaseActivity {

	private ListView mListView;
	private FavoriteAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
	    
		setTitle("Favorites");
		
	    // Get ListView object from xml
	    mListView = (ListView) findViewById(R.id.list);
	    
	    Intent intent = getIntent();
		mBookIndex = intent.getIntExtra(MainActivity.EXTRA_BOOK, 0);
	    
	    // Define a new Adapter
	    // First parameter - Context
	    // Second parameter - Layout for the row
	    // Third parameter - ID of the TextView to which the data is written
	    // Forth - the Array of data
	    mAdapter = new FavoriteAdapter(this, 
	    		GetFavorites(HighLightState.All, HighLightSort.Book, mBookIndex));
	 
	    // Assign adapter to ListView
	    mListView.setAdapter(mAdapter); 
	 
	    // ListView Item Click Listener
	    mListView.setOnItemClickListener(new OnItemClickListener() {
	
	       @Override
	       public void onItemClick(AdapterView<?> parent, View view,
	          int position, long id) {
	         
	    	HighLight highlight = (HighLight)parent.getItemAtPosition(position);
	    	if(highlight != null) {
	    		// ListView clicked item index
		    	   
	    		Intent intent = new Intent(view.getContext(), ReaderActivity.class);
	 	        intent.putExtra(MainActivity.EXTRA_BOOK, highlight.BookIndex);
	 	        intent.putExtra(MainActivity.EXTRA_CHAPTER, highlight.Chapter - 1);
	 	        intent.putExtra(MainActivity.EXTRA_VERSE, highlight.Verse);
	 	        startActivity(intent);    		
	    	}
	       } 
	    });
	}
	
	@Override
	public void onStart() {	
		super.onStart();
		
		SetElements(mFilter, mSort);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favorite, menu);
		
        if(mFilter == HighLightState.All) {
        	menu.findItem(R.id.menu_filter_all).setChecked(true);
		} else if(mFilter == HighLightState.Note ){
			menu.findItem(R.id.menu_filter_note).setChecked(true);
		} else if(mFilter == HighLightState.Like ){
			menu.findItem(R.id.menu_filter_like).setChecked(true);
		} else if(mFilter == HighLightState.Love ){
			menu.findItem(R.id.menu_filter_love).setChecked(true);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		HighLightState last = mFilter;
		
		int id = item.getItemId();
		if(id == R.id.menu_filter_all ){
			mFilter = HighLightState.All;
			item.setChecked(true);
		} else if(id == R.id.menu_filter_note ){
			mFilter = HighLightState.Note;
			item.setChecked(true);
		} else if(id == R.id.menu_filter_like ){
			mFilter = HighLightState.Like;
			item.setChecked(true);
		} else if(id == R.id.menu_filter_love ){
			mFilter = HighLightState.Love;
			item.setChecked(true);
		//Handle sort
		} else if(id == R.id.item_fav_sort ){
			if(mSort == HighLightSort.Book)
				mSort = HighLightSort.Date;
			else 
				mSort = HighLightSort.Book;
		}
		
		SetElements(mFilter, mSort);
		
		//Only show toast if something changed.
		if(last != mFilter) {
			Toast toast = Toast.makeText(this, "Showing " + mAdapter.getCount() + " items", Toast.LENGTH_LONG);
			toast.show();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void SetElements(HighLightState state, HighLightSort sort) {
		mAdapter.SetElements(GetFavorites(state, sort, mBookIndex));
	}
	
	private ArrayList<HighLight> GetFavorites(
			HighLightState filter, HighLightSort sort, int bookIndex) {
	
		ArrayList<HighLight> favs = BookIf.GetHighLights(filter, sort, bookIndex);
		
		return favs;
	}
}
