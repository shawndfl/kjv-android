package bible.kjv.ui;

import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bible.kjv.com.R;
import bible.kjv.com.data.BookNameManager;
import bible.kjv.com.data.LogEntry;

/**
 * This adapter is used to display the favorites list.
 * @author sdady
 *
 */
public class LogAdapter extends BaseAdapter  {

	@SuppressWarnings("unused")
	private static final String TAG = "LogAdapter";
	
	private List<LogEntry> mElements;
    
    private LayoutInflater mInflater;
    public LogAdapter(Context context, List<LogEntry> elements) {
		
		init(context, elements);
	}
	
	private void init(Context context, List<LogEntry> elements) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mElements = elements;
    }
	
	@Override
	public int getCount() {
		return mElements.size();
	}

	@Override
	public LogEntry getItem(int position) {
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
	public void SetElements(List<LogEntry> elements) {
		
		mElements = elements;
		
		notifyDataSetChanged();
	}
	
    private View createViewFromResource(int position, View convertView, ViewGroup parent) {
    	
        View view;
        LogEntry entry = mElements.get(position);
        
        if (convertView == null) {
            view = mInflater.inflate(R.layout.log_item, parent, false);
        } else {
            view = convertView;
        }
        
        try {       
        	
        	//View color = view.findViewById(R.id.SelectionColor);
        	//color.setBackgroundColor(GetColorHighlighColor(highlight));
        	
        	TextView textVerse = (TextView) view.findViewById(R.id.textBook);        	
        	textVerse.setText(BookNameManager.GetName(entry.BookIndex));
        	//textVerse.setTextSize(size);
        	
        	TextView textRef = (TextView) view.findViewById(R.id.textChapter);        	
        	textRef.setText(""+ (entry.Chapter+1));
        	//textRef.setTextSize(size);
        	
        	GregorianCalendar date = new GregorianCalendar();
			date.setTimeInMillis(entry.TimeStamp);
			String strDate = String.format("%02d-%02d-%04d",
					 date.get(GregorianCalendar.MONTH) + 1,					
					 date.get(GregorianCalendar.DAY_OF_MONTH),
					 date.get(GregorianCalendar.YEAR));
			
        	TextView textDate = (TextView)view.findViewById(R.id.textDate);
        	textDate.setText(strDate);
        	//textDate.setTextSize(size);
        	
        	String prevDate ="";
        	if(position > 0){
        		 LogEntry prev = mElements.get(position - 1);
     			 date.setTimeInMillis(prev.TimeStamp);
     			 prevDate = String.format("%02d-%02d-%04d",
     					 date.get(GregorianCalendar.MONTH) + 1,					
     					 date.get(GregorianCalendar.DAY_OF_MONTH),
     					 date.get(GregorianCalendar.YEAR));
        	}
        	
        	if(!prevDate.contentEquals(strDate)){
        		textDate.setVisibility(View.VISIBLE);
        		//Trace.d(TAG, "Visible '" + prevDate+ "' != '" + strDate + "'");
        	}
        	else 
        		textDate.setVisibility(View.GONE);
        	
        	
        } catch (ClassCastException e) {
            //Trace.e("FavoriteAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "FavoriteAdapter requires the resource ID to be a TextView", e);
        }
        
        return view;
    }
}
