package bible.kjv.com.data;

public enum LogFilter {
	All,
	Over30Days,
	Over60Days,
	Over90Days,
	Over6Months,
	Over1Year;
	
    
	public static LogFilter FromInteger(int x) {
       switch(x) {
	       case 0:
				return All; 
			case 1:
				return Over1Year;
			case 2:
				return Over30Days;
			case 3:
				return Over60Days;
			case 4:
				return Over6Months;
			case 5:
				return Over90Days;
			default:
				return All;
       }
    }
	
	public static int ToInteger(LogFilter state) {
        switch(state) {
		case All:
			return 0; 
		case Over1Year:
			return 1;
		case Over30Days:
			return 2;
		case Over60Days:
			return 3;
		case Over6Months:
			return 4;
		case Over90Days:
			return 5;
		default:
			return 0;
      
        }
    }
	
	public static String QueryArg(LogFilter state) {
		switch(state) {
		case All:
			return "-100 years"; 
		case Over1Year:
			return "-1 years";
		case Over30Days:
			return "-30 days";
		case Over60Days:
			return "-60 days";
		case Over6Months:
			return "-6 months";
		case Over90Days:
			return "-90 days";
		default:
			return "-100 years";
      
        }
	}
}