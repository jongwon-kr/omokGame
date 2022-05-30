package omok;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class OmokServer {
	static int userCnt = 0;
	HashMap clients;

	OmokServer() {
		clients = new HashMap();
		Collections.synchronizedMap(clients);
	}

	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("������ ���۵Ǿ����ϴ�.");

			while (true) {
				socket = serverSocket.accept();
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "���� �����Ͽ����ϴ�.");
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void sendToAll(String msg) {
		Iterator it = clients.keySet().iterator();

		while (it.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream) clients.get(it.next());
				out.writeUTF(msg);
			} catch (IOException e) {

			}
		}
	}

	public static void main(String[] args) {
		new OmokServer().start();
	}

	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		DataOutputStream out;

		ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {

			}
		}

		public void run() {
			String text = "";
			String name = "";
			try {
				name = in.readUTF();
				clients.put(name, out);
				System.out.println("���� ���������� ���� " + clients.size() + "�Դϴ�.");

				while (true) {
					text = in.readUTF();
					if (text.contains("��")) {// ���� ���� �ξ��� ��
						sendToAll(text);
					} else if (text.contains("��")) {
						text = name + " : " + text;
						sendToAll(text);
					} else if (text.equals("��")) {
						sendToAll(name + text);
					} else if (text.equals("��")) {
						sendToAll(name+"��");
					}
				}
			} catch (IOException e) {

			} finally {
				userCnt--;
				sendToAll(name + "���� �����̽��ϴ�.");
				System.out.println("������ �г���");
				clients.remove(name);
				sendToAll("���� �ο� : " + String.valueOf(clients.size() + "��"));
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "���� ������ �����Ͽ����ϴ�.");
				System.out.println("���� ���������ڴ�  " + clients.size() + "�� �Դϴ�.");
			}
		}
	}
}
