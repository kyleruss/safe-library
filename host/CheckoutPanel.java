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
import java.util.ArrayList;
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

public class CheckoutPanel extends JPanel implements ActionListener
{
    private JLabel titleLabel;
    private JPanel returnContentPanel;
    private JList checkoutList;
    private DefaultListModel checkoutListModel;
    private JScrollPane checkoutScrollPane;
    private JButton checkoutBtn, removeBtn;
    private JLabel checkoutLabel;
    private int cartTotal;
    
    public CheckoutPanel()
    {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        cartTotal           =   0;
        checkoutListModel   =   new DefaultListModel();
        checkoutList        =   new JList(checkoutListModel); 
        checkoutScrollPane  =   new JScrollPane(checkoutList);
        titleLabel          =   new JLabel("Checkout");
        
        titleLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "checkout-dark.png")));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        checkoutList.setCellRenderer(new BookCellRenderer(true));
        
        returnContentPanel  =   new JPanel(new BorderLayout());
        returnContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 10));
        returnContentPanel.setBackground(Color.WHITE);
        returnContentPanel.add(checkoutScrollPane, BorderLayout.CENTER);
        
        JPanel checkoutTitlePanel   =   new JPanel();
        checkoutLabel               =   new JLabel();
        updateCartTotal(0);
        
        checkoutLabel.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "cart.png")));
        checkoutTitlePanel.setBackground(Color.WHITE);
        checkoutTitlePanel.add(checkoutLabel);
        returnContentPanel.add(checkoutTitlePanel, BorderLayout.NORTH);
        
        JPanel controlPanel =   new JPanel();
        checkoutBtn         =   new JButton(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "submit_image.png")));
        removeBtn           =   new JButton(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "removeBtn.png")));
        checkoutBtn.addActionListener(this);
        removeBtn.addActionListener(this);
        
        controlPanel.setBackground(Color.WHITE);
        HomePanel.setFullTransparentBtn(checkoutBtn);
        HomePanel.setFullTransparentBtn(removeBtn);
        controlPanel.add(removeBtn);
        controlPanel.add(checkoutBtn);
        returnContentPanel.add(controlPanel, BorderLayout.SOUTH);
        
        JPanel titlePanel   =   new JPanel();
        titlePanel.add(Box.createRigidArea(new Dimension(Config.WINDOW_WIDTH, 50)));
        titlePanel.add(titleLabel);
        titlePanel.setPreferredSize(new Dimension(0, 150));
        titlePanel.setBackground(Color.WHITE);
        
        add(titlePanel, BorderLayout.NORTH);
        add(returnContentPanel, BorderLayout.CENTER);
    }

    public int getCartTotal()
    {
        return cartTotal;
    }

    public void setCartTotal(int cartTotal) 
    {
        this.cartTotal = cartTotal;
    }
    
    public void removeCartItem()
    {
        int index    =   checkoutList.getSelectedIndex();
        
        if(index != -1)
        {
            removeCheckoutBook(index);
            int nextTotal   =   cartTotal - 1;
            updateCartTotal(nextTotal);
        }
        
        else JOptionPane.showMessageDialog(null, "Select a book to remove");
    }
    
    public void commitCartTransaction()
    {
        User user           =   UserManager.getInstance().getActiveUser();
        
        if(user != null)
        {
            try
            {
                Books userBooks         =   user.getUserBooks();
                int currentBookCount    =   userBooks.getNumBooks();
                
                if((currentBookCount + checkoutListModel.getSize()) > Config.MAX_BOOKS)
                    JOptionPane.showMessageDialog(null, "You have selected too many books, return some and come back");
                
                else
                {
                    for(int i = 0; i < checkoutListModel.getSize(); i++)
                    {
                        int id  =   ((Book) checkoutListModel.get(i)).getId();
                        userBooks.addBook((byte) id);
                    }
                    
                    checkoutListModel.clear();
                    updateCartTotal(0);
                    JOptionPane.showMessageDialog(null, "The checkout items have been added");
                }
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to complete cart transaction");
            }
        }
    }
    
    public void updateCartTotal(int total)
    {
        cartTotal   =   total;
        checkoutLabel.setText("You have " + total + " item(s) in your cart");
    }

    public JList getCheckoutList()
    {
        return checkoutList;
    }

    public DefaultListModel getCheckoutListModel() 
    {
        return checkoutListModel;
    }
    
    public void addBookToCheckout(Book book)
    {
        checkoutListModel.addElement(book);
        updateCartTotal(cartTotal + 1);
    }
    
    public void removeCheckoutBook(int index)
    {
        checkoutListModel.remove(index);
    }
    
    public void removeCheckoutBook(Book book)
    {
        checkoutListModel.removeElement(book);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == removeBtn)
            removeCartItem();
        
        else if(src == checkoutBtn)
            commitCartTransaction();
    }
}
