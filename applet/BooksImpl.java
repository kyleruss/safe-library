//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

package applet;

import java.rmi.RemoteException;
import javacard.framework.UserException;
import javacard.framework.service.CardRemoteObject;
import javacard.framework.JCSystem;
import javacard.framework.SystemException;
import javacard.framework.Util;

public class BooksImpl implements Books
{
    private byte[] books;
    private short numBooks;
    
    public BooksImpl()
    {
        numBooks    =   0;
        books       =   new byte[8];
        CardRemoteObject.export(this);
    }

    public byte[] getBooks()
    throws RemoteException, UserException
    {
        return books;
    }

    public void setBooks(byte[] books) 
    throws RemoteException, UserException
    {
        JCSystem.beginTransaction();
	    this.books = new byte[books.length]; 
	    Util.arrayCopyNonAtomic(books, (short) 0, this.books, (short) 0, (short) books.length);
	    JCSystem.commitTransaction();
	  
	    try {  JCSystem.requestObjectDeletion(); }
	    catch (SystemException e) {}
    }
    
    public boolean addBook(byte book) 
    throws RemoteException, UserException
    {
        if(numBooks >= books.length) return false;
        
        else
        {
			JCSystem.beginTransaction();
            books[numBooks] = book;
            numBooks++;
			JCSystem.commitTransaction();
            return true;
        }
    }
    
    public boolean removeBook(short index) 
    throws RemoteException, UserException
    {
        if(index < 0 || index > numBooks) return false;
        else
        {
            JCSystem.beginTransaction();
            if(numBooks == 1)
            {
                books[index] = -1;
                numBooks--;
            }

            else
            {
                    books[index]	=	books[(short)(numBooks - 1)];
                    books[(short) (numBooks -1)]  =	(short) -1;
                    numBooks--;
            }

            JCSystem.commitTransaction();
            return true;
        }
    }
    
    public short getNumBooks()
    throws RemoteException, UserException
    {
        return numBooks;
    }
	
	public short canAddMoreBooks() 
	throws RemoteException, UserException
	{
		if(numBooks < books.length) return 1;
		else return 0;
	}
}