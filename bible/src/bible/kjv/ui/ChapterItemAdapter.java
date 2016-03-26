package bible.kjv.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bible.kjv.com.R;
import bible.kjv.com.data.ChapterItemData;

public class ChapterItemAdapter extends BaseAdapter {

	/**
	 * 
	 */
	private List<ChapterItemData> mElements = new ArrayList<ChapterItemData>();
    private LayoutInflater mInflater;
    
	public ChapterItemAdapter(Context context, List<ChapterItemData> elements) {
		
		init(context, elements);
	}
	
	private void init(Context context, List<ChapterItemData> elements) {
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
        ChapterItemData chapter = mElements.get(position);
        
        if (convertView == null) {	        	
            view = mInflater.inflate(R.layout.chapter_item, parent, false);
        } else {
            view = convertView;
        }
        
        try {
        	ImageView imgDone = (ImageView) view.findViewById(R.id.imgDone);
        	
        	if(chapter.Read)
        		imgDone.setVisibility(ImageView.VISIBLE);
        	else
        		imgDone.setVisibility(ImageView.INVISIBLE);
        	
        	TextView text = (TextView) view.findViewById(R.id.textChapter);
        	text.setText(chapter.ChapterName);
        	
        	TextView date = (TextView) view.findViewById(R.id.textLastRead);
        	date.setText(chapter.LastRead);
        	
        } catch (ClassCastException e) {
            //Trace.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        return view;
    }
	
}