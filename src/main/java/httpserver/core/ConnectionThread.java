package httpserver.core;

import http.*;
import httpserver.utils.ParseUrlQuery;
import httpserver.utils.ServeHtml;

import java.io.*;
import java.net.Socket;
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

            /* Kui vaja näha täpselt, mis input on uncommentida.
            int _byte;
            while ( (_byte = inputStream.read()) >= 0 ){
               System.out.print( (char)_byte);
            } */

            try {
                HttpRequest req = new HttpParser().parseHttpRequest(inputStream);

                body = req.getBody();
                method = req.getMethod();
                reqTarget = req.getRequestTarget();

                HttpVersion httpVersion = req.getBestCompatibleHttpVersion();
            } catch (HttpParsingException e) {
                System.out.println("Couldn't parse, Error code: " + e.getErrorCode());
            }

            //Vaatame, kas URL-iga anti kaasa ka muutjuaid
            HashMap<String, String> queryParams = ParseUrlQuery.getParameters(reqTarget);
            reqTarget = ParseUrlQuery.getUrlTarget(reqTarget); // kui parameetreid ei antut, siis queryParams == null


            /* Methods */


            if (method.name().equals("GET")) {
                ServeHtml.serveHtmlFile(endpoints, reqTarget, webroot, outputStream);
            }
            else if (method.name().equals("POST")) {
                String[] info = body.split("&");
                //Siin saaks siis bodyga asju teha

                ServeHtml.serveHtmlFile(endpoints, reqTarget, webroot, outputStream);
            }


        }  // Big Try block end
        catch(Exception e){
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
