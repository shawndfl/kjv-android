package bible.kjv.com.data;

public enum HighLightSort 
{
		Book,	
		Date;	
		
		public static HighLightSort FromInteger(int x) {
	        switch(x) {
	        case 0:
	            return Book;
	        case 1:
	            return Date;
	        default:
	        	return Book;
	        }
	    }
		
		public static int ToInteger(HighLightSort state) {
	        switch(state) {
	        case Book:
	            return 0;
	        case Date:
	            return 1;
	        default:
	        	return 0;
	        }
	    }
}
