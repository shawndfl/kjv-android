package bible.kjv.com.data;

/*
 * Used to convert names to ids and ids to string names.
 */
public class BookNameManager {
	
	@SuppressWarnings("unused")
	private static final String TAG = "BookNameManager";
	private static String[] mNames;
	
	private BookNameManager()
	{
		
	}
	
	public static void SetNameArray(String[] names)
	{
		mNames = names;
	}
	
	public static String[] GetNames() {
		return mNames.clone();
	}
	
	public static String GetName(int id)
	{
		
		if(id < 0 || id >= mNames.length){
			return null;
		}
		else {
			return mNames[id];
		}
	}
	
	public static int GetId(String book)
	{
		for(int i = 0; i < mNames.length; i++ )
		{
			if(mNames[i].equals(book))
				return i;
		}
		//Trace.e(TAG,	"Cannot find book " + book );
		return -1;
	}
	
}
