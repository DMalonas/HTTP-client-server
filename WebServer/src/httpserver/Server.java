package httpserver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    public static final int MAX_NUMBER_OF_THREADS = 2;
    private int connectionCount = 0;

    private int port;
    private String workingDirectory;
   
    private PrintWriter out;

    
	public Server(int port, String workingDirectoryString) {
        this.port = port;
        try {
            ServerSocket ss = new ServerSocket(this.port);
            out = new PrintWriter(new FileWriter("Log.txt", true));
            this.workingDirectory = workingDirectory;
            ExecutorService threadPool = Executors.newFixedThreadPool(MAX_NUMBER_OF_THREADS);
            while (true) {
                Socket s = ss.accept();
                //prepare runnable content
                ClientHandler myClientHandler = new ClientHandler(s, out, this.workingDirectory);
              //  threadPool.submit(myClientHandler);
            }	
    	} catch (IOException e) {
    		System.out.println("Trouble with connection" + e);
    	}
	}

}
