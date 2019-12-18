package httpserver;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Main class instantiates the server.
 * @author 170011408
 */
public class WebServerMain {
    /**
     * Number of command line arguments.
     * @param CORRECT_NUMBER_OF_ARGS
     */
    public static final int CORRECT_NUMBER_OF_ARGS = 2;
    /**
     * Ports available.
     * @param MAX_ELIGIBLE_PORT_NUMBER
     */
    public static final int MAX_ELIGIBLE_PORT_NUMBER = 65535;
    /**
     * Main. First command line argument is the resources path, second the port.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    int port;
        try {
            String workingDirectoryString = args[0];
            File workingDirectoryFile = new File(args[0]);
            /**
             * For file type handling and methods:
             * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/io/F
             * ile.html">https://docs.oracle.com/javase/7/docs/api/java/io/F
             * ile.html</a>
             * For eligible port numbers:
             * @see <a href="https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_
             * port_numbers">https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_
             * port_numbers</a>
             */
            if ((args.length != CORRECT_NUMBER_OF_ARGS)
                    || (Integer.parseInt(args[1]) > MAX_ELIGIBLE_PORT_NUMBER)
                    || (!workingDirectoryFile.isDirectory())) {
                    System.out.println("Usage: java WebServerMain "
                            + "<document_root> <port>");
            }
            else {
                port = Integer.parseInt(args[1]);
		/**
                 * Instantiate Server
                 */
                Server s = new Server(port, workingDirectoryString);
            }
        }
        catch (Exception e) {
            System.out.println("Usage: java WebServerMain <document_root> <port>");
            System.exit(0);
	}
    }
}