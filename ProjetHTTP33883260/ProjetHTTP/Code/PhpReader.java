package server;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Vector;



public class PhpReader{

    public static String contentPhp(File f,String method,String parameters) throws IOException {
        System.out.println("Path of file="+f.getAbsolutePath());
        Vector <String> commands=new Vector<String>();
        String commande="";
        ProcessBuilder pb;
        Process p;
            if (method.equalsIgnoreCase("GET")) {
                commands.add("php");
                commands.add("-r");
                //commands.add("");
                commands.add("parse_str('"+parameters+"', $_GET); include('"+f.getAbsolutePath()+"');");
            } else if (method.equalsIgnoreCase("POST")) {
                commands.add("php");
                commands.add("-r");
                commands.add(" parse_str('"+parameters+"', $_POST); include('"+f.getAbsolutePath()+"');");

            }
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        System.out.println(processBuilder.command());
        processBuilder.redirectErrorStream(true);
        p=processBuilder.start();

        String retour="";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                retour+=(line+"\n");
            }
        }
        System.out.println("Retour="+retour);
        return retour;
    }   
}
