package main;

import listener.WindowListener;
import server.ServerHTTP;
import server.Window;


public class Main {
    public static void main(String[] args) {
        Window w;
        try {
            w = new Window();
            WindowListener wl=new WindowListener(w);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}