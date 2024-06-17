import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Server {
    private final ExecutorService threadPool;

    public Server(int poolSize) {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void handleClient(Socket clientSocket) {
        try {
            PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true);
            toSocket.println("Hello from server " + clientSocket.getInetAddress());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8010;
        int poolSize = 100;
        Server server = new Server(poolSize);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(70000);
            System.out.println("Server is listening on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                server.threadPool.execute(() -> server.handleClient(clientSocket));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            server.threadPool.shutdown();
        }
    }
}