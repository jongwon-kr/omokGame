package testSocket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements Runnable {
	private JTextArea textArea = new JTextArea();
	private JTextField nameField = new JTextField();
	private JTextField textField = new JTextField();
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;

	public Client() {
		setTitle("client");
		setSize(740, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		setVisible(true);

		textArea.setBounds(10, 10, 700, 300);
		add(textArea);

		nameField.setBounds(10, 320, 200, 50);
		add(nameField);

		textField.setBounds(10, 380, 700, 50);
		add(textField);

		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					writer.println(nameField.getText() + " : " + textField.getText());
					textField.setText("");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public void run() {
		while (true) {
			try {
				textArea.append(reader.readLine() + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void connect() {
		try {
			socket = new Socket("192.168.35.250", 1234);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			new Thread(this).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.connect();
	}
}
