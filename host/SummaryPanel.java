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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import applet.User;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;

public class SummaryPanel extends JPanel
{
    private JLabel titleLabel;
    private JPanel summaryDetailsPanel;
    private JLabel nameLabel, emailLabel, addressLabel, phoneLabel;
    
    public SummaryPanel()
    {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        titleLabel  =   new JLabel("Account summary");
        titleLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "usersicon.png")));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        
        summaryDetailsPanel =   new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameLabel           =   new JLabel();
        emailLabel          =   new JLabel();
        addressLabel        =   new JLabel();
        phoneLabel          =   new JLabel();
        
        nameLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "user.png")));
        emailLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "emailicon.png")));
        addressLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "location.png")));
        phoneLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "phone_bl.png")));
        summaryDetailsPanel.setBackground(Color.WHITE);
        
        JPanel detailsWrapper   =   new JPanel(new MigLayout());
        detailsWrapper.setBackground(Color.WHITE);
        detailsWrapper.add(nameLabel, "wrap, gapy 0 25");
        detailsWrapper.add(emailLabel,  "wrap, gapy 0 25");
        detailsWrapper.add(addressLabel, "wrap, gapy 0 25");
        detailsWrapper.add(phoneLabel);
        
        summaryDetailsPanel.add(detailsWrapper);
        summaryDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 10));
        
        JPanel titlePanel   =   new JPanel();
        titlePanel.add(Box.createRigidArea(new Dimension(Config.WINDOW_WIDTH, 50)));
        titlePanel.add(titleLabel);
        titlePanel.setPreferredSize(new Dimension(0, 150));
        titlePanel.setBackground(Color.WHITE);
        
        add(titlePanel, BorderLayout.NORTH);
        add(summaryDetailsPanel, BorderLayout.CENTER);
    }
    
    public void initSummary()
    {
        User activeUser =   UserManager.getInstance().getActiveUser();
        
        if(activeUser != null)
        {
            try
            {
                nameLabel.setText(new String(activeUser.getName()));
                emailLabel.setText(new String(activeUser.getEmail()));
                phoneLabel.setText(new String(activeUser.getPhone()));
                addressLabel.setText(new String(activeUser.getAddress()));
            }
            
            catch(NullPointerException ex) {}
            
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
