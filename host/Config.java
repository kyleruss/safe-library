//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================


public class Config 
{
    private Config() {}
    
    public static final int WINDOW_HEIGHT   =   700;
    
    public static final int WINDOW_WIDTH    =   900;
    
    public static final String APP_NAME     =   "SafeLibrary";
    
    public static final String RES_PATH     =   "/assets/";
    
    public static final String DB_NAME      =   "safelibrary.db";
    
    public static final byte[] AID          =   {0x10, 0x20, 0x30, 0x40, 0x50, 0x03};
	
    public static final int MAX_BOOKS       =	8;
	
    public static final int MAX_ATTEMPTS    =	5;
    
    public static final byte[] DEFAULT_IV   =   new byte[] {66, 49, 70, 39, 120, -90, 81, -83, 60, -19, 6, 123, 53, 91, -80, -89};
    
    public static final byte[] DEFAULT_KEY  =   new byte[] {103, -125, -92, 79, -126, -49, 48, -84, -85, 113, -13, 41, -58, -106, -17, 31};
}
