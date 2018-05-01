//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.text.DateFormatter;
import net.miginfocom.swing.MigLayout;

public class BookCellRenderer implements ListCellRenderer
{
    private boolean displayStock;
    
    public BookCellRenderer()
    {
        this(false);
    }
    
    public BookCellRenderer(boolean displayStock)
    {
        this.displayStock   =   displayStock;
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
    {
        Book book           =   (Book) value;
        JPanel component    =   new JPanel(new BorderLayout());
        String name         =   book.getTitle();
        String author       =   book.getAuthor();
        Date due            =   book.getDueDate();
        ImageIcon bookImg   =   book.getBookImage();
        
        JPanel componentImagePanel      =   new JPanel();
        JPanel componentDetailsPanel    =   new JPanel(new MigLayout());

        Color componentBG   =   isSelected? new Color(67,63,74): Color.WHITE;
        componentImagePanel.setBackground(componentBG);
        componentDetailsPanel.setBackground(componentBG);
        component.setBackground(componentBG);
        component.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        componentImagePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        
        componentImagePanel.add(new JLabel(bookImg));
        JLabel nameLabel    =   new JLabel(name);
        JLabel authorLabel  =   new JLabel(author);
        nameLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "classes_icon.png")));
        authorLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "editicon.png")));
        
        Color componentForeground   =  isSelected? Color.WHITE : Color.BLACK; 
        nameLabel.setForeground(componentForeground);
        authorLabel.setForeground(componentForeground);
        
        componentDetailsPanel.add(nameLabel, "wrap");
        componentDetailsPanel.add(authorLabel, "wrap");
        
        component.add(componentImagePanel, BorderLayout.WEST);
        component.add(componentDetailsPanel, BorderLayout.CENTER);
        
        if(due != null)
        {
            String dueStr       =   new SimpleDateFormat("dd/MM/yyyy").format(due);
            JLabel dueLabel     =   new JLabel(dueStr);
            dueLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "calendar_small.png")));
            dueLabel.setForeground(componentForeground);
            componentDetailsPanel.add(dueLabel, "wrap");
        }
        
        if(displayStock)
        {
            JLabel stockLabel   =   new JLabel(book.getStock() + " in stock");
            stockLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "department_icon.png")));
            stockLabel.setForeground(componentForeground);
            componentDetailsPanel.add(stockLabel);
        }
                    
        return component;
    }
}
