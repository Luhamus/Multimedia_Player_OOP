package httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServer {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8080)){
            System.out.println("accept stuff");
            Socket socket = serverSocket.accept();  // Client socket, data servimiseks kasutame seda

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            System.out.println("Got the faking input-output down");

            // WRITE HERE
            String html = "<html><head><title>LetsGou</title></head> <body><h1>PlaceHolder thingy</h1></body></html>";


            String CRLF = "\n\r";  //Newline + enter, vaja http protocollis
            String response =   //http protocol stuff
                    "HTTP/1.1 200 OK" + CRLF +   //status bar : HTTP_VERSION RESPONSE_CODE MESSAGE
                    "Content-length: "+ html.getBytes().length + CRLF +  // Headers
                    CRLF+  //Done with protocol info
                    html+
                    CRLF + CRLF;

            outputStream.write(response.getBytes());

            inputStream.close();
            outputStream.close();
            socket.close();
        }
        catch (IOException e){
            System.out.println("Trunk is all frakd up!");
            System.out.println(e);
        }
    }
}
