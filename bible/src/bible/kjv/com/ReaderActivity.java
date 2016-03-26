package bible.kjv.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import bible.kjv.com.data.BookNameManager;
import bible.kjv.com.data.FontSize;
import bible.kjv.com.data.LogEntry;


public class ReaderActivity extends BaseActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "ReaderActivity";
	/**
     * The fragment arguments representing the book and 
     * chapter for the fragment.
     */
    public static final String ARG_BOOK = "ARG_BOOK";
    public static final String ARG_CHAPTER = "ARG_CH";
    public static final String ARG_VERSE = "ARG_VERSE";
    
    private int mBookIndex;
    private int mChapter;
    private int mStartVerse;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    ChapterPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        
        Intent intent = getIntent();
        mBookIndex = intent.getIntExtra(MainActivity.EXTRA_BOOK, 0);
        mChapter = intent.getIntExtra(MainActivity.EXTRA_CHAPTER, 0);
        mStartVerse = intent.getIntExtra(MainActivity.EXTRA_VERSE, 0);      
        
        //set the title
        setTitle("Book: " + BookNameManager.GetName(mBookIndex) + " " + mChapter + 1);
        
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new ChapterPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setActiveChapter(mBookIndex);
        mSectionsPagerAdapter.SetInitialVerse(mStartVerse);
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(mChapter, true);
        
        //Trace.d(TAG, "New Adapter!!!");
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.verse, menu);
		
		//Trace.d(TAG, "New Menu");
		FontSize size = BookIf.GetFontSize();
        
        if(size == FontSize.Small) {
        	menu.findItem(R.id.menu_zoom_1).setChecked(true);
		} else if(size == FontSize.Mid ){
			menu.findItem(R.id.menu_zoom_2).setChecked(true);
		} else if(size == FontSize.Large ){
			menu.findItem(R.id.menu_zoom_3).setChecked(true);
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
			
		if(id == R.id.menu_zoom_1 ){
			BookIf.SetFontSize(FontSize.Small);
			mSectionsPagerAdapter.FontChanged();
			item.setChecked(true);
		} else if(id == R.id.menu_zoom_2 ){
			BookIf.SetFontSize(FontSize.Mid);
			mSectionsPagerAdapter.FontChanged();
			item.setChecked(true);
		} else if(id == R.id.menu_zoom_3 ){
			BookIf.SetFontSize(FontSize.Large);
			mSectionsPagerAdapter.FontChanged();
			item.setChecked(true);
		} else if(id == R.id.item_next ){
			LogEntry entry = BookIf.GetNextBookChapter();
			Intent intent = new Intent(this, ReaderActivity.class);
 	        intent.putExtra(MainActivity.EXTRA_BOOK, entry.BookIndex);
 	        intent.putExtra(MainActivity.EXTRA_CHAPTER, entry.Chapter);
 	        intent.putExtra(MainActivity.EXTRA_VERSE, 0);
 	        startActivity(intent); 
		}
		return super.onOptionsItemSelected(item);
	}
}
