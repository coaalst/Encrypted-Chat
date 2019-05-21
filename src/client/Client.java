package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;


import auth.LoginState;
import sec.RSA;

/*
 * Glavna klient klasa
 */
public class Client {

	private static PrintWriter socket_out;

	//enkripcija za poruke
	private static RSA decryptor;
	private static RSA encrypt;
	
	private static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {

		try {
			
			//ovde dodati ip servera
			Socket client = new Socket("localhost", 8081);

			//saljemo kljuceve
			socket_out = new PrintWriter(client.getOutputStream());
			decryptor = new RSA();
			socket_out.println(decryptor.getE());
			socket_out.println(decryptor.getN());
			socket_out.flush();
			
			//u modul za dekripciju saljemo dekriptor i client socket
			ClientInputModul cim = new ClientInputModul(client, decryptor);
			BigInteger e = new BigInteger(cim.readInput());
			BigInteger n = new BigInteger(cim.readInput());
			
			//uzimamo kljuceve za enkripciju
			encrypt = new RSA(e, n);

			input = new Scanner(System.in);
			
			//stanje login-a
			LoginState authState;

			do {
				//unos podataka
				System.out.print("Nickname: ");
				String username = input.nextLine();
				System.out.print("Password: ");
				String password = input.nextLine();
				
				//saljemo ih serveru na verifikaciju
				socket_out.println(encrypt.encryptString(username));
				socket_out.println(encrypt.encryptString(password));
				socket_out.flush();
				
				//hvatamo odgovor
				authState = LoginState.valueOf(decryptor.decryptString(cim.readInput()));
				
				//print na ekran za trablshoot
				System.out.println(authState.name());
				
			} while (authState != LoginState.LOGIN_ACCPETED && authState != LoginState.REGISTERED);
			
			//porekecemo run metode u modulu
			new Thread(cim).start();
			
			while (true) {
				
				//input za ostale korisnike
				String messageToServer = input.nextLine();
				
				//ako nije prazna
				if (!messageToServer.equals("")) {
					socket_out.println(encrypt.encryptString(messageToServer));
					socket_out.flush();
				} 
				
				//komanda za promenu sifre
				else if (messageToServer.toLowerCase().startsWith(":changepassword")) {
					socket_out.println(encrypt.encryptString("changePassword: " + input.nextLine()));
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
