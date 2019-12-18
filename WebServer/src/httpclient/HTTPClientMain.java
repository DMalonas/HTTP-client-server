package httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;

public class HTTPClientMain {
	private Socket s;
	private OutputStream out;
	private PrintWriter pw;
	private InputStream is;
	private BufferedReader br;
	
	private String hostname;
	private int port;
	
	Scanner sc = new Scanner(System.in);
	private URL url;
	
	private String methodType;
	private String path;
	private String protocolName = "HTTP/1.1";
	
	String response = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HTTPClientMain clm = new HTTPClientMain();
		while(true) {
			clm.retrieveHostname();
			clm.setPort();
			clm.createSocketAndStreams();
			clm.makeRequest();
			clm.handleResponse();
			if (clm.terminate()) {
		    	System.out.println("The client will now exit");
				break;
			}
		}
	}

	private boolean terminate() {
		// TODO Auto-generated method stub
    	do {
    		System.out.print("\nMake another request? (Yes/No): ");
    		this.response = sc.next().toLowerCase();
    	} while ((!this.response.equals("yes")) && (!this.response.equals("no")));
		try {
			s.close();
			is.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (response.equals("no")) {
			return true;
		}
		return false;
	}

	private void handleResponse() {
		// TODO Auto-generated method stub
		try {
			while((this.response = this.br.readLine()) != null) {
				System.out.println(response);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void makeRequest() {
		// TODO Auto-generated method stub
		setRequestMethod();
		this.path = url.getPath();
		pw.println(methodType + " " +  path + " " + protocolName);
	}

	private void createSocketAndStreams() {
		// TODO Auto-generated method stub
		try {
			s = new Socket(this.hostname, this.port);
			out = s.getOutputStream();
			pw = new PrintWriter(out, true);
			is = s.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void setPort() {
		// TODO Auto-generated method stub
		int ans = 0;
		
		do {
			System.out.println("\nEnter port (8000 - 8999): ");
			ans = sc.nextInt();
		} while ((ans < 8000) || (ans > 8999));
		this.port = ans;
	}

	
	
	public void retrieveHostname() {
		// TODO Auto-generated method stub
		int ans = 0;
		String input;
		 while(true) {
			System.out.print("\n******    Valid URLs    ******\n"
					+ "1. http:localhost:8002/index.html\n"
					+ "2. http:localhost:8002/page2.html\n"
					+ "3. http:localhost:8002/page3.html\n"
					+ "******    Invalid URL    ******\n"
					+ "4. http:localhost:8002/page4.html\n");
			ans = sc.nextInt();
			if ((ans == 1) || (ans == 2) || (ans == 3) || (ans == 4)) {
				if (1 == ans) {
					input = "http:localhost:8002/index.html";
				} else if (2 == ans) {
					input = "http:localhost:8002/page2.html";
				} else if (3 == ans) {
					input = "http:localhost:8002/page3.html";
				} else {
					input = "http:localhost:8002/page4.html";
				}
				try {
					this.url = new URL("http:localhost:8002/index.html");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.hostname = this.url.getHost();
				System.out.print(hostname);
				break;
			}
		}
	}
	
	public void setRequestMethod() {
		do {
			System.out.print("Enter method type (GET / HEAD / PUT / POST / DELETE)");
			this.methodType = sc.next();
		} while ((!this.methodType.equals("GET")) && 
				(!this.methodType.equals("HEAD")) &&
				(!this.methodType.equals("PUT")) &&
				(!this.methodType.equals("POST")) &&
				(!this.methodType.equals("DELETE")));
	}

}
