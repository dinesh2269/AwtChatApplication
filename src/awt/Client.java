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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements Runnable {
	JFrame frame;
	JLabel messageLabel;
	JTextField message;
	JButton sendbtn;
	JLabel allMsgsArea;
	JTextArea displayArea;
	JScrollPane scrollArea;
	JLabel connectUsersLabel;
	JTextArea connectedUsers;
	Socket s;
	BufferedReader br;
	PrintWriter out;

	public Client(Socket s, String name, BufferedReader br, PrintWriter out) throws IOException {
		this.s = s;
		this.br = br;
		this.out = out;
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
				String str = message.getText();
				message.setText(null);
				if (str != null) {
					out.println(str);
				}
			}
		});

		allMsgsArea = new JLabel("Messages");
		allMsgsArea.setBounds(100, 250, 100, 30);

		displayArea = new JTextArea();
		displayArea.setEditable(false);

		scrollArea = new JScrollPane(displayArea);
		scrollArea.setBounds(100, 300, 500, 200);
		// scrollArea.setPreferredSize(new Dimension(350, 100));
		scrollArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.add(scrollArea);

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
		// frame.add(scrollArea);
		// frame.add(displayArea);
		frame.add(connectedUsers);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void usersList() {
		String data;
		try {
			data = this.br.readLine();
			String users[] = data.split(" ");
			for (String s : users) {
				connectedUsers.append(s + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s = new Socket("localhost", 60000);
		PrintWriter o = new PrintWriter(s.getOutputStream(), true);
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Client Name:");
		String name = sc.nextLine();
		o.println(name);
		Client c = new Client(s, name, br, o);
		c.usersList();
		new Thread(c).start();

	}

	@Override
	public void run() {
		try {
			while (true) {
				String s = br.readLine();
				if (s != null) {
					this.displayArea.append(s + "\n");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error" + e);
		}

	}
}
