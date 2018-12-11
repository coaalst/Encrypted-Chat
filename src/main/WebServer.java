package main;

import java.io.*;
import java.net.*;

public class WebServer{
	
	private static int PORT=8081;
	private static ServerSocket server;
	
	public static void main(String[] args) throws IOException{
		server = new ServerSocket(PORT);
		Socket socket=null;
		while(true) {
			socket=server.accept();
			WebServerThread wst=new WebServerThread(socket);
			wst.start();
		}
	}
}
