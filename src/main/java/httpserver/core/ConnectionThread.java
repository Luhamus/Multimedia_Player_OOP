package httpserver.core;

import http.*;
import httpserver.utils.ParseUrlQuery;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionThread extends Thread{

    final private Socket socket;
    final private String webroot;

    private HttpMethod method;
    private String reqTarget;
    private String body;

    private ArrayList<String> endpoints;

    public ConnectionThread(Socket socket, String webroot) {
        this.socket = socket;
        this.webroot = webroot;

        this.endpoints=new ArrayList<>();
        //Lisa endpointse siin
        this.endpoints.add("/index");
        this.endpoints.add("/contact");
        this.endpoints.add("/login");
    }

    @Override
    public void run() {
        try(InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream()) {

            // Kui vaja näha täpselt, mis input on uncommentida.
/*
            int _byte;
            while ( (_byte = inputStream.read()) >= 0 ){
               System.out.print( (char)_byte);
            }
*/

            try {
                HttpRequest req = new HttpParser().parseHttpRequest(inputStream);

                body = req.getBody();
                method = req.getMethod();
                reqTarget = req.getRequestTarget();

                HttpVersion httpVersion = req.getBestCompatibleHttpVersion();
            } catch (HttpParsingException e) {
                System.out.println("Couldn't parse");
                System.out.println("Error code: " + e.getErrorCode());
            }

            //Vaatame, kas URL-iga anti kaasa ka muutjuaid
            HashMap<String, String> queryParams = ParseUrlQuery.getParameters(reqTarget);
            reqTarget = ParseUrlQuery.getUrlTarget(reqTarget);
            System.out.println(queryParams);  // kui parameetreid ei antut, siis queryParams == null


            if (method.name().equals("GET")) {

                Path currentRelativePath = Paths.get("");
                String pwdPath = currentRelativePath.toAbsolutePath().toString();
                String endpoint = reqTarget;
                if (reqTarget.equals("/"))
                    endpoint = "/index";
                else if (!endpoints.contains(endpoint))
                    endpoint = "/errorPage";

                pwdPath += webroot + endpoint + ".html";


                // Response Back --- GET
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

            }  // If Methoid Equals GET end

            else if (method.name().equals("POST")) {

                //Body handle
                String[] info = body.split("&");


                Path currentRelativePath = Paths.get("");
                String pwdPath = currentRelativePath.toAbsolutePath().toString();
                pwdPath += webroot + "index.html";

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


        } // Try block end
        catch(IOException e){
            System.out.println(e);
        }
        finally{
            try {
                socket.close();
            } catch (IOException e) {
            }  // mby no good, but work
        }



    }  //Run
}  //Class
