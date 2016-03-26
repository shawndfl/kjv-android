package bible.kjv.com.data;

import java.util.ArrayList;

/**
 * An interface used to access the book contents on the disk.
 * @author sdady
 *
 */
public interface IBookContents {

	/**
	 * Gets the number of chapters in a given book. If 
	 * the book is not found 0 is returned.
	 * @param book - The book index.
	 * @return Number of chapters or 0.
	 */
	public abstract int GetChapterCount(int bookIndex);

	/**
	 * Gets the total number of chapters.
	 * @return
	 */
	public abstract int GetTotalNumberOfChapters();

	/**
	 * Gets the text of the chapter. Each entry in the string list is a verse.
	 * If there is any error this will return and empty list and log the error.
	 * @param book - Index of the book.
	 * @param chapter - Chapter is one based. So chapter 1 is chapter 1.
	 * @return Full text of the chapter. each array element is a verse.
	 */
	public abstract ArrayList<String> GetChapter(int bookIndex, int chapter);

}