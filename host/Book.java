//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================


import java.util.Date;
import javax.swing.ImageIcon;

public class Book
{
    private int id;
    private String title;
    private String author;
    private Date dueDate;
    private ImageIcon bookImage;
    private int stock;
    
    public Book(int id, String title, String author)
    {
        this.id     =   id;
        this.title  =   title;
        this.author =   author;
        stock       =   0;
    }

    public String getTitle() 
    {
        return title;
    }

    public Book setTitle(String title) 
    {
        this.title = title;
        return this;
    }

    public String getAuthor() 
    {
        return author;
    }

    public Book setAuthor(String author) 
    {
        this.author = author;
        return this;
    }

    public Date getDueDate()
    {
        return dueDate;
    }

    public Book setDueDate(Date dueDate) 
    {
        this.dueDate = dueDate;
        return this;
    }

    public ImageIcon getBookImage()
    {
        return bookImage;
    }

    public Book setBookImage(ImageIcon bookImage) 
    {
        this.bookImage = bookImage;
        return this;
    }

    public int getStock() 
    {
        return stock;
    }
    
    public Book setStock(int stock) 
    {
        this.stock = stock;
        return this;
    }

    public int getId() 
    {
        return id;
    }

    public void setId(int id) 
    {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Book)
            return ((Book) other).getId() == id;
        
        else return false;
    }
}
