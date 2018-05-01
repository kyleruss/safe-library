//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

import applet.User;
import com.sun.javacard.clientlib.ApduIOCardAccessor;
import com.sun.javacard.clientlib.CardAccessor;
import com.sun.javacard.rmiclientlib.JCRMIConnect;

public class UserManager 
{
    private static UserManager instance;
    private User activeUser;
    
    private UserManager() {}

    public User getActiveUser() 
    {
        return activeUser;
    }

    public void setActiveUser(User activeUser)
    {
        this.activeUser = activeUser;
    }
    
    public void fetchCardUser() 
    throws Exception
    {
        CardAccessor accessor   =   new SafeLibraryCardAccessor(new ApduIOCardAccessor());
        JCRMIConnect connect    =   new JCRMIConnect(accessor);
        connect.selectApplet(Config.AID, JCRMIConnect.REF_WITH_INTERFACE_NAMES);
        activeUser              =   (User) connect.getInitialReference();
    }
    
    public static UserManager getInstance()
    {
        if(instance == null) instance = new UserManager();
        return instance;
    }
}
