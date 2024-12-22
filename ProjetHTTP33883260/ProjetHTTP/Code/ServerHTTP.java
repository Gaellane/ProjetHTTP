package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ServerHTTP {
    private static int port;
    private static String rootPath;
    private static boolean phpreader;
    boolean isRunning=false;
    ServerSocket serverSocket;

    @SuppressWarnings("removal")
    public void setconfig(int p,String path,boolean php) throws Exception {
        String chemin="config/config.txt";
        File f=new File(chemin);
        if (!f.exists()) {   
            System.out.println(f.getAbsolutePath());
            throw new Exception("Le fichier de configuration est introuvable");
        }
        List <String> lines=Files.readAllLines(f.toPath());
        List <String> modified=new ArrayList<>();
        for(String line : lines) {
            String [] parts=line.split("=");
            if(line.startsWith("port") && Integer.valueOf(p)!=null) {           
                line=parts[0]+"="+p;
            } else if(line.startsWith("rootPath") && path!=null) {
                line=parts[0]+"="+path;
            } else if(line.startsWith("phpenable") && Boolean.valueOf(php)!=null) {
                line=parts[0]+"="+php;
            }
            modified.add(line); 
        }

        Files.write(Paths.get(f.getAbsolutePath()),modified);
    }
    
    void setConfig() throws Exception{
        String chemin="config/config.txt";
        File f=new File(chemin);
        if (!f.exists()) {   
            System.out.println(f.getAbsolutePath());
            throw new Exception("Le fichier de configuration est introuvable");
        }
        List <String> lines=Files.readAllLines(f.toPath());
        for(String line : lines) {
            if(line.startsWith("port")) {           
                if(line.split("=").length==0) {
                    port=1234;
                } else {
                    port=Integer.parseInt(line.split("=")[1]);
                }
            } else if(line.startsWith("rootPath")) {
                if(line.split("=").length==0) {
                    rootPath="htdocs";
                } else {
                    rootPath=line.split("=")[1];
                }
            } else if(line.startsWith("phpenable")) {
                if(line.split("=").length==0) {
                    phpreader=true;
                } else {
                    phpreader=Boolean.valueOf(line.split("=")[1]);
                }
            }
        }
    }

    public ServerHTTP() throws Exception{
        setConfig();
        serverSocket = new ServerSocket(port);
    }
    
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    
    public void startServer() throws Exception {
        isRunning=true;
        
        //thread separe pour eviter le blocage de l'interface du server tout en maintenant la connexion sur le navigateur
        new Thread(() -> {
            try {
            System.out.println("Serveur HTTP en attente de connexions sur le port " + port);

            while (isRunning) {
                try {
                    // Attente de la connexion d'un client
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connexion établie avec " + clientSocket.getInetAddress());

                    // Gestion du client dans un thread séparé
                    new Thread(new ClientHandler(clientSocket, rootPath,phpreader)).start();
                } catch (IOException e) {
                    if (!isRunning) {
                        // Si le serveur est en cours d'arrêt, ignorer les exceptions
                        System.out.println("Arrêt du serveur en cours...");
                    } else {
                        throw e;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Le serveur a été arrêté.");
        }
    }).start();
    }

    public void stopServer() throws Exception {
        isRunning=false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close(); // Ferme le serveur
        }
    }

}
