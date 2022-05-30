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
			System.out.println("서버가 시작되었습니다.");

			while (true) {
				socket = serverSocket.accept();
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");
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
				System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");

				while (true) {
					text = in.readUTF();
					if (text.contains("∥")) {// 오목 알을 두었을 때
						sendToAll(text);
					} else if (text.contains("†")) {
						text = name + " : " + text;
						sendToAll(text);
					} else if (text.equals("★")) {
						sendToAll(name + text);
					} else if (text.equals("※")) {
						sendToAll(name+"※");
					}
				}
			} catch (IOException e) {

			} finally {
				userCnt--;
				sendToAll(name + "님이 나가셨습니다.");
				System.out.println("접속자 닉네임");
				clients.remove(name);
				sendToAll("접속 인원 : " + String.valueOf(clients.size() + "명"));
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속을 종료하였습니다.");
				System.out.println("현재 게임접속자는  " + clients.size() + "명 입니다.");
			}
		}
	}
}
