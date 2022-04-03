package httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionThread extends Thread{

    private Socket socket;

    public ConnectionThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream()) {

            //int _byte;
            //while ( (_byte = inputStream.read()) >= 0 ){
            //    System.out.print( (char)_byte);
            //}

            // WRITE HERE
            String html = "<html><head><title>LetsGou</title></head> <body><h1>PlaceHolder thingy</h1></body></html>";

            String CRLF = "\n\r";  //  Newline + enter, vaja http protocollis
            String response =   //http protocol stuff
                    "HTTP/1.1 200 OK" + CRLF +   //status bar : HTTP_VERSION RESPONSE_CODE MESSAGE
                            "Content-length: " + html.getBytes().length + CRLF +  // Headers
                            CRLF +  //Done with protocol info
                            html +
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
