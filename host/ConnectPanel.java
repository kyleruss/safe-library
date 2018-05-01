//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import applet.User;
import javax.swing.Timer;


public class ConnectPanel extends JPanel implements ActionListener
{
    public static final String PROCESSING_VIEW  =   "process_v";
    public static final String CODE_VIEW        =   "code_v";
    
    private JLabel titleLabel;
    private JLabel processLabel;
    private JLabel processMessageLabel;
    private JLabel codeLabel;
    private JTextField codeField;
    private JPanel codePanel, processingPanel, connectChangePanel;
    private JButton codeSubmit;
    private int attemptCount;
    
    public ConnectPanel()
    {
        setBackground(Color.WHITE);
        
        titleLabel          =   new JLabel(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "title.png")));
        processLabel        =   new JLabel(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "spinner3.gif")));   
        processMessageLabel =   new JLabel("Processing... Please wait one moment");  
        codeLabel           =   new JLabel(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "passwordlock.png")));
        codeField           =   new JTextField();
        codeSubmit          =   new JButton(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "submit_image.png")));
        attemptCount        =   0;
        
        HomePanel.setFullTransparentBtn(codeSubmit);
        codeSubmit.addActionListener(this);
        
        processMessageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        processMessageLabel.setForeground(Color.DARK_GRAY);
        processMessageLabel.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "info2.png")));
        codeField.setPreferredSize(new Dimension(200, 50));
        
        connectChangePanel  =   new JPanel(new CardLayout());
        processingPanel     =   new JPanel();
        processingPanel.setLayout(new BoxLayout(processingPanel, BoxLayout.Y_AXIS));
        codePanel           =   new JPanel();
        
        JPanel processingWrapper    =   new JPanel();
        processingWrapper.setBackground(Color.WHITE);
        processingWrapper.add(processingPanel);
        
        connectChangePanel.setBackground(Color.WHITE);
        processingPanel.setBackground(Color.WHITE);
        codePanel.setBackground(Color.WHITE);
        
        processingPanel.add(processLabel);
        processingPanel.add(processMessageLabel);
        
        codePanel.add(codeLabel);
        codePanel.add(codeField);
        codePanel.add(codeSubmit);
        
        connectChangePanel.add(processingWrapper, PROCESSING_VIEW);
        connectChangePanel.add(codePanel, CODE_VIEW);
        
        add(Box.createRigidArea(new Dimension(Config.WINDOW_WIDTH, 150)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(Config.WINDOW_WIDTH, 30)));
        add(connectChangePanel);
    }
    
    public void handleInitialConnection()
    {
        changeView(PROCESSING_VIEW);
            
        Timer timer =   new Timer(3000, (ActionEvent e) ->
        {
            try
             {
                UserManager userManager =   UserManager.getInstance();
                userManager.fetchCardUser();
                User user   =   userManager.getActiveUser();

                if(user != null)
                {
                    if(user.pinIsSet() == (short) 1)
                        changeView(CODE_VIEW);
                    else
                    {
                        MainPanel mainPanel =   MainPanel.getInstance();
                        mainPanel.changeView(MainPanel.HOME_VIEW);
                        mainPanel.getHomePanel().showSettings();
                    }
                }

                else 
                {
                    changeView(PROCESSING_VIEW);
                    JOptionPane.showMessageDialog(null, "Failed to connect to card");
                }
             }

            catch(Exception ex)
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to connect to card");
            }
        });

        timer.setRepeats(false);
        timer.start();
    }
    
    public void changeView(String cardName)
    {
        CardLayout cLayout  =   (CardLayout) connectChangePanel.getLayout();
        cLayout.show(connectChangePanel, cardName);
    }
    
    private void verifyCode()
    {
        changeView(PROCESSING_VIEW);
        
        Timer timer =   new Timer(3000, (ActionEvent e) ->
        {
            try
            {
                if(attemptCount >= Config.MAX_ATTEMPTS)
				{
                    JOptionPane.showMessageDialog(null, "You have tried to connect too many times");
					changeView(CODE_VIEW);
					return;
				}
                
                String code         =   codeField.getText();
                User user           =   UserManager.getInstance().getActiveUser();

                if(user != null)
                {

                    if(user.checkPIN(code.getBytes()) == 1)
                    {
                        MainPanel mainPanel =   MainPanel.getInstance();
                        mainPanel.changeView(MainPanel.HOME_VIEW);
                        mainPanel.getHomePanel().getSummaryPanel().initSummary();
                        attemptCount    =   0;
                    }

                    else 
                    {
                        JOptionPane.showMessageDialog(null, "Invalid PIN code");
                        attemptCount++;
                        changeView(CODE_VIEW);
                    } 
                }
            }
            
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Invalid code format");
                changeView(CODE_VIEW);
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == codeSubmit)
            verifyCode();
    }
}
