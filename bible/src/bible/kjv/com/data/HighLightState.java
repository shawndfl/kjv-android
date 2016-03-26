package bible.kjv.com.data;

public enum HighLightState 
{
		None,		///< No color.
		Note,		///< Blue.
		Like,		///< Yellow.
		Love,		///< Green.
		All;		///< Used for filtering
		
		public static HighLightState FromInteger(int x) {
	        switch(x) {
	        case 0:
	            return None;
	        case 1:
	            return Note;
	        case 2:
	        	return Like;
	        case 3:
	        	return Love;
	        case 4:
	        	return All;
	        default:
	        	return Note;
	        }
	    }
		
		public static int ToInteger(HighLightState state) {
	        switch(state) {
	        case None:
	            return 0;
	        case Note:
	            return 1;
	        case Like:
	        	return 2;
	        case Love:
	        	return 3;
	        case All:
	        	return 4;
	        default:
	        	return 0;
	        }
	    }
}
