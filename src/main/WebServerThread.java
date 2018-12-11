package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import support.TempSensor;

public class WebServerThread extends Thread{
	
	private Socket sc;
	
	private static InputStream in;
	private static OutputStream output;
	private static Scanner scan;
	private static PrintWriter out;
	
	private static boolean terminate=false;
	
	public WebServerThread(Socket socket) {
		this.sc=socket;
	}
	
	public void run(){
		try{
			
			//IO for the server
			in = sc.getInputStream();
			output = sc.getOutputStream();
			
			//Output
			scan = new Scanner(in,"UTF-8");
			out = new PrintWriter(new OutputStreamWriter(output,"UTF-8"),true);
			
			//Console log
			out.println("Server started on port 8081.");
			
			while(scan.hasNext() ) {
				if(terminate)sc.close();
				String line = scan.nextLine();
				out.println("GreenPI: <--"+line);
				eval(line);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static void eval(String l) {
		switch(l) {
		case ".help": 
			printHelp();
			break;
		case "sta mi radis?": 
			out.println("GreenPI: Sviram gusle, brat moi");
			break;
		
		case ".temp":
			printTemp();
			break;
		
		case ".exit":
			setTerminate(true);
			break;
			
		default: 
			out.println("GreenPI: Komanda nije validna");
			break;
		}
		
	}

	private static void printTemp() {
		TempSensor ts=new TempSensor();
		out.println("GreenPI: Temperatura iznosi: "+ts.getTemp());
	}

	private static void printHelp() {
		out.println("\n=======Lista Komandi=======\n"
				+ "1. .help - otvara ovaj dijalog\n"
				+ "2. .temp - vraca temperaturu\n"
				+ "3. .exit - gasi server\n"
				+ "===============================");
	}
	
	public static void setTerminate(boolean terminate) {
		WebServerThread.terminate = terminate;
	}
}
