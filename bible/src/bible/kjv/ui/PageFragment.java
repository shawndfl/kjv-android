package bible.kjv.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import bible.kjv.com.BookIf;
import bible.kjv.com.R;
import bible.kjv.com.ReaderActivity;
import bible.kjv.com.data.BookNameManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class PageFragment extends Fragment {
	
	@SuppressWarnings("unused")
	private static final String TAG = "PageFragment";
	private int mBookIndex;
	private int mChapter;
	private int mVerse;
	private PassageAdapter mAdapter;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	View rootView = inflater.inflate(R.layout.fragment_reader, container, false);
        setRetainInstance(true);
    	
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    	if(getArguments() != null) {
    		
    		mBookIndex = getArguments().getInt(ReaderActivity.ARG_BOOK);
	    	mChapter = getArguments().getInt(ReaderActivity.ARG_CHAPTER);
	    	mVerse = getArguments().getInt(ReaderActivity.ARG_VERSE);
	    	
	    	ImageButton btnFinish = (ImageButton)getView().findViewById(R.id.btnDoneReading);
	    		
    		btnFinish.setVisibility(ImageButton.VISIBLE);
	    	
	    	btnFinish.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Resources res = getResources();
			    	String txt = res.getString(R.string.loggedRead);
			    	
					BookIf.SetLastRead(mBookIndex, mChapter);
					Toast toast = Toast.makeText(
							v.getContext(), 
							txt, Toast.LENGTH_LONG);
					
			        toast.show();
					v.setVisibility(ImageButton.GONE);
				}
			});
	    	
	    	ListView listView = (ListView) getView().findViewById(R.id.chapter_text);
	    	ArrayList<String> verses = BookIf.GetChapter(mBookIndex, mChapter);
	    	
	    	mAdapter = new PassageAdapter(
	    			getActivity(), 
	    			verses,
	    			mBookIndex,
	    			mChapter);
	    	
	    	// Assign adapter to ListView
		    listView.setAdapter(mAdapter); 
		 
		    // ListView Item Click Listener
		    listView.setOnItemClickListener(new OnItemClickListener() {
		
		       @Override
		       public void onItemClick(AdapterView<?> parent, View view,
		          int position, long id) {
		    	   
		    	   PassageAdapter passageAdapter = (PassageAdapter)parent.getAdapter();
	    		   passageAdapter.OnClicked(position);
		    	   
		       } 
		    });
		    listView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					
					String verseId = 
							BookNameManager.GetName(mBookIndex) +	//book 
							" " +mChapter + 						//chapter
							":" + (position +1); 					//verse
					
					//build the full text in the format of (book ch:vs) text
					String verse = (String)mAdapter.getItem(position);
					verse = verse.substring(4);
					String fullText = "(" + verseId + ") " + verse;
					
					Activity activity = getActivity();
					
					//copy to the clip board
					ClipboardManager clipboard = (ClipboardManager)
					        activity.getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clip = ClipData.newPlainText("verse",fullText);
					clipboard.setPrimaryClip(clip);
					
					//show toast 
					Toast toast = Toast.makeText(activity, "Copied " + verseId, Toast.LENGTH_LONG);
					toast.show();
					return true;
				}
		    	
			});
		    
		    listView.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus)
						v.scrollBy(0, 0);
				}
			});
		    
		    listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
				
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					
					if(view.isFocused()) {
						
						float precentRead = (float)(firstVisibleItem + visibleItemCount) / (float)totalItemCount; 
						String precent = String.format("%d%%", (int)(precentRead * 100.0f));
		    
						getActivity().setTitle(
								BookNameManager.GetName(mBookIndex) +
								" " + mChapter + " (" + precent + ")");
						
					}
				}
			});
		    
		    //Scroll to the verse.
		    if(mVerse > 0)
		    	listView.setSelection(mVerse);
		        
    	} else {
    		//Trace.e(TAG, "No Bundle!!!");
    	}
    }
    
    /**
     * Called when the font changes size.
     */
    public void FontChanged() {
    	mAdapter.notifyDataSetChanged();
    }
}
