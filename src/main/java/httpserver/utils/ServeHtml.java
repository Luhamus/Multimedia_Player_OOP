package httpserver.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ServeHtml {

    public static void servePix(String pwdPath, String reqTarget, OutputStream outputStream) throws Exception{
        pwdPath += "/src/main/webroot/pictures" + reqTarget;

        byte[] imageToBytes = Files.readAllBytes(Paths.get(pwdPath));

        String response =   //http protocol stuff
                "HTTP/1.0 200 Document Follows \r\n" +
                        "Content - Type: image/png > \r\n" +
                        "Content - Length: " + imageToBytes.length + "\r\n" +
                        "\r\n";

        outputStream.write(response.getBytes());
        outputStream.write(imageToBytes);
    }


    public static void serveHtmlText(String pwdPath, String webroot, String endpoint, OutputStream outputStream) throws Exception{

        pwdPath += webroot + endpoint + ".html";

        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(pwdPath));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        }
        catch (IOException e) {}

        String content = contentBuilder.toString();

        String CRLF = "\n\r";  //  Newline + enter, vaja http protocollis
        String response =   //http protocol stuff
                "HTTP/1.1 200 OK" + CRLF +   //status bar : HTTP_VERSION RESPONSE_CODE MESSAGE
                        "Content-length: " + content.getBytes().length + CRLF +  // Headers
                        CRLF +  //Done with protocol info
                        content +
                        CRLF + CRLF;

        outputStream.write(response.getBytes());
    }


    public static void serveHtmlFile(ArrayList endpoints, String reqTarget, String webroot, OutputStream outputStream) throws Exception{

        Path currentRelativePath = Paths.get("");
        String pwdPath = currentRelativePath.toAbsolutePath().toString();
        String endpoint = reqTarget;

        // Check endpoints
        if (reqTarget.equals("/"))
            endpoint = "/index";
        else if (!endpoints.contains(endpoint))
            endpoint = "/errorPage";

        //Pilt või tekst
        if (reqTarget.contains(".jpg"))
            servePix(pwdPath, reqTarget, outputStream);
        else
            serveHtmlText(pwdPath, webroot, endpoint, outputStream);

    }  // ServerHtml funk
} // Class
