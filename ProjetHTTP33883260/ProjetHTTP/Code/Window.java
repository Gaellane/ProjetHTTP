package server;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Window extends JFrame{
    JPanel panel;
    JButton start;
    JButton stop;
    ServerHTTP server;
    JButton parameter;
    JButton saveParameter=new JButton("Enregistrer");
    JTextField port;
    JTextField path;
    JCheckBox phpenable;

    public Window() throws Exception{
        server=new ServerHTTP();
        setLayout(null);
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel=new JPanel();
        panel.setLayout(null);
        panel.setSize(400, 300);
        panel.setBounds(0, 0, 450, 300);
        
        start=new JButton("Start Server");
        start.setLayout(null);
        start.setBounds(50, 100, 150, 20);
        panel.add(start);
        
        stop=new JButton("Stop Server");
        stop.setLayout(null);
        stop.setBounds(300, 100, 150, 20);
        panel.add(stop);

        parameter=new JButton("Parametre");
        parameter.setLayout(null);
        parameter.setBounds(200, 200, 150, 20);
        panel.add(parameter);
        
        add(panel);
        setVisible(true);
    }

    public JButton getStart() {
        return start;
    }

    public JButton getStop() {
        return stop;
    }

    public JButton getParameter() {
        return parameter;
    }

    public JButton getSave() {
        return saveParameter;
    }

    public int getPortConfig() {
        return Integer.parseInt(port.getText());
    }

    public String getPathConfig() {
        return path.getText();
    }

    public boolean getPhpConfig(){
        return phpenable.isSelected();
    }

    public ServerHTTP getServer() {
        return server;
    }

    public void settings() {
        JFrame frame=new JFrame();
        frame.setLayout(null);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p=new JPanel();
        p=new JPanel();
        p.setLayout(null);
        p.setSize(400, 300);
        p.setBounds(0, 0, 450, 300);

        JLabel l1=new JLabel("Port: ");
        l1.setBounds(20,20,50,30);
        port=new JTextField();
        port.setLayout(null);
        port.setBounds(100, 20, 50, 30);

        JLabel l2=new JLabel("RootPath: ");
        l2.setBounds(20,50,150,30);
        path=new JTextField();
        path.setLayout(null);
        path.setBounds(100, 50, 50, 30);

        JLabel l3=new JLabel("Php enable: ");
        l3.setBounds(20,80,150,30);
        phpenable=new JCheckBox();
        phpenable.setLayout(null);
        phpenable.setBounds(100,80,20,20);

        saveParameter.setLayout(null);
        saveParameter.setBounds(200, 200, 150, 20);
        p.add(l1);
        p.add(l2);
        p.add(l3);
        p.add(phpenable);
        p.add(saveParameter);
        p.add(port);
        p.add(path);
        frame.add(p);
        frame.setVisible(true);
    }
}
