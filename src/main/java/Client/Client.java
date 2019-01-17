package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

	
	
	private void updateRequest(String aUserId)
	{
		try {
			Socket socket = new Socket("127.0.0.1",6666);
			PrintWriter socketWriter = new PrintWriter(socket.getOutputStream());
			socketWriter.write("1");
			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			if(socketReader.readLine().equals("1 updated")) System.out.println("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			Thread.sleep(10000);
			updateRequest("1");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
