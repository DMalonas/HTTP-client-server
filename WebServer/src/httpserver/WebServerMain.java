package httpserver;

import java.io.File;

public class WebServerMain {
	//args[0] directory, args[1] port number
	public static final int CORRECT_NUM_OF_ARGS = 2;
	// https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers
	public static final int MAX_ELLIGIBLE_PORT_NUM = 65535;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port;
        String workingDirectoryString = args[0];
        File workingDirectoryFile = new File(args[0]);
        
		try {
			if ((args.length != CORRECT_NUM_OF_ARGS) ||
				(Integer.parseInt(args[1]) > MAX_ELLIGIBLE_PORT_NUM) ||
				(!workingDirectoryFile.isDirectory())) {
                System.out.println("Usage: java WebServerMain "
                        + "<document_root> <port>");
			} else {
				port = Integer.parseInt(args[1]);
				Server s = new Server(port, workingDirectoryString);
			}
		} catch (Exception e) {
            System.out.println("Usage: java WebServerMain <document_root> <port>");
            System.exit(0);
		}

	}
}
