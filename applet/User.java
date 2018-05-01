//============================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//============================

package applet;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javacard.framework.UserException;

public interface User extends Remote
{
    public byte[] getName() throws RemoteException, UserException;
    
    public void setName(byte[] name) throws RemoteException, UserException;
    
    public byte[] getAddress() throws RemoteException, UserException;;
    
    public void setAddress(byte[] address) throws RemoteException, UserException;
    
    public byte[] getEmail() throws RemoteException, UserException;
    
    public void setEmail(byte[] email) throws RemoteException, UserException;
    
    public byte[] getPhone() throws RemoteException, UserException;
    
    public void setPhone(byte[] phone) throws RemoteException, UserException;
	
	public Books getUserBooks() throws RemoteException, UserException; 
	
	public void setPin(byte[] pin) throws RemoteException, UserException; 
	
	public short checkPIN(byte[] pinAttempt) throws RemoteException, UserException;
	
	public short pinIsSet() throws RemoteException, UserException;
}