package applet;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javacard.framework.UserException;

public interface Books extends Remote
{
    public byte[] getBooks() throws RemoteException, UserException;
    
    public void setBooks(byte[] books) throws RemoteException, UserException;
    
    public boolean addBook(byte book) throws RemoteException, UserException;
    
    public boolean removeBook(short index) throws RemoteException, UserException;
    
    public short getNumBooks() throws RemoteException, UserException;
}