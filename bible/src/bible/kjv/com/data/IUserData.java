package bible.kjv.com.data;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

public interface IUserData
{
	/**
	 * Used for filtering favorites
	 */
	public static final int ALL_BOOKS = -1;
	
	/**
	 * Used to convert the index of the book to the string name of the book.
	 * @param book - Index of the book
	 * @return Int index of the book in a collection. 
	 */
	public String GetBookId(int bookIndex);
	
	/**
	 * Gets the highlight value of a verse.
	 * @param book - The index of the book.
	 * @param chapter - The chapter.
	 * @param verse - The verse.
	 * @return The highlight value of the given verse. If any of the above parameters are
	 * out of range HighLightState.None is returned.
	 */
	public HighLightState GetHighLight(int bookIndex, int chapter, int verse);

	/**
	 * Gets an array of all the highlights in a chapter of a given book.
	 * @param book
	 * @param chapter
	 * @return a sparse array that maps a verse to a highlight state. 
	 * Note not all verses will be included in this sparse array.
	 */
	public SparseArray<HighLightState> GetAllHighLights(int bookIndex, int chapter);
	
	/**
	 * Used to get a list of all highlights in the whole bible.
	 * @return
	 */
	public ArrayList<HighLight> GetHighLights(HighLightState filter, HighLightSort sort, int bookIndex);
	
	/**
	 * Gets the number of highlighted items
	 * @param filter
	 * @return
	 */
	public int GetHighLightCount(HighLightState filter);
	
	/**
	 * Sets the highlight for a book and chapter 
	 * @param book - String name of the book
	 * @param chapter - 
	 * @param verse - zero based
	 * @param highLight
	 */
	public void SetHighlight(int bookIndex, int chapter, int verse, HighLightState state);
	
	/**
	 * Sets the last read date.
	 * @param book
	 * @param chapter
	 * @param date
	 */
	public void SetLastReadDate(int bookIndex, int chapter);
	
	/**
	 * Resets the last read time for a given book and chapter.
	 * This function will set the last read time to 0.
	 * @param bookIndex
	 * @param chapter
	 */
	public void ResetLastReadDate(int bookIndex, int chapter);
	
	/**
	 * Resets all chapters for a given book.
	 * @param bookIndex
	 */
	public void ResetLastReadDate(int bookIndex);
	
	/**
	 * 
	 */
	public float GetPercentReadFromLog(int bookIndex);
	
	/**
	 * Get the percent read from the log entries which is the booklogs table.
	 * @return
	 */
	public float GetPercentReadFromLog(); 
	
	/**
	 * Gets the percent read of a given book.
	 * @param bookIndex
	 * @return
	 */
	public int GetChaptersRead(int bookIndex);
	
	/**
	 * Gets the size of the font.
	 * @return
	 */
	public FontSize GetFontSize();
	
	/**
	 * Sets the size of the font.
	 */
	public void SetFontSize(FontSize size);
	
	///////////////////////////New///////////////////
	
	/**
	 * Gets a property 
	 * @param name
	 * @return
	 */
	public String GetProperty(String name);
	
	/**
	 * Sets a property
	 * @param name
	 * @param Value
	 */
	public void SetProperty(String name, String Value);
	
	/**
	 * Gets the next chapter of the next book
	 * @return
	 */
	public LogEntry GetNextBookChapter();
	
	/**
	 * Used to get log entries.
	 * @param bookIndex
	 * @return
	 */
	public ArrayList<LogEntry> GetLog(int bookIndex);

	/**
	 * Gets the books read.
	 * @return
	 */
	public boolean[] GetBooksRead();

	/**
	 * Gets the chapters read in a book
	 * @param bookIndex
	 * @return
	 */
	public boolean[] GetChaptersReadList(int bookIndex);
	
	/**
	 * Gets the book items for the main display
	 * @return
	 */
	public List<BookItemData> GetBookItems();

	/**
	 * Gets the timestamp
	 * @param bookIndex
	 * @return
	 */
	public String[] GetChapterReadTimeStamps(int bookIndex);
	
	public LogFilter GetLogFilter();
	
	public void SetLogFilter(LogFilter filter);
}
