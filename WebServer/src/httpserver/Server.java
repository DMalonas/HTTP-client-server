package httpserver;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Server class.
 * @author 170011408
 */
public class Server {
    private int port;
    private String workingDirectory;
    /**
     * For testing, in a real server it could be much more.
     */
    public static final int MAX_NUMBER_OF_THREADS = 2;
    /**
     * For logging.
     */
    private PrintWriter out;
    /**
     * Connection number counter.
     */
    private int connectionCount = 0;
    /**
     * Constructor with two arguments, int port which is
     * the port where the ServerSocket will be bounded
     * and, int workingDirectory that is the directory
     * with the resources.
     * @param port type: int, the port the Server is listening
     * @param workingDirectory type: String the resources directory (www/)
     */
    public Server(int port, String workingDirectory) {
        this.port = port;
        try {
		/**
                 * Create ServerSocket object ss in the listening port
                 */
                ServerSocket ss = new ServerSocket(this.port);
                this.workingDirectory = workingDirectory;
		/**
                 * Instantiate PrintWriter object
                 */
                out = new PrintWriter(new FileWriter("Log.txt", true));
		/**
                 * Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded queue.
                 * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/
                 * util/concurrent/ExecutorService.html">https://docs.oracle.com
                 * /javase/7/docs/api/java/util/concurrent/ExecutorService.htm
                 * l</a>
                 */
                ExecutorService threadPool = Executors.
                        newFixedThreadPool(MAX_NUMBER_OF_THREADS);
                while (true) {
		    /**
                     * Accept connection request from client
                     */
                    Socket s = ss.accept();
		    /**
                     * Prepare runnable content
                     */
                    ClientHandler myClientHandler = new ClientHandler(s, out, this.workingDirectory);
		    /** 
                     * Execute as a separate thread
                     */
                    threadPool.submit(myClientHandler);
                }
        }
        catch (IOException e) {
                System.out.println("Trouble with connection" + e);
        }
    }
}