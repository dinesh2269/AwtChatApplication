package awt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class HandleClientThread implements Runnable {
	Socket ss;
	List<Socket> clientsList;
	Map<String, String> map;
	String name;
	// <<<<<<< HEAD
	static List<String> connectedUsers;
	// =======
	// static List<String> connectedUsers;
	// >>>>>>> branch 'master' of https://github.com/dinesh2269/AwtChatApplication.git

	HandleClientThread(String name, Socket ss, List<Socket> clientsList) {
		this.name = name;
		this.ss = ss;
		this.clientsList = clientsList;
		this.clientsList.add(ss);
		map = new HashMap<>();
	}

	@Override
	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
				PrintWriter out = new PrintWriter(ss.getOutputStream(), true);
				Scanner sc = new Scanner(System.in)) {
			while (true) {
				String str = br.readLine();
				if (str.length() != 0) {
					for (Socket s : clientsList) {
						PrintWriter o = new PrintWriter(s.getOutputStream(), true);
						o.println(this.name + " : " + str);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class ServerClass {
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(60000);
		System.out.println("waiting");
		List<Socket> clientsList = new ArrayList<>();
		// <<<<<<< HEAD
		// HandleClientThread.connectedUsers = new ArrayList<>();
		// =======
		HandleClientThread.connectedUsers = new ArrayList<>();
		// >>>>>>> branch 'master' of https://github.com/dinesh2269/AwtChatApplication.git
		while (true) {
			Socket s = ss.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String name = br.readLine();
			System.out.println("listening " + s);
			// <<<<<<< HEAD
			//
			// HandleClientThread.connectedUsers.add(name);
			// PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			//
			// for (String data : HandleClientThread.connectedUsers) {
			// out.println(data);
			// }
			// =======

			HandleClientThread.connectedUsers.add(name);
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			String temp = "";
			for (String data : HandleClientThread.connectedUsers) {
				temp += (data + " ");
			}
			out.println(temp);

			// >>>>>>> branch 'master' of https://github.com/dinesh2269/AwtChatApplication.git
			new Thread(new HandleClientThread(name, s, clientsList)).start();
		}
	}

}
