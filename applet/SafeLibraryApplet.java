//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

package applet;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISOException;
import javacard.framework.service.Dispatcher;
import javacard.framework.service.RMIService;
import javacard.framework.service.SecurityService;

public class SafeLibraryApplet extends Applet
{
    private User remoteUser;
    private Dispatcher rDispatcher;
    private SecurityService securityService;
    private RMIService userService;
    
    public SafeLibraryApplet()
    {
        super();
        initServices();
        initDispatcher();
    }
    
    private void initServices()
    {
        securityService =   new SafeLibrarySecurity();  
        remoteUser      =   new UserImpl(securityService);
        userService     =   new RMIService(remoteUser);
    }
    
    private void initDispatcher()
    {
        rDispatcher     =   new Dispatcher((short) 3);
        rDispatcher.addService(userService, Dispatcher.PROCESS_COMMAND);
        rDispatcher.addService(securityService, Dispatcher.PROCESS_INPUT_DATA);
        rDispatcher.addService(securityService, Dispatcher.PROCESS_OUTPUT_DATA);
    }
    
    public static void install(byte[] bArray, short bOffset, byte bLength) 
    throws ISOException
    {  
        SafeLibraryApplet applet    =   new SafeLibraryApplet();
        byte aidLength              =   bArray[bOffset];
        
        if (aidLength == 0)
            applet.register();
        else
            applet.register(bArray, (short)(bOffset+1), aidLength);   
   }
    @Override
    public void process(APDU apdu) throws ISOException 
    {
        if(rDispatcher != null)
            rDispatcher.process(apdu);
    }
    
    @Override
    public void deselect()
    {
        super.deselect();
        
        if(remoteUser != null)
            ((UserImpl) remoteUser).resetValidation();
    }
}