package bible.kjv.com.util;

import android.util.Log;

public class Trace {
    public static final int    NONE                         = 0;
    public static final int    ERRORS_ONLY                  = 1;
    public static final int    ERRORS_WARNINGS              = 2;
    public static final int    ERRORS_WARNINGS_INFO         = 3;
    public static final int    ERRORS_WARNINGS_INFO_DEBUG   = 4;

    private static final int          LOGGING_LEVEL   = NONE;        // Errors + warnings + info + debug (default)

    @SuppressWarnings("unused")
	public static void e(String tag, String msg)
    {
        if ( LOGGING_LEVEL >=1) Log.e(tag,msg);
    }

    @SuppressWarnings("unused")
    public static void e(String tag, String msg, Exception e)
    {
        if ( LOGGING_LEVEL >=1) Log.e(tag,msg,e);
    }
    
    @SuppressWarnings("unused")
    public static void w(String tag, String msg)
    {
        if ( LOGGING_LEVEL >=2) Log.w(tag, msg);
    }

    @SuppressWarnings("unused")
    public static void i(String tag, String msg)
    {
        if ( LOGGING_LEVEL >=3) Log.i(tag,msg);
    }

    @SuppressWarnings("unused")
    public static void d(String tag, String msg)
    {
        if ( LOGGING_LEVEL >=4) Log.d(tag, msg);
    }
}
