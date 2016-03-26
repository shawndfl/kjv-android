package bible.kjv.com.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import bible.kjv.com.R;

/**
 * This class manages all interactions with the content in the asset folder.
 * This includes the bible and the file structure that holds it. 
 * @author sdady
 *
 */
public class BookContents implements IBookContents {
	
	@SuppressWarnings("unused")
	private static final String TAG = "BookContents";
	
	public static final int TOTAL_CHAPTER_COUNT = 1189;
	public static final int BOOK_COUNT = 66;
	public static final int INVALID_CACHE = -1;
	
	private Context mContext = null;
	private static int[] mChapterCountCache;
	
	/* (non-Javadoc)
	 * @see bible.kjv.com.data.IBookContents#SetContext(android.content.Context)
	 */
	public BookContents (Context context) {
		mContext = context;
		
		 //send the books to book manager
	    Resources res = mContext.getResources();
	    String[] books = res.getStringArray(R.array.books);	    
	    BookNameManager.SetNameArray(books);
	    
	    mChapterCountCache = new int[BOOK_COUNT];
		mChapterCountCache[0] = 50;
		mChapterCountCache[1] = 40;
		mChapterCountCache[2] = 27;
		mChapterCountCache[3] = 36;
		mChapterCountCache[4] = 34;
		mChapterCountCache[5] = 24;
		mChapterCountCache[6] = 21;
		mChapterCountCache[7] = 4;
		mChapterCountCache[8] = 31;
		mChapterCountCache[9] = 24;
		mChapterCountCache[10] = 22;
		mChapterCountCache[11] = 25;
		mChapterCountCache[12] = 29;
		mChapterCountCache[13] = 36;
		mChapterCountCache[14] = 10;
		mChapterCountCache[15] = 13;
		mChapterCountCache[16] = 10;
		mChapterCountCache[17] = 42;
		mChapterCountCache[18] = 150;
		mChapterCountCache[19] = 31;
		mChapterCountCache[20] = 12;
		mChapterCountCache[21] = 8;
		mChapterCountCache[22] = 66;
		mChapterCountCache[23] = 52;
		mChapterCountCache[24] = 5;
		mChapterCountCache[25] = 48;
		mChapterCountCache[26] = 12;
		mChapterCountCache[27] = 14;
		mChapterCountCache[28] = 3;
		mChapterCountCache[29] = 9;
		mChapterCountCache[30] = 1;
		mChapterCountCache[31] = 4;
		mChapterCountCache[32] = 7;
		mChapterCountCache[33] = 3;
		mChapterCountCache[34] = 3;
		mChapterCountCache[35] = 3;
		mChapterCountCache[36] = 2;
		mChapterCountCache[37] = 14;
		mChapterCountCache[38] = 4;
		mChapterCountCache[39] = 28;
		mChapterCountCache[40] = 16;
		mChapterCountCache[41] = 24;
		mChapterCountCache[42] = 21;
		mChapterCountCache[43] = 28;
		mChapterCountCache[44] = 16;
		mChapterCountCache[45] = 16;
		mChapterCountCache[46] = 13;
		mChapterCountCache[47] = 6;
		mChapterCountCache[48] = 6;
		mChapterCountCache[49] = 4;
		mChapterCountCache[50] = 4;
		mChapterCountCache[51] = 5;
		mChapterCountCache[52] = 3;
		mChapterCountCache[53] = 6;
		mChapterCountCache[54] = 4;
		mChapterCountCache[55] = 3;
		mChapterCountCache[56] = 1;
		mChapterCountCache[57] = 13;
		mChapterCountCache[58] = 5;
		mChapterCountCache[59] = 5;
		mChapterCountCache[60] = 3;
		mChapterCountCache[61] = 5;
		mChapterCountCache[62] = 1;
		mChapterCountCache[63] = 1;
		mChapterCountCache[64] = 1;
		mChapterCountCache[65] = 22;
	}
	
	/* (non-Javadoc)
	 * @see bible.kjv.com.data.IBookContents#GetChapterCount(java.lang.String)
	 */
	@Override
	public int GetChapterCount(int bookIndex) {
		int count = mChapterCountCache[bookIndex];
		if(count != INVALID_CACHE)
			return count;
		
		if(mContext == null) {
			////Trace.e(TAG, "Need to call SetContext() first.");
			return 0;
		}
		
		String path = "Bible/" + BookNameManager.GetName(bookIndex);
		
		try {
			
			String[] chapters = mContext.getAssets().list(path);
			
			mChapterCountCache[bookIndex] = chapters.length;
			////Trace.d(TAG,"mChapterCountCache[" + bookIndex + "] = " + chapters.length + ";");
			return chapters.length;
			
		} catch(Exception ex) {
			
			//Trace.e(TAG, "Error reading " + path );
			
		}
		
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see bible.kjv.com.data.IBookContents#GetTotalNumberOfChapters()
	 */
	@Override
	public int GetTotalNumberOfChapters() {
		
		return TOTAL_CHAPTER_COUNT;
	}
	
	/* (non-Javadoc)
	 * @see bible.kjv.com.data.IBookContents#GetChapter(java.lang.String, int)
	 */
	@Override
	public ArrayList<String> GetChapter(int bookIndex, int chapter) {
		
		if(mContext == null) {
			//Trace.e(TAG, "Need to call SetContext() first.");
			return new ArrayList<String>();
		}
		
		String path = "Bible/" + BookNameManager.GetName(bookIndex) + "/" +chapter + ".txt";
		InputStream inputStream = null;		
		try {
			
			inputStream = mContext.getAssets().open(path);
			InputStreamReader r = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferReader = new BufferedReader(r);
			ArrayList<String> verses = new ArrayList<String>(60);
			String line;
			
			////Trace.i(TAG, "Reading " + path);
			
			int verseCount = 1;
			//read each line
			while((line = bufferReader.readLine()) != null) {
				verses.add("" + verseCount + "   " + line);
				verseCount++;
			}
			//verses.add(0, "\t" + book + " " + chapter + ":1 - " + (verseCount - 1) );
			
			return verses;
		}catch(Exception ex) {
			
			//Trace.e(TAG, "Error reading " + path );
			
		}finally {
			
			//try and close the buffer
			try {
				if (inputStream != null)
					inputStream.close();
			}catch(IOException ex) { }
		}
		return new ArrayList<String>();
	}
}
