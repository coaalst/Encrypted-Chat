package server;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import server.ClientConnection;

/*
 * Server konzola za administraciju
 */
public class Console implements Runnable{

	private final Scanner consoleInput = new Scanner(System.in);
	private final Map<String, ClientConnection> clients;
	
	public Console(Map<String, ClientConnection> clients) {
		this.clients = clients;
		System.out.println("Console pokrenuta");
	}
		
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			String input = consoleInput.nextLine();
			
			if(input != null) execute(input);
			
			else break;
		}
	}
	
	/*
	 * Metoda za mendzment komandi
	 */
	private void execute(String input) {
		// TODO Auto-generated method stub
		
		if (input.startsWith("kick ")) kick(input.substring("kick ".length()));
		
		 else if (input.equals("clients")) printListOfClients();
		
		 else if (input.startsWith("broadcast ")) broadcast(input.substring("broadcast ".length()));
		 else if (input.equals("help")) System.out.println("Server commands:" +
															"\n\tkick" +
															"\n\tbroadcast" +
															"\n\tclients");
		 else System.err.println("Pogresna komanda");
	}
	
	/*
	 * Funkcija za slanje svima notifikacije
	 */
	private void broadcast(String substring) {
		// TODO Auto-generated method stub
        for (ClientConnection c : clients.values()) c.sendEncrypeted("SERVER: " + substring);
        
	}
	
	/*
	 * Funkcija za prikaz svih povezanih korisnika
	 */
	private void printListOfClients() {
		// TODO Auto-generated method stub
		System.out.println("Konektovano: (" + clients.size() + ") korisnika");
		for (String username : clients.keySet()) System.out.println("\t" + username);
		
	}
	
	/*
	 * funkcija za kikovanje
	 */
	private void kick(String substring) {
		// TODO Auto-generated method stub
		try {
			clients.get(substring).sendEncrypeted("SERVER: kikovan si.");
			clients.get(substring).clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
