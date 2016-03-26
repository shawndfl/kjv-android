package bible.kjv.com.data;

/**
 * Class used to store information about the row
 * @author sdady
 *
 */
public class BookItemData {
	
	/**
	 * Was the book ever read based on the booklogs table.
	 * NOTE: percentRead can still be 0.0 yet the book has been
	 * read before. That is what the log table is used.
	 */
	public boolean Read;
	/**
	 * The name of the book
	 */
	public String BookName;
	
	/**
	 *  percent read range is 0.0f - 100.0f
	 */
	public float PercentRead;
	
}