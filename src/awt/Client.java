package awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// import TwoWaySocketExample.HandleServerMessages;

public class Client extends JFrame implements Runnable {
	JFrame frame;
	JLabel messageLabel;
	JTextField message;
	JButton sendbtn;
	JLabel allMsgsArea;
	JTextArea displayArea;
	JLabel connectUsersLabel;
	JTextArea connectedUsers;
	Socket s;
	BufferedReader br;

	public Client(String name) throws IOException {
		this.s = new Socket("localhost", 60000);
		PrintWriter o = new PrintWriter(this.s.getOutputStream(), true);
		o.println(name);
		this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		frame = new JFrame(name);
		messageLabel = new JLabel("Enter message");
		messageLabel.setBounds(100, 60, 100, 30);

		message = new JTextField();
		message.setBounds(100, 100, 500, 30);

		sendbtn = new JButton("Send");
		sendbtn.setBounds(610, 100, 70, 30);
		sendbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String str = message.getText();
				if (str != null) {
					try (PrintWriter o = new PrintWriter(s.getOutputStream(), true);) {
						o.println(str);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		});

		allMsgsArea = new JLabel("Messages");
		allMsgsArea.setBounds(100, 250, 100, 30);

		displayArea = new JTextArea();
		displayArea.setBounds(100, 300, 500, 200);
		displayArea.setEditable(false);

		connectUsersLabel = new JLabel("Connected Users");
		connectUsersLabel.setBounds(750, 100, 100, 30);

		connectedUsers = new JTextArea();
		connectedUsers.setBounds(750, 150, 150, 300);
		connectedUsers.setEditable(false);

		frame.setLayout(null);
		frame.setSize(1000, 800);
		frame.add(messageLabel);
		frame.add(message);
		frame.add(sendbtn);
		frame.add(connectUsersLabel);
		frame.add(allMsgsArea);
		frame.add(displayArea);
		frame.add(connectedUsers);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		Client c = new Client(name);
		new Thread(c).start();

	}

	@Override
	public void run() {
		try {
			while (true) {
				String s = br.readLine();
				if (br != null) {
					displayArea.append(s + "\n");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
