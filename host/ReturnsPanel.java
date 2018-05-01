//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import applet.User;
import applet.Books;

public class ReturnsPanel extends JPanel implements ActionListener
{
    private JLabel titleLabel;
    private JPanel returnContentPanel;
    private JList returnsList;
    private DefaultListModel returnsListModel;
    private JScrollPane returnListScroller;
    private JButton returnBtn;
    private JLabel overdueLabel;
    private int returnTotal;
    
    public ReturnsPanel()
    {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        returnTotal         =   0;
        returnsListModel    =   new DefaultListModel();
        returnsList         =   new JList(returnsListModel);
        titleLabel          =   new JLabel("Returns");
        returnListScroller  =   new JScrollPane(returnsList);
        titleLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "returns.png")));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        returnsList.setCellRenderer(new BookCellRenderer());
        
        returnContentPanel  =   new JPanel(new BorderLayout());
        returnContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 10));
        returnContentPanel.setBackground(Color.WHITE);
        returnContentPanel.add(returnListScroller, BorderLayout.CENTER);
        
        JPanel overduePanel =   new JPanel();
        overdueLabel        =   new JLabel();
        updateOverdueTotal(0);
        
        overdueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        overduePanel.setBackground(Color.WHITE);
        overduePanel.add(overdueLabel);
        returnContentPanel.add(overduePanel, BorderLayout.NORTH);
        
        JPanel controlPanel =   new JPanel();
        returnBtn           =   new JButton(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "returnBookBtn.png")));
        returnBtn.addActionListener(this);
        
        controlPanel.setBackground(Color.WHITE);
        HomePanel.setFullTransparentBtn(returnBtn);
        controlPanel.add(returnBtn);
        returnContentPanel.add(controlPanel, BorderLayout.SOUTH);
        
        JPanel titlePanel   =   new JPanel();
        titlePanel.add(Box.createRigidArea(new Dimension(Config.WINDOW_WIDTH, 50)));
        titlePanel.add(titleLabel);
        titlePanel.setPreferredSize(new Dimension(0, 150));
        titlePanel.setBackground(Color.WHITE);
        
        add(titlePanel, BorderLayout.NORTH);
        add(returnContentPanel, BorderLayout.CENTER);
    }
    
    public void initReturnsList()
    {
        try
        {
            User user   =   UserManager.getInstance().getActiveUser();
            returnsListModel.clear();
            
            if(user != null)
            {
                Books userBooks     =     user.getUserBooks();
                byte[] books        =     userBooks.getBooks();
                int numBooks        =     userBooks.getNumBooks();
                updateOverdueTotal(numBooks);
                
                DatabaseController dbController =   DatabaseController.getInstance();
                for(int i = 0; i < numBooks; i++)
                {
                    int id      =   books[i];
                    Book book   =   dbController.getBookForID(id);   
                    returnsListModel.addElement(book);
                }
            }
        }
        
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public int getReturnTotal() 
    {
        return returnTotal;
    }

    public void setReturnTotal(int returnTotal)
    {
        this.returnTotal = returnTotal;
    }
    
    public void addBookToReturn(Book book)
    {
        returnsListModel.addElement(book);
        updateOverdueTotal(returnTotal + 1);
    }
    
    public void returnBook()
    {
        int index   =   returnsList.getSelectedIndex();
        
        if(index != -1)
        {
            
            try
            {
                User user           =   UserManager.getInstance().getActiveUser();
                Books userBooks     =   user.getUserBooks();
                
                if(userBooks.removeBook((short) index))
                {
                    updateOverdueTotal(returnTotal - 1);
                    returnsListModel.remove(index);
                }
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to return book");
            }
        }
        
        else JOptionPane.showMessageDialog(null, "Please select a book");
    }
    
    public void updateOverdueTotal(int total)
    {
        returnTotal =   total;
        overdueLabel.setText("You have " + total + " overdue book(s)");
    }

    public JList getReturnsList() 
    {
        return returnsList;
    }

    public DefaultListModel getReturnsListModel() 
    {
        return returnsListModel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == returnBtn)
            returnBook();
    }
}
