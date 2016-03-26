package bible.kjv.com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
/**
 * 
 * This class is used to parse the bible.
 * Start by calling OpenParse(). When Finished call Close()
 *
 */
public class BibleParser {
	
	@SuppressWarnings("unused")
	private static final String TAG = "BibleParser";
	
	static final String BOOK ="Book ";
	static final int BOOK_PREFIX_SIZE = 8;		//how many characters before the name of the book
	static final String BIBLE_FILENAME="KJV12.TXT";
	static final byte BOOK_COUNT = 66;
	
	//Member vars 
	Context mContext;					///< Context used for file i/o. 
	
	public BibleParser(Context context)
	{
		mContext= context;
	}
	
	public void SplitChapters() throws IOException {
		
		try {
			
			InputStream inputStream = mContext.getAssets().open(BIBLE_FILENAME);
			
			//Trace.i(TAG, "Creating input stream reader...");
			
			//get a buffer reader so we can read each line
			InputStreamReader r = new InputStreamReader(inputStream, "UTF-8");
			
			BufferedReader bufferReader = new BufferedReader(r);
			
			//reset variables
			String line = null;
			int currentChIndex = 1;
			String currentBook = null;
			StringBuilder chapter = new StringBuilder(4096);
			
			Pattern p = Pattern.compile("^([0-9][0-9][0-9]):([0-9][0-9][0-9]) (.*$)");
			while((line = bufferReader.readLine()) != null) {
				
				if(line.startsWith(BOOK)) {
					
					if( chapter.length() > 0) {
						OutputStreamWriter writer = OpenChapter(currentBook, currentChIndex);
						writer.write(chapter.toString());
						chapter.replace(0, chapter.length(), "");
						writer.close();
					}
					
					currentChIndex = 1;
					currentBook = line.substring(BOOK_PREFIX_SIZE);
					
				} else {
					Matcher m = p.matcher(line);
					if(m.matches()) {
						
						int ch = Integer.parseInt(new String(m.group(1).getBytes()));
						//If we enter a new chapter write the file for the previous chapter
						if (ch != currentChIndex) {
							
							OutputStreamWriter writer = OpenChapter(currentBook, currentChIndex);
							writer.write(chapter.toString());
							chapter.replace(0, chapter.length(), "");
							writer.close();
							
							currentChIndex = ch;
						}
						
						//This is not the first verse so add a new line
						if(chapter.length() > 0)
							chapter.append('\n');
						
						String temp = new String(m.group(3).getBytes());
						chapter.append(temp);
						
					} else {
						if(line.length() > 0) {
							chapter.append(' ');
							chapter.append(line.trim());
						}
					}
				}
			}
			//write the last chapter
			if( chapter.length() > 0) {
				OutputStreamWriter writer = OpenChapter(currentBook, currentChIndex);
				writer.write(chapter.toString());
				chapter.replace(0, chapter.length(), "");
				writer.close();
			}
			
			inputStream.close();
			bufferReader.close();
			
		}catch(IOException ex) {
			
			
		} finally {
			
		}
	}
	
	private OutputStreamWriter OpenChapter(String book, int ch) 
			throws IOException {
		
		final String path = Environment.getExternalStorageDirectory().getPath() + "/Bible/";
		File bookDir = new File(path + book);
		if( ! bookDir.exists())
			bookDir.mkdirs();
		
		File chapterFile = new File(bookDir.getAbsoluteFile() + "/" + ch +".txt");
		//Trace.v(TAG, "Creating " + chapterFile.getAbsoluteFile());
		
		if(chapterFile.exists())
			chapterFile.delete();
		
		chapterFile.createNewFile();
		
		FileOutputStream chapterStream = new FileOutputStream(chapterFile); 
		OutputStreamWriter writer = new OutputStreamWriter(chapterStream, "UTF-8");
		
		return writer;
		
	}
}
