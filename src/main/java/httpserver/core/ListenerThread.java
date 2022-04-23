package httpserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenerThread extends Thread{

    private int port;
    private String webroot;  // See kus html vms paikneb
    private ServerSocket serverSocket;

    public ListenerThread(int port, String webroot) throws IOException {  //et handeliks serverSocketi
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {

        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                ConnectionThread ConnectionThread = new ConnectionThread(socket, webroot);
                ConnectionThread.start();
                System.out.println("New Connection");

            }  // While

        }  // Try
        catch (Exception e){
            System.out.println(e);
        }
        finally{
            try {
                serverSocket.close();
            } catch (IOException e) {}  // Võibolla natuke paha siin, aga praegu töötab
        }

    }  // Run
}   //Class