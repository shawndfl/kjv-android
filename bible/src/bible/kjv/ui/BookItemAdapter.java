package bible.kjv.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bible.kjv.com.MainActivity;
import bible.kjv.com.R;
import bible.kjv.com.data.BookItemData;

public class BookItemAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = "BookItemAdapter";
	/**
	 * 
	 */
	private List<BookItemData> mElements = new ArrayList<BookItemData>();
    private LayoutInflater mInflater;
    
	public BookItemAdapter(MainActivity mainActivity, Context context, int resource, List<BookItemData> elements) {
		
		init(context, resource, elements);
	}
	
	private void init(Context context, int resource, List<BookItemData> elements) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mElements = elements;
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
	
	private View createViewFromResource(int position, View convertView, ViewGroup parent) {
    	
        View view;
        BookItemData book = mElements.get(position);
        
        if (convertView == null) {	        	
            view = mInflater.inflate(R.layout.book_item, parent, false);
        } else {
            view = convertView;
        }
        
        try {       
        	ImageView imgDone = (ImageView) view.findViewById(R.id.imgDone);
        	if(book.Read)
        		imgDone.setVisibility(ImageView.VISIBLE);
        	else 
        		imgDone.setVisibility(ImageView.INVISIBLE);
        	
        	TextView text = (TextView) view.findViewById(R.id.txtName);
        	text.setText(book.BookName);
        	
        	TextView percent = (TextView) view.findViewById(R.id.txtPercent);
        	setPercentText(book, percent);
        	
        } catch (ClassCastException e) {
            //Trace.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        return view;
    }

	private void setPercentText(BookItemData book, TextView percent) {
		if(book.PercentRead > 0) {
			String strPercent = String.format("%.0f", book.PercentRead);
			
			float max = 196.0f;
			//calculate the green gradient 
			int green = (int)((book.PercentRead / 100.0f) * max);
			green <<=8;
			
			//create the html
			String html = "<font color=" + hex(green) +">" + 
							strPercent+
							"</font>%";
			//Set the text
			percent.setText(Html.fromHtml(html));
		} else {
			percent.setText("");
		}
	}
	
	public static String hex(int n) {
	    // call toUpperCase() if that's required
	    return String.format("0x%06X", n);//.replace(' ', '0');
	}
}