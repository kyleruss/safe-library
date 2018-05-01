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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import applet.User;
import javax.swing.JOptionPane;

public class SettingsPanel extends JPanel implements ActionListener
{
    private JLabel titleLabel;
    private JPanel summaryDetailsPanel;
    private JLabel nameLabel, emailLabel, addressLabel, phoneLabel, codeLabel;
    private JTextField nameField, emailField, addressField, phoneField, codeField;
    private JButton saveBtn;
    
    public SettingsPanel()
    {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        titleLabel  =   new JLabel("Settings");
        titleLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "settings.png")));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        
        summaryDetailsPanel =   new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameLabel           =   new JLabel("Name");
        emailLabel          =   new JLabel("Email");
        addressLabel        =   new JLabel("Address");
        phoneLabel          =   new JLabel("Phone");
        codeLabel           =   new JLabel("Code");
        nameField           =   new JTextField();
        emailField          =   new JTextField();
        addressField        =   new JTextField();
        phoneField          =   new JTextField();
        codeField           =   new JTextField();
        saveBtn             =   new JButton();
        
        saveBtn.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "submit_image.png")));
        HomePanel.setFullTransparentBtn(saveBtn);
        
        nameField.setPreferredSize(new Dimension(200, 30));
        emailField.setPreferredSize(new Dimension(200, 30));
        addressField.setPreferredSize(new Dimension(200, 30));
        phoneField.setPreferredSize(new Dimension(200, 30));
        codeField.setPreferredSize(new Dimension(200, 30));
        
        nameLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "user.png")));
        emailLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "emailicon.png")));
        addressLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "location.png")));
        phoneLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "phone_bl.png")));
        codeLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "passwordlock.png")));
        summaryDetailsPanel.setBackground(Color.WHITE);
        
        JPanel detailsWrapper   =   new JPanel(new MigLayout());
        detailsWrapper.setBackground(Color.WHITE);
        detailsWrapper.add(nameLabel, "gapx 0 60");
        detailsWrapper.add(nameField, "wrap, gapy 0 25");
        detailsWrapper.add(emailLabel);
        detailsWrapper.add(emailField, "wrap, gapy 0 25");
        detailsWrapper.add(addressLabel);
        detailsWrapper.add(addressField, "wrap, gapy 0 25");
        detailsWrapper.add(phoneLabel);
        detailsWrapper.add(phoneField, "wrap, gapy 0 25");
        detailsWrapper.add(codeLabel);
        detailsWrapper.add(codeField, "wrap, gapy 0 50");
        detailsWrapper.add(new JLabel());
        detailsWrapper.add(saveBtn, "");
        
        summaryDetailsPanel.add(detailsWrapper);
        summaryDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 10));
        
        JPanel titlePanel   =   new JPanel();
        titlePanel.add(Box.createRigidArea(new Dimension(Config.WINDOW_WIDTH, 50)));
        titlePanel.add(titleLabel);
        titlePanel.setPreferredSize(new Dimension(0, 150));
        titlePanel.setBackground(Color.WHITE);
        
        add(titlePanel, BorderLayout.NORTH);
        add(summaryDetailsPanel, BorderLayout.CENTER);
        
        saveBtn.addActionListener(this);
    }
    
    public void initSettings()
    {
        User activeUser =   UserManager.getInstance().getActiveUser();
        
        if(activeUser != null)
        {
            try
            {
                nameField.setText(new String(activeUser.getName()));
                emailField.setText(new String(activeUser.getEmail()));
                phoneField.setText(new String(activeUser.getPhone()));
                addressField.setText(new String(activeUser.getAddress()));
            }
            
            catch(NullPointerException ex) {}
            
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }   
    }
    
    public void saveSettings()
    {
        String name         =   nameField.getText();
        String email        =   emailField.getText();
        String phone        =   phoneField.getText();
        String address      =   addressField.getText();
        String code         =   codeField.getText();
        
        User activeUser     =   UserManager.getInstance().getActiveUser();
        
        if(activeUser != null)
        {
            try
            {
                if(code != null && !code.equals(""))
                {
                    if(code.length() != 5)
                    {
                        JOptionPane.showMessageDialog(null, "Invalid pin length");
                        return;
                    }
                    
                    else activeUser.setPin(code.getBytes());
                }
                
                activeUser.setName(name.getBytes());
                activeUser.setEmail(email.getBytes());
                activeUser.setPhone(phone.getBytes());
                activeUser.setAddress(address.getBytes());
                
                
                JOptionPane.showMessageDialog(null, "Settings have been saved");
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to save settings");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        if(src == saveBtn)
            saveSettings();
    }
}
