//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

package applet;

import java.rmi.RemoteException;
import javacard.framework.JCSystem;
import javacard.framework.OwnerPIN;
import javacard.framework.SystemException;
import javacard.framework.UserException;
import javacard.framework.Util;
import javacard.framework.service.CardRemoteObject;
import javacard.framework.service.SecurityService;

public class UserImpl implements User
{
    public static final short REQ_DENIED    =   (short) 0x6003;
    public static final short PIN_INVALID   =   (short) 0x6004;
    private byte[] name;
    private byte[] address;
    private byte[] email;
    private byte[] phone;
    private SecurityService securityService;
    private OwnerPIN pin;
    private Books books;
    
    public UserImpl(SecurityService securityService)
    {
        this.securityService    =   securityService;
        books                   =   new BooksImpl();
        CardRemoteObject.export(this);
    }

    public byte[] getName()
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_OUTPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);
        
        return name;
    }
    
    public void setName(byte[] name) 
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_INPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);
              
        JCSystem.beginTransaction();
        this.name = new byte[name.length]; 
        Util.arrayCopyNonAtomic(name, (short) 0, this.name, (short) 0, (short) name.length);
        JCSystem.commitTransaction();

        try {  JCSystem.requestObjectDeletion(); }
        catch (SystemException e) {}
    }
	
	public Books getUserBooks() throws RemoteException, UserException
	{
            if (!securityService.isCommandSecure(SecurityService.PROPERTY_OUTPUT_CONFIDENTIALITY))
                UserException.throwIt(REQ_DENIED);
            
            else if(pinNotValidated())
                UserException.throwIt(PIN_INVALID);
            
            return books;
	}

    public byte[] getAddress() 
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_OUTPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);
        
        return address;
    }

    public void setAddress(byte[] address) 
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_INPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);
        
        JCSystem.beginTransaction();
        this.address = new byte[address.length]; 
        Util.arrayCopyNonAtomic(address, (short) 0, this.address, (short) 0, (short) address.length);
        JCSystem.commitTransaction();

        try {  JCSystem.requestObjectDeletion(); }
        catch (SystemException e) {}
    }

    public byte[] getEmail()
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_OUTPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);
        
        return email;
    }

    public void setEmail(byte[] email) 
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_INPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);
        
        JCSystem.beginTransaction();
        this.email = new byte[email.length]; 
        Util.arrayCopyNonAtomic(email, (short) 0, this.email, (short) 0, (short) email.length);
        JCSystem.commitTransaction();

        try {  JCSystem.requestObjectDeletion(); }
        catch (SystemException e) {}
    }

    public byte[] getPhone() 
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_OUTPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);
        
        return phone;
    }

    public void setPhone(byte[] phone)
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_INPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);
        
        JCSystem.beginTransaction();
        this.phone = new byte[phone.length]; 
        Util.arrayCopyNonAtomic(phone, (short) 0, this.phone, (short) 0, (short) phone.length);
        JCSystem.commitTransaction();

        try {  JCSystem.requestObjectDeletion(); }
        catch (SystemException e) {}
    }

    public void setPin(byte[] pin)
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_INPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);
        
        JCSystem.beginTransaction();
        if(this.pin == null)
            this.pin    =   new OwnerPIN((byte) 5, (byte) pin.length);
        
        this.pin.update(pin, (short) 0, (byte) pin.length);
		
        JCSystem.commitTransaction();
	  
        try {  JCSystem.requestObjectDeletion(); }
        catch (SystemException e) {}
    }

    public short checkPIN(byte[] pinAttempt)
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_INPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);
        
        if(pin == null) return 0;
        else if(pin.check(pinAttempt, (short) 0, (byte) pinAttempt.length)) 
            return 1;
        
        else return 0;
    }
    
    protected boolean pinNotValidated()
    {
        if(pin != null && !pin.isValidated())
            return true;
        
        else return false;
    }
	
    public short pinIsSet() 
    throws RemoteException, UserException
    {
        if (!securityService.isCommandSecure(SecurityService.PROPERTY_OUTPUT_CONFIDENTIALITY))
            UserException.throwIt(REQ_DENIED);
        
        else if(pinNotValidated())
            UserException.throwIt(PIN_INVALID);

        if(pin == null) return 0;
        else return 1;
    }
    
    protected void resetValidation()
    {
        if(pin != null)
            pin.reset();
    }
}