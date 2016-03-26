package bible.kjv.ui;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bible.kjv.com.BookIf;
import bible.kjv.com.R;
import bible.kjv.com.data.HighLightState;

public class PassageAdapter extends BaseAdapter {
	 
	@SuppressWarnings("unused")
	private static final String TAG = "PassageAdapter";
	
	private List<String> mElements;
	private int mBookIndex;
	private int mChapter;
	private SparseArray<HighLightState> mHighlight;
	
	/**
     * Lock used to modify the content of {@link #mElements}. Any write operation
     * performed on the array should be synchronized on this lock.
     */
    private final Object mLock = new Object();
    
    private LayoutInflater mInflater;
    private Context mContext;
    
    /**
     * Create a passage adapter
     * @param elements
     */
	public PassageAdapter(Context context, List<String> elements, int bookIndex, int chapter) {
		init(context, elements, bookIndex, chapter);
	}
	
	private void init(Context context, List<String> elements, int bookIndex, int chapter) {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mElements = elements;
        mBookIndex = bookIndex;
        mChapter = chapter;
        mHighlight = BookIf.GetAllHighlight(mBookIndex, mChapter);
    }
	
	 /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(String item) {
        synchronized (mLock) {          
        	mElements.add(item);
        }
        notifyDataSetChanged();
    }
    
	@Override
	public int getCount() {
		return mElements.size();
	}

	@Override
	public Object getItem(int position) {
		return mElements.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return createViewFromResource(position, convertView, parent);
    }
	
	public void OnClicked(int position) {
		
		CycleHighlight(position);
	}
	
	private void CycleHighlight(int position) {
		//Trace.v(TAG, "CycleHighlight: " + position);
		
		HighLightState currentState = mHighlight.get(position, HighLightState.None);
		HighLightState nextState = HighLightState.None;
		switch(currentState) {
		case None:
			nextState = HighLightState.Note;
			break;
		case Note:
			nextState = HighLightState.Like;
			break;
		case Like:
			nextState = HighLightState.Love;
			break;
		case Love:
			nextState = HighLightState.None;
			break;
		default:
			nextState = HighLightState.None;
			break;
		}
		
		BookIf.SetHighlight(mBookIndex, mChapter, position, nextState);
		
		mHighlight.append(position, nextState);
		notifyDataSetChanged();
	}
	
	private int GetColorHighlighColor(int position) {
		
		HighLightState state = mHighlight.get(position, HighLightState.None);
		Resources res = mContext.getResources();
		
		int color = res.getColor(R.color.normal);
		
		switch(state) {
		case None:
			color = res.getColor(R.color.normal);
			break;
		case Note:
			color = res.getColor(R.color.note);
			break;
		case Like:
			color = res.getColor(R.color.like);
			break;
		case Love:
			color = res.getColor(R.color.love);
			break;
		default:
			color = res.getColor(R.color.normal);
			break;
		}
		
		return color;
	}
	
	private float GetFontSize(){
		Resources rs =  mContext.getResources();
		
		switch(BookIf.GetFontSize()){
			case Small:
				return rs.getDimension(R.dimen.font_size_small);
			case Mid:
				return rs.getDimension(R.dimen.font_size_mid);
			case Large:
				return rs.getDimension(R.dimen.font_size_large);
		}
		return 0;
	}

    private View createViewFromResource(int position, View convertView, ViewGroup parent) {
    	
        View view;
        
        if (convertView == null) {
            view = mInflater.inflate(R.layout.verse_item, parent, false);
        } else {
            view = convertView;
        }
        
        view.setBackgroundColor(GetColorHighlighColor(position));
        
        try {
        	
        	TextView textVerse = (TextView) view.findViewById(R.id.textVerse);
        	
        	String item = mElements.get(position);
        	String verseNo = item.substring(0, 3);
        	String verse = item.substring(4);
        	if(position >= mElements.size() -1)
        		verse += "<br><br><br>";
        	
        	float size = GetFontSize();
        	textVerse.setTextSize(size);
        	
        	//show the verse in red and the text in black
        	String html = String.format(
        			"<font color=#ff0000>%s</font><font color=#000000>%s</font>",
        			verseNo, verse);
        	
        	textVerse.setText(Html.fromHtml(html));
             
        } catch (ClassCastException e) {
            //Trace.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        return view;
    }
}
