package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import sec.RSA;

/*
 * Thread za klient koji sluzi za interakciju sa serverom
 */
public class ClientInputModul implements Runnable{

	private BufferedReader reader;
	private RSA decryptor;

	/*
	 * konstruktor
	 * @param input - soket za povezivanje reader-a
	 * @param decryptor - Rsa instanca za dekodiranje poruka
	 */
	public ClientInputModul(Socket input, RSA decryptor) throws IOException {
		this.decryptor = decryptor;
		this.reader = new BufferedReader(new InputStreamReader(input.getInputStream()));
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				String line = reader.readLine();
				if (line != null) System.out.println(decryptor.decryptString(line));
				 else {
					System.err.println("SERVER DISCONNECTED");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Funckija za komunikaciju preko soketa koja ne zahteva ukljucen thread
	 */
	public String readInput() throws IOException {
		return reader.readLine();
	}
}
