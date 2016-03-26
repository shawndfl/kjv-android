package bible.kjv.ui;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * 
 * This class will be used to replace the ArrayAdapter<String>
 * So we can customize the look and feel of the list view for 
 * each chapter.
 *
 */
public class ChapterLoaderAdapter extends ArrayAdapter<String>
{
	private Activity mActivity;
	
	public ChapterLoaderAdapter(Activity context, int resource, int textViewResourceId, List<String> objects) {
		super(context, resource, textViewResourceId, objects);
		mActivity = context;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    
		View view = super.getView(position, convertView, parent);
		mActivity.registerForContextMenu(view);
		return view;
	}
}