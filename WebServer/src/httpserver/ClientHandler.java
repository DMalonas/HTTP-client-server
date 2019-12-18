package httpserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable{

	private Socket s;
	private PrintWriter out;
	private String workingDirectory;
	private InputStream is;
	private OutputStream os;
	private BufferedOutputStream outputBody;
	private PrintWriter pw;
	private BufferedReader br;

	
    public static final int NOT_IMPLEMENTED = 0;
    /**
     * Header part of the server's reply.
     * @param OK
     */
    public static final int OK = 1;
    /**
     * Header part of the server's reply.
     * @param NOT_FOUND
     */
    public static final int NOT_FOUND = 2;
    /**
     * Tokens.
     * @param TOKENS
     */
    public static final int TOKENS = 3;
    
    
	public ClientHandler(Socket s, PrintWriter out, String workingDirectory) {
		// TODO Auto-generated constructor stub
	    this.s = s;

        this.out = out;

        this.workingDirectory = workingDirectory;
	}

	@Override
	public void run() {
		try {
			is = s.getInputStream();
	        os = s.getOutputStream();
	        outputBody = new BufferedOutputStream(os);
	        /**
	         * PrintWriter (for logging) on the Server Socket's OutputStream os
	         */
	        pw = new PrintWriter(os, true);
	        br = new BufferedReader(new InputStreamReader(is));
	        /**
	         * Create an empty String for storing the html pages data
	         */
	        String line = "";
	        /**
	         * Browser reads from Server
	         */
	        while (true) {
	            int d = 0;
				d = br.read();
				 
	            System.out.print((char)d);
	            /**
	             * Break when find \n or \n modifiers
	             */
	            if (d == '\r' || d == '\n') {
	                break;
	            }
	            line += (char) d;
	        }
	        
	        String date = new Date().toString();
	        out.print("Time: " + date + ". Client request: " + line);
	        out.flush();

	        StringTokenizer st = new StringTokenizer(line);
            if (st.countTokens() < TOKENS) {
                replyToClientHeader(NOT_IMPLEMENTED, new File(workingDirectory
                        + "/errorNI.html"));
            }
            String methodType = st.nextToken();
            String fileName = st.nextToken();
            String version = st.nextToken();
            File file;
            if (methodType.equals("GET") || methodType.equals("HEAD")) {
                if (fileName.equals("/")) {
                    fileName += "index.html";
                }
   
                fileName = this.workingDirectory + fileName;
                file = new File(fileName);
                if (file.exists() && !file.isDirectory()) {
                    replyToClientHeader(OK, file);
                    if (methodType.equals("GET")) {
                        replyToClientBody(file);
                    }
                }
                else {
                    file = new File(workingDirectory + "/errorNF.html");
                    replyToClientHeader(NOT_FOUND, file);
                    if (methodType.equals("GET")) {
                            replyToClientBody(file);
                    }
                }
            }
            else {
                file = new File(workingDirectory + "/errorNI.html");
                replyToClientHeader(NOT_IMPLEMENTED, file); // 0: 501 Not implemented
                replyToClientBody(file);
            }
            br.close();
            is.close();
            os.close();
            s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
			}
			if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
			}
			if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
			}
			if (s != null) {
                try {
                    s.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
			}
		}
	}
	
	
    /**
     * This method sends the header part of the server's reply.
     * according to the http protocol's requirements.0: Not Implemented,
     * 1: OK, 2: Not Found
     * @param type int type returned (not-implemented, ok, or not-found)
     * @param file File, the html file
     */
    public void replyToClientHeader(int type, File file) {
    	
        if (type == NOT_IMPLEMENTED) {
            pw.print("HTTP/1.1 501 Not Implemented\r\n");
            out.print(". Response to client: HTTP/1.1 501 Not Implemented\r\n");
            out.flush();
        }
        else if (type == OK) {
            pw.print("HTTP/1.1 200 OK\r\n");
            out.print(". Response to client: HTTP/1.1 200 OK\r\n");
            out.flush();
        }
        else {
            pw.print("HTTP/1.1 404 Not Found\r\n");
            out.print(". Response to client: HTTP/1.1 404 Not Found\r\n");
            out.flush();
        }
        pw.print("Server: Simple Java Http Server\r\n");
        try {
            pw.print("Content-Type: " + Files.probeContentType(file.toPath()) + "\r\n");
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw.print("Content-Length: " + countChars(file) + "\r\n\r\n");
        pw.flush();
    }
    
    
    /**
     * This method sends the content of the requested entity,
     * including html pages and binary files.The header
     * part is assumed to have already been sent.
     * @param file the html file
     */
    public void replyToClientBody(File file) {
        FileInputStream fis = null;
        try {
            /**
             * @see <a href="https://stackoverflow.com/questions/14169661/read-c
             * omplete-file-without-using-loop-in-java">https://stackoverflow.co
             * m/questions/14169661/read-complete-file-without-using-loop-in-j
             * ava</a>
             */
            fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            outputBody.write(data);
            outputBody.flush();
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * This method returns the file length.
     * @param file resource file
     * @return long
     */
    public long countChars(File file) {
        /**
         * file type handling and methods
         * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/io/File.
         * html">https://docs.oracle.com/javase/7/docs/api/java/io/File.html</a>
         */
        return file.length();
    }
    
    
    
}
