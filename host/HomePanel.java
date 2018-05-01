//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class HomePanel extends JPanel
{
    public static final String SETTINGS_VIEW   =   "settings_v";
    public static final String BOOK_VIEW       =   "book_v";
    public static final String SUMMARY_VIEW    =   "summary_v";
    public static final String CHECKOUT_VIEW   =   "checkout_v";
    public static final String RETURNS_VIEW    =   "returns_v";
    
    private static HomePanel instance;
    private SettingsPanel settingsPanel;
    private BookPanel bookPanel;
    private SummaryPanel summaryPanel;
    private CheckoutPanel checkoutPanel;
    private ReturnsPanel returnsPanel;
    private HomeContainer homeContainer;
    private NavigationPanel navigationPanel;
    
    private HomePanel()
    {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        homeContainer   =   new HomeContainer();
        navigationPanel =   new NavigationPanel();
        
        add(navigationPanel, BorderLayout.WEST);
        add(homeContainer, BorderLayout.CENTER);
    }

    public SettingsPanel getSettingsPanel() 
    {
        return settingsPanel;
    }

    public BookPanel getBookPanel() 
    {
        return bookPanel;
    }

    public SummaryPanel getSummaryPanel() 
    {
        return summaryPanel;
    }

    public CheckoutPanel getCheckoutPanel()
    {
        return checkoutPanel;
    }

    public ReturnsPanel getReturnsPanel() 
    {
        return returnsPanel;
    }
    
    public void changeView(String cardName)
    {
        homeContainer.changeView(cardName);
    }
    
    public void disconnect()
    {
        MainPanel.getInstance().changeView(MainPanel.CONNECT_VIEW);
        changeView(SUMMARY_VIEW);
    }
    
    public static void setTransparentBtn(JButton btn)
    {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusable(false);
    }
    
    public static void setFullTransparentBtn(JButton btn)
    {
        setTransparentBtn(btn);
        btn.setBorderPainted(false);
    }
	
    public void showSettings()
    {
        changeView(SETTINGS_VIEW);
        navigationPanel.setActive(navigationPanel.settingsBtn);
        settingsPanel.initSettings();
    }
    
    public void showSummary()
    {
        changeView(SUMMARY_VIEW);
        navigationPanel.setActive(navigationPanel.summaryBtn);
        summaryPanel.initSummary();
    }
    
    public void showReturns()
    {
        changeView(RETURNS_VIEW);
        navigationPanel.setActive(navigationPanel.returnsBtn);
        returnsPanel.initReturnsList();
    }
    
    private class NavigationPanel extends JPanel implements ActionListener
    {
        private JPanel navContainer, disconnectPanel;
        private JButton settingsBtn, bookBtn, summaryBtn, checkoutBtn, returnsBtn;
        private JButton activeBtn, disconnectBtn;
        
        public NavigationPanel()
        {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(200, 0));
            setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
            setBackground(Color.WHITE);
            
            navContainer    =   new JPanel(new GridLayout(5, 1));
            settingsBtn     =   new JButton("Settings");
            bookBtn         =   new JButton("Books");
            checkoutBtn     =   new JButton("Checkout");
            summaryBtn      =   new JButton("Summary");
            returnsBtn      =   new JButton("Returns");
            disconnectBtn   =   new JButton("Disconnect");
            
            
            settingsBtn.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "settings.png")));
            bookBtn.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "books_icon.png")));
            summaryBtn.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "usersicon.png")));
            checkoutBtn.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "checkout-dark.png")));
            returnsBtn.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "returns.png")));
            disconnectBtn.setIcon(new ImageIcon(this.getClass().getResource(Config.RES_PATH + "User_logout.png")));
            
            setClearButton(settingsBtn);
            setClearButton(bookBtn);
            setClearButton(summaryBtn);
            setClearButton(checkoutBtn);
            setClearButton(returnsBtn);
            setClearButton(disconnectBtn);
            
            disconnectPanel =   new JPanel(new BorderLayout());
            disconnectPanel.setBackground(Color.WHITE);
            disconnectBtn.setFont(new Font("Arial", Font.BOLD, 12));
            disconnectPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
            disconnectBtn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            disconnectPanel.add(disconnectBtn);
            
            navContainer.add(summaryBtn);
            navContainer.add(bookBtn);
            navContainer.add(settingsBtn);
            navContainer.add(checkoutBtn);
            navContainer.add(returnsBtn);

            navContainer.setBackground(Color.WHITE);
            navContainer.setPreferredSize(new Dimension(0, 500));
            add(navContainer, BorderLayout.NORTH);
            add(disconnectPanel, BorderLayout.SOUTH);
            
            settingsBtn.addActionListener(this);
            bookBtn.addActionListener(this);
            summaryBtn.addActionListener(this);
            checkoutBtn.addActionListener(this);
            returnsBtn.addActionListener(this);
            disconnectBtn.addActionListener(this);
            setActive(summaryBtn);
        }
        
        private void setActive(JButton btn)
        {
            if(activeBtn != null)
            {
                activeBtn.setOpaque(false);
                activeBtn.setForeground(Color.BLACK);
            }
            
            btn.setOpaque(true);
            btn.setBackground(new Color(67,63,74));
            btn.setForeground(Color.WHITE);
            activeBtn   =   btn;
        }
        
        private void setClearButton(JButton btn)
        {
            setTransparentBtn(btn);
            btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
            btn.setFont(new Font("Arial", Font.BOLD, 20));
        }
        

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src  =   e.getSource();
            
            if(src == settingsBtn || src == bookBtn || src == summaryBtn || src == checkoutBtn || src == returnsBtn)
                setActive((JButton) src);
            
            if(src == settingsBtn)
                showSettings();
            
            else if(src == bookBtn)
                changeView(BOOK_VIEW);
            
            else if(src == summaryBtn)
                showSummary();
            
            else if(src == checkoutBtn)
                changeView(CHECKOUT_VIEW);
            
            else if(src == returnsBtn)
                showReturns();
            
            else if(src == disconnectBtn)
                disconnect();
        }
    }
    
    private class HomeContainer extends JPanel
    {
        public HomeContainer()
        {
            setLayout(new CardLayout());
            setBackground(Color.WHITE);
        
            settingsPanel   =   new SettingsPanel();
            bookPanel       =   new BookPanel();
            checkoutPanel   =   new CheckoutPanel();
            summaryPanel    =   new SummaryPanel();
            returnsPanel    =   new ReturnsPanel();

            add(summaryPanel, SUMMARY_VIEW);
            add(checkoutPanel, CHECKOUT_VIEW);
            add(settingsPanel, SETTINGS_VIEW);
            add(bookPanel, BOOK_VIEW);
            add(returnsPanel, RETURNS_VIEW);
        }
        
        public void changeView(String cardName)
        {
            CardLayout cLayout  =   (CardLayout) getLayout();
            cLayout.show(this, cardName);
        }
    }
    
    public static HomePanel getInstance()
    {
        if(instance == null) instance = new HomePanel();
        return instance;
    }
}
