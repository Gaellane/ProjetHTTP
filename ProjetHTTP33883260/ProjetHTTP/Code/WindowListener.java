package listener;

import javax.swing.*;

import server.Window;

import java.awt.event.*;

public class WindowListener implements ActionListener {
    Window w;

    public WindowListener(Window w) {
        this.w=w;
        w.getStart().addActionListener(this);
        w.getStop().addActionListener(this);
        w.getParameter().addActionListener(this);
        w.getSave().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource().equals(w.getStart())) {
            try {
                w.getServer().startServer();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else if(e.getSource().equals(w.getStop())) {
            try {
                w.getServer().stopServer();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else if(e.getSource().equals(w.getParameter())) {
            try {
                w.settings();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else if(e.getSource().equals(w.getSave())) {
            try {
                int port=w.getPortConfig();
                String path=w.getPathConfig();
                boolean php=w.getPhpConfig();
                System.out.println(port);
                w.getServer().setconfig(port, path, php);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        //throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
    
}
