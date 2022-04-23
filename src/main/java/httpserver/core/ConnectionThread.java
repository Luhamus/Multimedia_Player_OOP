package httpserver.core;

import http.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConnectionThread extends Thread{

    private Socket socket;
    private String reqTarget;
    private ArrayList<String> endpoints;

    public ConnectionThread(Socket socket) {
        this.socket = socket;
        this.endpoints=new ArrayList<>();

        //Lisa endpointse siin
        this.endpoints.add("/index");
        this.endpoints.add("/contact");
    }

    @Override
    public void run() {
        try(InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream()) {

            // Kui vaja näha täpselt, mis input on uncommentida.

            //int _byte;
            //while ( (_byte = inputStream.read()) >= 0 ){
            //   System.out.print( (char)_byte);
            //}


            try {
                HttpRequest req = new HttpParser().parseHttpRequest(inputStream);

                HttpMethod method = req.getMethod();
                reqTarget  = req.getRequestTarget();
                HttpVersion httpVersion = req.getBestCompatibleHttpVersion();
            }
            catch (HttpParsingException e) {
                System.out.println("Couldn't parse");
                System.out.println("Error code: "+e.getErrorCode());
            }

            Path currentRelativePath = Paths.get("");
            String pwdPath = currentRelativePath.toAbsolutePath().toString();
            String endpoint = reqTarget;
            System.out.println("reqTarget: "+reqTarget);
            if (reqTarget.equals("/"))
                endpoint="/index";
            else if (!endpoints.contains(endpoint))
                endpoint="/errorPage";

            System.out.println(endpoint);
            pwdPath+="/src/main/html"+endpoint+".html";


            // Response Back --- From Here
            //String content = "<html><head><title>LetsGou</title></head> <body><h1>PlaceHolder thingy</h1></body></html>";

            StringBuilder contentBuilder = new StringBuilder();
            try {
                BufferedReader in = new BufferedReader(new FileReader(pwdPath));
                String str;
                while ((str = in.readLine()) != null) {
                    contentBuilder.append(str);
                }
                in.close();
            } catch (IOException e) {
            }
            String content = contentBuilder.toString();


                    String CRLF = "\n\r";  //  Newline + enter, vaja http protocollis
            String response =   //http protocol stuff
                    "HTTP/1.1 200 OK" + CRLF +   //status bar : HTTP_VERSION RESPONSE_CODE MESSAGE
                            "Content-length: " + content.getBytes().length + CRLF +  // Headers
                            CRLF +  //Done with protocol info
                            //html +
                            content +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());

        }
        catch (IOException e) {
            System.out.println(e);
        }
        finally{
            try {
                socket.close();
            } catch (IOException e) {}  // mby no good, but work
        }


    }  //Run
}  //Class
