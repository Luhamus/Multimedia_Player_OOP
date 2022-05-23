package httpserver;

import httpserver.core.ListenerThread;
import java.io.IOException;

public class HttpServer {
    public static void main(String[] args) {

        ListenerThread serverListenerThread = null;  //Täpselt ei tea hetkel, miks nii, IDEA aitas. Vist ei ole autoclosable vms
        try {
            serverListenerThread = new ListenerThread(8080, "/src/main/webroot");  //Windowsil vist peab bäkslashima
            serverListenerThread.start();
            System.out.println("Sever is up");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
