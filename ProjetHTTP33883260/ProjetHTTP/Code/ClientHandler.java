package server;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

class ClientHandler implements Runnable{
    Socket client;
    String rootPath;
    boolean phpenable;
    
    public ClientHandler(Socket s,String root,boolean p) {
        client=s;
        rootPath=root;
        phpenable=p;
    }

    String getContentType(File file) {
        String contentType = "text/html"; 
        if (file.getName().endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (file.getName().endsWith(".png") ) {
            contentType = "image/png";
        } else if (file.getName().endsWith(".txt")) {
            contentType = "text/plain";
        }else if (file.getName().endsWith(".php")){
            contentType = "text/html; charset=UTF-8";
        } else if(file.getName().endsWith(".css")) {
            contentType="text/css";   
        }
        return contentType;
    }

    String getParameters(String path) {
        String [] paths=path.split(" ")[1].split("\\?");
        if(paths.length<2) {
            return "";
        } else {
            return paths[1];
        }
    }

    String getMethod(String path) {
        return path.split(" ")[0];
    }

    File getFile(String p) {

        String path = rootPath + p.split(" ")[1].split("\\?")[0]; 
        File file = new File(path);
        
        // Vérifiez si le chemin est la racine
        if (path.equals(rootPath + "/")) {
            // Retournez le fichier welcome.html
            return new File(rootPath, "welcome.html");
        }
        
        // Vérifiez si le chemin est un dossier
        if (file.isDirectory()) {
            // Essayez de charger index.php ou index.html
            File indexPhp = new File(file, "index.php");
            if (indexPhp.exists()) {
                return indexPhp;
            }
            File indexHtml = new File(file, "index.html");
            if (indexHtml.exists()) {
                return indexHtml;
            }
            
            // Si aucun index n'existe, lister le contenu du dossier
            return file; // Retourne le dossier pour lister son contenu
        }
        
        return file; // Retourne le fichier demandé ou le dossier
    }

    void doRequestText(String path,InputStream s,OutputStream out,int statusCode,String msg,String postbody) throws Exception {
        File f=getFile(path);
        String retour=PhpReader.contentPhp(f,getMethod(path),postbody);
        byte[] fileContent = retour.getBytes("UTF-8");

        StringBuilder headers = new StringBuilder();
        headers.append("HTTP/1.1 ").append(statusCode).append(" ").append(msg).append("\r\n");
        headers.append("Content-Type: ").append(getContentType(f)).append("\r\n");
        headers.append("Content-Length: ").append(fileContent.length).append("\r\n");
        headers.append("\r\n");

        out.write(headers.toString().getBytes("UTF-8"));
        out.write(fileContent);
        out.flush();
    }

    void doRequest(String path, InputStream s, OutputStream out, int statusCode, String msg, String postbody) throws Exception {
        File f = getFile(path);
        String errorMsg;
        if (!f.exists() || !f.isFile()) {
            statusCode = 404;
            msg = "Not Found";
            errorMsg = "404-File not found: " + f.getName();
            
            // Si c'est un dossier, lister son contenu
            if (f.isDirectory()) {
                StringBuilder fileList = new StringBuilder("<h1>Liste des fichiers</h1><ul>");
                for (File file : f.listFiles()) {
                    // Créer un lien pour chaque fichier
                    String relativePath=path.split(" ")[1].split("\\?")[0];
                    String fileName = file.getName();
                    String filePath = path.endsWith("/") ? relativePath + fileName : relativePath + "/" + fileName;
                    
                    // S'assurer que le chemin est correct
                    fileList.append("<li><a href='").append(filePath).append("'>").append(fileName).append("</a></li>");
                }
                fileList.append("</ul>");
                errorMsg = fileList.toString();
                statusCode = 200; // On retourne un code 200 pour afficher le contenu
                msg = "OK";
            }
    
            out.write(("HTTP/1.1 " + statusCode + " " + msg + "\r\n").getBytes());
            out.write(("Content-Type: text/html\r\n").getBytes());
            out.write(("Content-Length: " + errorMsg.length() + "\r\n").getBytes());
            out.write("\r\n".getBytes());
            out.write(errorMsg.getBytes());
            out.flush();
        } else if(f.getName().endsWith("php") && !phpenable) {
            statusCode = 403;
            msg = "Forbidden";
            errorMsg = statusCode+" "+msg+" "+" only .html files are allowed";
            out.write(("HTTP/1.1 " + statusCode + " " + msg + "\r\n").getBytes());
            out.write(("Content-Type: text/html\r\n").getBytes());
            out.write(("Content-Length: " + errorMsg.length() + "\r\n").getBytes());
            out.write("\r\n".getBytes());
            out.write(errorMsg.getBytes());
            out.flush();
        }
         else if (getContentType(f).contains("text/html")) {
            doRequestText(path, s, out, statusCode, msg, postbody);
        } else {
            byte[] file = Files.readAllBytes(f.toPath());
            int length = file.length;
            StringBuilder headers = new StringBuilder();
            headers.append("HTTP/1.1 ").append(statusCode).append(" ").append(msg).append("\r\n");
            headers.append("Content-Type: ").append(getContentType(f)).append("\r\n");
            headers.append("Content-Length: ").append(length).append("\r\n");
            headers.append("\r\n");
    
            out.write(headers.toString().getBytes("UTF-8"));
            out.write(file);
            out.flush();
        }
    }


    @Override
    public void run() {
        try {
            InputStream in=client.getInputStream();
            OutputStream out=client.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while (true) {
                String request=reader.readLine();
                System.out.println(request);
                File f=getFile(request);
                int contentLength = 0;
                while (!(line = reader.readLine()).isEmpty()) {
                    System.out.println(line);
                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.split(":")[1].trim());
                    }
                }

                // Lire le corps de la requête (si méthode POST)
                String postBody = "";
                if ("POST".equalsIgnoreCase(getMethod(request)) && contentLength > 0) {
                    char[] body = new char[contentLength];
                    reader.read(body, 0, contentLength);
                    postBody = new String(body);
                }
                doRequest(request, in, out, 200, "OKKK",postBody);
                
            }

        }  catch (Exception e) {

        }
    }
}
