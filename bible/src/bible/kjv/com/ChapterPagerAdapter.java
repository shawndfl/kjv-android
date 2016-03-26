package bible.kjv.com;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import bible.kjv.com.data.BookNameManager;
import bible.kjv.ui.PageFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ChapterPagerAdapter extends FragmentStatePagerAdapter {
	
	@SuppressWarnings("unused")
	private static final String TAG = "SectionsPagerAdapter";
	
	private int mBookIndex;
	private int mChapterCount;
	private int mInitialVerse = 0;
	private SparseArray<PageFragment> mActive = new SparseArray<PageFragment>();
	
    public ChapterPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    
    /**
     * Sets the verse to scroll to for the first fragment.
     * @param initialVerse
     */
    public void SetInitialVerse(int initialVerse) {
    	mInitialVerse = initialVerse;
    }
    
    /**
     * Called when the font changes and refreshes all the fragments
     */
    public void FontChanged() {
    	
    	for(int i = 0; i < mActive.size(); i++) {
    		int key = mActive.keyAt(i);
    		mActive.get(key).FontChanged();
    	}
    }
    
    /**
     * Sets the active book and chapter
     * @param book - Index of the book.
     */
    public void setActiveChapter(int bookIndex) {
    	mBookIndex = bookIndex;
		mChapterCount = BookIf.GetChapterCount(mBookIndex);
	}

	@Override
    public Fragment getItem(int position) {
		
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
		PageFragment fragment = newInstance(mBookIndex, position + 1); 	//chapter is 1 based
		mActive.append(position, fragment);
		
        return fragment;
    }
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	    super.destroyItem(container, position, object);
		mActive.remove(position);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		PageFragment fragment = 
				(PageFragment) super.instantiateItem(
						container,
						position);
		
		mActive.append(position, fragment);
		
	    return fragment;
	}
	
    @Override
    public int getCount() {
        return mChapterCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
    	return "Book " + BookNameManager.GetName(mBookIndex) + " " + (position +1);
    }
    
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    private PageFragment newInstance(int bookIndex, int chapter) {
    	PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        
        args.putInt(ReaderActivity.ARG_BOOK, bookIndex);
        args.putInt(ReaderActivity.ARG_CHAPTER, chapter);
        args.putInt(ReaderActivity.ARG_VERSE, mInitialVerse);
        
        //reset the verse. We are only using it once.
        mInitialVerse = 0;
        
        fragment.setArguments(args);
        return fragment;
    }
}