package bible.kjv.ui;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bible.kjv.com.BookIf;
import bible.kjv.com.R;
import bible.kjv.com.data.BookNameManager;
import bible.kjv.com.data.HighLight;
import bible.kjv.com.data.HighLightState;

/**
 * This adapter is used to display the favorites list.
 * @author sdady
 *
 */
public class FavoriteAdapter extends BaseAdapter  {

	@SuppressWarnings("unused")
	private static final String TAG = "FavoriteAdapter";
	
	private List<HighLight> mElements;
	private LayoutInflater mInflater;
    private Context mContext;
    
    
	public FavoriteAdapter(Context context, List<HighLight> elements) {
		
		init(context, elements);
	}
	
	private void init(Context context, List<HighLight> elements) {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mElements = elements;
    }
	
	@Override
	public int getCount() {
		return mElements.size();
	}

	@Override
	public HighLight getItem(int position) {
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
	
	/**
	 * This is used to set a new list of elements
	 * @param elements
	 */
	public void SetElements(List<HighLight> elements) {
		
		mElements = elements;
		
		notifyDataSetChanged();
	}
	
	public void SetFilter(HighLightState filter) {
		
	}
	
	private int GetColorHighlighColor(HighLight highlight) {
		
		
		Resources res = mContext.getResources();
		int color = res.getColor(R.color.normal);
		
		switch(highlight.State) {
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
		case All:
			color = res.getColor(R.color.normal);
			break;
		default:
			break;
		}
		
		return color;
	}
	
	private String GetVerseText(HighLight highlight) {
		
		ArrayList<String> verses = BookIf.GetChapter(
				highlight.BookIndex,
				highlight.Chapter);
		
		String verse = null;
		
		//if the verse is out of range change the highlight to none
		if(highlight.Verse >= verses.size()) {
			BookIf.SetHighlight(
					highlight.BookIndex, 
					highlight.Chapter, 
					highlight.Verse, HighLightState.None);
			verse ="Data Corrected!";
		}
		else
		{
			verse = verses.get(highlight.Verse);
			int stringOffset = 4;
			if(highlight.Verse >= 10)
				stringOffset++;
			if(highlight.Verse >= 100)
				stringOffset++;
			
			//remove the verse number
			verse = verse.substring(stringOffset);
			
			//add the source
			//verse += " (" + book + " " + highlight.Chapter + ":" + (highlight.Verse + 1) + ")";
		}
		return verse;
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
        HighLight highlight = mElements.get(position);
        
        if (convertView == null) {
            view = mInflater.inflate(R.layout.favorite_item, parent, false);
        } else {
            view = convertView;
        }
        
        try {       
        	float size = GetFontSize();
        	
        	View color = view.findViewById(R.id.SelectionColor);
        	color.setBackgroundColor(GetColorHighlighColor(highlight));
        	
        	TextView textVerse = (TextView) view.findViewById(R.id.textVerse);        	
        	textVerse.setText(GetVerseText(highlight));
        	textVerse.setTextSize(size);
        	
        	String book = BookNameManager.GetName(highlight.BookIndex);
        	String ref = "(" + book + " " + highlight.Chapter + ":" + (highlight.Verse + 1) + ")";
        	
        	TextView textRef = (TextView) view.findViewById(R.id.textReference);        	
        	textRef.setText(ref);
        	//textRef.setTextSize(size);
        	
        	GregorianCalendar date = new GregorianCalendar();
			date.setTimeInMillis(highlight.TimeStamp);
			String strDate = String.format("%02d-%02d-%04d",
					 date.get(GregorianCalendar.MONTH) + 1,					
					 date.get(GregorianCalendar.DAY_OF_MONTH),
					 date.get(GregorianCalendar.YEAR));
			
        	TextView textDate = (TextView)view.findViewById(R.id.textDate);
        	textDate.setText(strDate);
        	//textDate.setTextSize(size);
        	
        } catch (ClassCastException e) {
            //Trace.e("FavoriteAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "FavoriteAdapter requires the resource ID to be a TextView", e);
        }
        
        return view;
    }
}
