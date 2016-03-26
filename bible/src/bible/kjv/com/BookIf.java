package bible.kjv.com;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;
import bible.kjv.com.data.BookItemData;
import bible.kjv.com.data.FontSize;
import bible.kjv.com.data.HighLight;
import bible.kjv.com.data.HighLightSort;
import bible.kjv.com.data.HighLightState;
import bible.kjv.com.data.IBookContents;
import bible.kjv.com.data.IUserData;
import bible.kjv.com.data.LogEntry;
import bible.kjv.com.data.LogFilter;

/**
 * 
 * ChapterLoader is a static class that is responsible for loading 
 * the text of a given chapter. It is also the main interface for
 * all activities to query information about the Bible.
 *
 */
public class BookIf {
	
	
	@SuppressWarnings("unused")
	private static final String TAG = "ChapterLoader";
	
	private static IUserData mUserDataIf = null;
	private static IBookContents mBookContentsIf;
	private static boolean mReady = false;
	
	/**
	 * This is a static class.
	 */
	private BookIf() { }
	
	/**
	 * This function needs to be called at the
	 * creation of an activity.
	 * @param userDataIf - interface used to access user data. Default should be BookDatabase.
	 * @param bookContentsIf - interface used to access the bible contents. Default should be BookContents.
	 */
	public static void Initialize (IUserData userDataIf, IBookContents bookContentsIf) {
		mUserDataIf = userDataIf;
		mBookContentsIf = bookContentsIf;
		mReady=true;
	}
	
    
	/**
	 * Returns true if Initialize was called.
	 * @return
	 */
	public static boolean IsReady(){
		return mReady;
	}
	
	/**
	 * Gets the number of chapters in a given book. If 
	 * the book is not found 0 is returned.
	 * @param book - Index of the book.
	 * @return Number of chapters or 0.
	 */
	public static int GetChapterCount(int bookIndex) {
		
		return mBookContentsIf.GetChapterCount(bookIndex);
	}
	
	/**
	 * Gets the next chapter that should be read.
	 * @return
	 */
	public static LogEntry GetNextBookChapter(){
		 return mUserDataIf.GetNextBookChapter();
	}
	
	/**
	 * Gets the text of the chapter. Each entry in the string list is a verse.
	 * If there is any error this will return and empty list and log the error.
	 * @param book - Index of the book.
	 * @param chapter - Chapter is one based. So chapter 1 is chapter 1.
	 * @return Full text of the chapter. each array element is a verse.
	 */
	public static ArrayList<String> GetChapter(int bookIndex, int chapter) {
		
		return mBookContentsIf.GetChapter(bookIndex, chapter); 
	}
	
	/**
	 * Sets the last read date for a chapter.
	 * @param book
	 * @param chapter - One based chapter.
	 */
	public static void SetLastRead(int bookIndex, int chapter) {
		
		//set the last read date
		mUserDataIf.SetLastReadDate(bookIndex, chapter-1);
	
	}
	
	/**
	 * Resets the last read time for a given chapter and book.
	 * @param bookIndex
	 * @param chapter - One based chapter.
	 */
	public static void ResetLastRead(int bookIndex, int chapter) {
		
		//set the last read date
		mUserDataIf.ResetLastReadDate(bookIndex, chapter-1);
	}
	
	/**
	 * Resets the last read time for a given chapter and book.
	 * @param bookIndex
	 * @param chapter - One based chapter.
	 */
	public static void ResetLastRead(int bookIndex) {
		
		//set the last read date
		mUserDataIf.ResetLastReadDate(bookIndex);
	}
	
	/**
	 * Gets the highlight state of a given verse.
	 * @param bookIndex
	 * @param chapter
	 * @param verse
	 * @return
	 */
	public static HighLightState GetHighlight(int bookIndex, int chapter, int verse) {
		return mUserDataIf.GetHighLight(bookIndex, chapter, verse);
	}
	
	/**
	 * Gets the highlight state of a given verse.
	 * @param bookIndex
	 * @param chapter
	 * @param verse
	 * @return
	 */
	public static SparseArray<HighLightState> GetAllHighlight(int bookIndex, int chapter) {
		return mUserDataIf.GetAllHighLights(bookIndex, chapter);
	}
	
	/**
	 * Gets the number of highlighted items.
	 * @param filter
	 * @return
	 */
	public int GetHighLightCount(HighLightState filter) {
		return mUserDataIf.GetHighLightCount(filter);
	}
	
	/**
	 * Gets all the highlights in the bible.
	 * @return
	 */
	public static ArrayList<HighLight> GetHighLights(
			HighLightState filter, HighLightSort sort, int bookIndex) {
		
		return mUserDataIf.GetHighLights(filter, sort, bookIndex);
	}
	
	/**
	 * Gets all the highlights in the bible.
	 * @return
	 */
	public static ArrayList<LogEntry> GetLog(int bookIndex) {
		return mUserDataIf.GetLog(bookIndex);
	}
	
	/**
	 * Used to set the highlighted value for a verse.
	 * @param bookIndex
	 * @param chapter
	 * @param verse
	 * @param state
	 */
	public static void SetHighlight(int bookIndex, int chapter, int verse, HighLightState state) {
		mUserDataIf.SetHighlight(bookIndex, chapter, verse, state);
	}
	
	
	/**
	 * Gets the percent read from the log entries.
	 * @return
	 */
	public static String GetPercentReadFromLog(int bookIndex) {
		float percent = mUserDataIf.GetPercentReadFromLog(bookIndex) * 100.0f;
		String strPercent = String.format("%.1f%%", percent);
		
		return strPercent;
	}
	
	/**
	 * Gets the percent read from the log entries.
	 * @return
	 */
	public static String GetPercentReadFromLog() {
		float percent = mUserDataIf.GetPercentReadFromLog() * 100.0f;
		String strPercent = String.format("%.1f%%", percent);
		
		return strPercent;
	}
	
	/**
	 * Is the log filtered
	 */
	public static boolean IsLogFiltered() {
		return GetLogFilter() != LogFilter.All;
	}
	
	/**
	 * Gets the size of the font.
	 * @return
	 */
	public static FontSize GetFontSize(){
		return mUserDataIf.GetFontSize();
	}
	
	/**
	 * Sets the size of the font.
	 */
	public static void SetFontSize(FontSize size) {
		mUserDataIf.SetFontSize(size);
	}

	public static boolean[] GetChaptersReadList(int bookIndex) {
		return mUserDataIf.GetChaptersReadList(bookIndex);
	}
	
	public static List<BookItemData> GetBookItems() {
		return mUserDataIf.GetBookItems();
	}
	
	public static String[] GetChapterReadTimeStamps(int bookIndex) {
		
		return mUserDataIf.GetChapterReadTimeStamps(bookIndex);
	}
	
	public static LogFilter GetLogFilter() {
		return mUserDataIf.GetLogFilter();
	}

	public static void SetLogFilter(LogFilter filter) {
		mUserDataIf.SetLogFilter(filter);
	}
}

