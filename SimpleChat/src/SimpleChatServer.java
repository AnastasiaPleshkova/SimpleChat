import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleChatServer {
    List<PrintWriter> clientOutputStreams;

    public static void main(String[] args) {
        new SimpleChatServer().go();
    }

    public void go(){
        clientOutputStreams = new ArrayList<>();

        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true){
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);
                Thread t = new Thread(() -> {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String message;
                        while ((message = reader.readLine())!=null){
                            System.out.println("read" + message);
                            tellEveryone(message);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                t.start();
                System.out.println("Got a connection");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void tellEveryone (String message) {
        Iterator it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
