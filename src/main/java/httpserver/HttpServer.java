package httpserver;

import httpserver.core.ListenerThread;
import java.io.IOException;

public class HttpServer {
    public static void main(String[] args) {

        int port = 8080;

        ListenerThread serverListenerThread = null;  //Täpselt ei tea hetkel, miks nii, IDEA aitas. Vist ei ole autoclosable vms
        try {
            serverListenerThread = new ListenerThread(port, "");
            serverListenerThread.start();
            System.out.println("Sever is up at localhost, on port: " + port);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
