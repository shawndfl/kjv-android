package bible.kjv.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import bible.kjv.com.R;

/**
 * This dialog box is used to reset the last read times of 
 * a chapter or whole book.
 * @author sdady
 *
 */
public class DialogBoxReset extends  DialogFragment {

	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogBoxReset dialog);
        public void onDialogNegativeClick(DialogBoxReset dialog);
    }

	private String mTarget;
	private NoticeDialogListener mListener;
	private int mBookIndex;
	private int mChapter;
	public final static int ALL_CHAPTERS = -1;
	/**
	 * Gets the book index that needs to be reset.
	 * @return
	 */
	public int GetBookIndex() {
		return mBookIndex;
	}
	
	/**
	 * Gets the chapter that needs to be reset.
	 * @return
	 */
	public int GetChapter() {
		return mChapter;
	}
	
	/**
	 * Constructor
	 * @param target - What we are resetting. The name of the book or chapter.
	 * @param bookIndex
	 * @param chapter -The chapter to reset. If resetting all chapters pass in ALL_CHAPTERS
	 */
	public DialogBoxReset(String target, int bookIndex, int chapter) {
		mTarget = target;
		mBookIndex = bookIndex;
		mChapter = chapter;
	}
	
	private Context GetContext() {
		return getActivity();
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		String message = GetContext().getString(R.string.dialog_chapter_message) + 
				"\n" + mTarget;
		// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_chapter_title)
        	   .setMessage(message)
               .setPositiveButton(R.string.dialog_chapter_positive,
            		   new DialogInterface.OnClickListener()
               {
                   public void onClick(DialogInterface dialog, int id) 
                   {
                	   mListener.onDialogPositiveClick(DialogBoxReset.this);
                   }
               })
               
               .setNegativeButton(R.string.dialog_chapter_negative,
            		   new DialogInterface.OnClickListener() 
               {
                   public void onClick(DialogInterface dialog, int id) 
                   {
                	   mListener.onDialogNegativeClick(DialogBoxReset.this);
                   }
               });
        
        // Create the AlertDialog object and return it
        return builder.create();
	}
	
	// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

}
