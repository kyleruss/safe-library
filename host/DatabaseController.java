//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class DatabaseController 
{
    private static DatabaseController instance;
    
    private DatabaseController()
    {
        initDriver();
        initTables();
    }
    
    private void initDriver()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private Connection initConnection()
    {
        try
        {
            return DriverManager.getConnection("jdbc:sqlite:" + Config.DB_NAME);
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    private String getBooksTableSQL()
    {
        String sql  =   "CREATE TABLE IF NOT EXISTS books "
                + "("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title VARCHAR(255), "
                + "author VARCHAR(100),"
                + "stock INTEGER DEFAULT 0,"
                + "book_picture VARCHAR(255)"
                + ");";
        
        return sql;
    }
    
    private void initTables()
    {
        Connection conn =   initConnection();
        
        if(conn != null)
        {
            try (Statement statement = conn.createStatement()) 
            {
                String booksTableSQL    =   getBooksTableSQL();

                statement.execute(booksTableSQL);
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void initBooks()
    {
        Connection conn     =   initConnection();
        
        if(conn != null)
        {
            String sql  =   "INSERT INTO books (title, author, stock, book_picture) VALUES "
                    + "('Dreaming of Sweaden', 'Andrew Brown', 5, 'book1.jpg'),"
                    + "('The maker of swans', 'Paraic ODonnell', 10, 'book2.jpg'),"
                    + "('The Caribean of Costa Rica', 'Gill Philean', 3, 'book3.jpg'),"
                    + "('LException', 'Auoror Olafsdottir', 2, 'book4.jpg'),"
                    + "('The sea around us', 'Rachel Carson', 4, 'book5.jpg'),"
                    + "('A Game of Thrones', 'George R.R Martin', 8, 'book6.jpg'),"
                    + "('Harry Potter and the Half blood prince', 'J.K. Rowling', 6, 'book7.jpg'),"
                    + "('Enchantment', 'Guy Kawasaki', 9, 'book8.jpg');";
            
            try(Statement statement     =   conn.createStatement())
            {
                statement.executeUpdate(sql);
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public Book getBookForID(int id)
    {
        Connection conn     =   initConnection();
        
        if(conn != null)
        {
            try
            {
                String query        =   "SELECT * FROM books WHERE ID = " + id + ";";
                Statement statement =   conn.createStatement();
                ResultSet results   =   statement.executeQuery(query);
                
                if(results.next())
                {
                    String title    =   results.getString("title");
                    String author   =   results.getString("author"); 
                    ImageIcon img   =   new ImageIcon(this.getClass().getResource(Config.RES_PATH + "books/" + results.getString("book_picture")));
                    int stock       =   results.getInt("stock");
                    Book book       =   new Book(id, title, author);
                    book.setBookImage(img);
                    book.setStock(stock);
                    
                    return book;
                }
                
                else return null;
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
        
        else return null;
    }
    
    public List<Book> getBooks()
    {
        List<Book> bookList =   new ArrayList<>();
        Connection conn     =   initConnection();
        
        if(conn != null)
        {
            try
            {
                String query        =   "SELECT * FROM books;";
                Statement statement =   conn.createStatement();
                ResultSet results   =   statement.executeQuery(query);

                while(results.next())
                {
                    int id          =   results.getInt("ID");
                    String title    =   results.getString("title");
                    String author   =   results.getString("author");
                    ImageIcon img   =   new ImageIcon(this.getClass().getResource(Config.RES_PATH + "books/" + results.getString("book_picture")));
                    int stock       =   results.getInt("stock");
                    Book book       =   new Book(id, title, author);
                    book.setBookImage(img);
                    book.setStock(stock);
                    
                    bookList.add(book);
                }
                
                conn.close();
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return bookList;
    }
    
    
    public void updateBookStock(Book book, int stock)
    {
        updateBookStock(book.getId(), stock);
    }
    
    public void updateBookStock(int bookID, int stock)
    {
        Connection conn     =   initConnection();
        
        if(conn != null)
        {
            try
            {
                String sql    =   "UPDATE books SET stock = " + stock + " WHERE ID = " + bookID + ";";
                try (Statement stmt = conn.createStatement()) 
                {
                    stmt.executeUpdate(sql);
                    conn.close();
                }
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    
    public static DatabaseController getInstance()
    {
        if(instance == null) instance   =   new DatabaseController();
        return instance;
    }
    
    public static void main(String[] args)
    {
        DatabaseController.getInstance().initBooks();
    }
}
