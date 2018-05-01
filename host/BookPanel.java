//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BookPanel extends JPanel implements ActionListener
{
    private JLabel titleLabel;
    private JPanel bookContentPanel;
    private JList bookList;
    private DefaultListModel bookListModel;
    private JButton addBtn;
    
    public BookPanel()
    {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        bookListModel   =   new DefaultListModel();
        bookList        =   new JList(bookListModel);
        titleLabel      =   new JLabel("Books");
        titleLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "books_icon.png")));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        
        bookContentPanel  =   new JPanel(new BorderLayout());
        bookContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 10));
        bookContentPanel.setBackground(Color.WHITE);
        
        bookList.setCellRenderer(new BookCellRenderer(true));
        JScrollPane bookScrollPane  =   new JScrollPane(bookList);
        bookScrollPane.setPreferredSize(new Dimension(500, 300));
        bookContentPanel.add(bookScrollPane, BorderLayout.CENTER);
        initBookList();
        
        JPanel controlPanel =   new JPanel();
        addBtn              =   new JButton(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "cartBtn.png")));
        addBtn.addActionListener(this);
        
        HomePanel.setFullTransparentBtn(addBtn);
        controlPanel.add(addBtn);
        controlPanel.setBackground(Color.WHITE);
        bookContentPanel.add(controlPanel, BorderLayout.SOUTH);
        
        JPanel titlePanel   =   new JPanel();
        titlePanel.add(Box.createRigidArea(new Dimension(Config.WINDOW_WIDTH, 50)));
        titlePanel.add(titleLabel);
        titlePanel.setPreferredSize(new Dimension(0, 150));
        titlePanel.setBackground(Color.WHITE);
        
        add(titlePanel, BorderLayout.NORTH);
        add(bookContentPanel, BorderLayout.CENTER);
		initBookList();
    }
    
    private void initBookList()
    {
        List<Book> books    =   DatabaseController.getInstance().getBooks();
        for(Book book : books)
            bookListModel.addElement(book);
    }
    
    public void addCartItem()
    {
        Object selectedValue    =   bookList.getSelectedValue();
        
        if(selectedValue != null)
        {
            Book selectedBook   =   (Book) selectedValue;
            
            if(selectedBook.getStock() <= 0)
                JOptionPane.showMessageDialog(null, "Not enough stock");
            
            else
            {
                HomePanel.getInstance().getCheckoutPanel().addBookToCheckout(selectedBook);
                JOptionPane.showMessageDialog(null, "Book has been added to your cart");
            }
        }
        
        else JOptionPane.showMessageDialog(null, "Please select a book");
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == addBtn)
            addCartItem();
    }
}
