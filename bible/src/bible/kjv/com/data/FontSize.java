package bible.kjv.com.data;

public enum FontSize {
	Small,
	Mid,
    Large;
    
	public static FontSize FromInteger(int x) {
        switch(x) {
        case 0:
            return Small;
        case 1:
            return Mid;
        case 2:
        	return Large;
        default:
        	return Small;
        }
    }
	
	public static int ToInteger(FontSize state) {
        switch(state) {
        case Small:
            return 0;
        case Mid:
            return 1;
        case Large:
        	return 2;
        default:
        	return 0;
        }
    }
}