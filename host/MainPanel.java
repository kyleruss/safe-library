//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================


import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class MainPanel extends JPanel
{
    public static final String HOME_VIEW    =   "home_v";
    public static final String CONNECT_VIEW =   "connect_v";
    
    private static MainPanel instance;
    private HomePanel homePanel;
    private ConnectPanel connectPanel;
    
    private MainPanel()
    {
        setLayout(new CardLayout());
        setPreferredSize(new Dimension(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT));
        setBackground(Color.WHITE);
        
        homePanel       =   HomePanel.getInstance();
        connectPanel    =   new ConnectPanel();

		add(connectPanel, CONNECT_VIEW);
		add(homePanel, HOME_VIEW);
       
    }

    public HomePanel getHomePanel() 
    {
        return homePanel;
    }

    public ConnectPanel getConnectPanel() 
    {
        return connectPanel;
    }
    
    public void changeView(String cardName)
    {
        CardLayout cLayout  =   (CardLayout) getLayout();
        cLayout.show(this, cardName);
    }
    
    public static MainPanel getInstance()
    {
        if(instance == null) instance = new MainPanel();
        return instance;
    }
}
