//==================================
//  Kyle Russell
//  AUT University 2016
//  Highly Secure Systems
//==================================

import javax.swing.JFrame;

public class SafeLibraryHost
{
    private MainPanel panel;
    private JFrame frame;
    
    private SafeLibraryHost()
    {
        panel   =   MainPanel.getInstance();
        frame   =   new JFrame(Config.APP_NAME);
        
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    
    public void show()
    {
        frame.setVisible(true);
    }
    
    public MainPanel getPanel()
    {
        return panel;
    }
    
    public static void main(String[] args)
    {
        SafeLibraryHost window  =    new SafeLibraryHost();
        window.show();
		
        try
        {
            MainPanel.getInstance().getConnectPanel().handleInitialConnection();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
