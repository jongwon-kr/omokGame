package omok;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class OmokClient extends JFrame {
	static int selNum = 9;
	static int nSelNum = 10;
	Com com = new Com();
	static String serverIp = "127.0.0.1";
	static int sec = 0;
	Socket socket;
	static boolean vsComCheck = false;
	static boolean turn = false;
	static boolean gameStart = false;
	static int vsComWinCheck = 0; // 1�϶� ��ǻ�� �¸� 2�϶� ����� �¸�
	JPanel omokBoard = new BPanel();
	JPanel statusBoard = new JPanel();
	static JFrame gameFrm = new JFrame("Omok Game");
	JLabel nameL = new JLabel("�̸�");
	JLabel chat = new JLabel("ä��â");
	JTextField inName = new JTextField(); // �̸� �Է�
	JButton nameBtn = new JButton(); // �̸� �Է� ��ư
	JButton vsPerson = new JButton(); // 1:1 ��� ��ư
	JButton vsCom = new JButton(); // ��ǻ�� ��� ��ư
	JButton white = new JButton(); // �鵹(�İ�)
	JButton black = new JButton(); // �鵹(�İ�)
	JLabel turnCheck = new JLabel(); // �� Ȯ�� ��
	JTextField inChat = new JTextField(); // ä�� �Է�ĭ
	JButton sendChat = new JButton();
	JScrollPane chatPane = new JScrollPane(chatBoard);
	JButton restartBtn = new JButton(); // �����
	JButton endBtn = new JButton(); // ����
	JMenuBar menuBar = new JMenuBar(); // �޴���
	JMenu setGame = new JMenu("���Ӽ���");
	JMenuItem restart = new JMenuItem("�����(��ǻ�� ����)");
	JMenuItem exit = new JMenuItem("����");
	JMenu info = new JMenu("����"); // ����
	JMenuItem gameInfo = new JMenuItem("���Ӽ���");
	JMenuItem producerInfo = new JMenuItem("����������");
	JDialog gameInfoDi = new JDialog(this, "���Ӽ���");
	JDialog producerInfoDi = new JDialog(gameFrm, "����������");
	static JDialog finDi = new JDialog(gameFrm, "���Ӱ��");
	static JLabel finLabel = new JLabel();

	JButton finBtn1 = new JButton("��������");
	JButton finBtn2 = new JButton("����ϱ�");
	JLabel gameInfoL = new JLabel(
			"<html><body>1. �����̸� : ����<br />2. ���Ӽ��� : ���� ��, �İ� �鵹�� ������ ����, ����, <br />  �밢���� ���� 5���� ���� ���¼����� �¸��մϴ�.<br />3. ���� ��� : 1:1 �¶���, ��ǻ�Ͷ� �ϱ�</body></html>",
			JLabel.HORIZONTAL);
	JLabel producerInfoL = new JLabel("<html><body>�������̸� : ������<br />���۳⵵ : 2022��<br />  </body></html>",
			JLabel.HORIZONTAL);
	static JLabel enemyName = new JLabel();
	static Timer timer = new Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			sec++;
			int min = sec / 60, showSec = sec % 60;
			String choiceTime = String.valueOf(min) + "��" + String.valueOf(showSec) + "��";
			Mytimer.setText(choiceTime);
		}
	});
	static JLabel Mytimer = new JLabel(); // Ÿ�̸� ��
	static JLabel choicing = new JLabel(); // ��밡 ������
	static JTextArea chatBoard = new JTextArea(); // ä��â
	static Image whiteStone = new ImageIcon(OmokClient.class.getResource("whilteStone.png")).getImage();
	static Image blackStone = new ImageIcon(OmokClient.class.getResource("blackStone.png")).getImage();
	static Image selStone, nSelStone;
	static String name = "";
	static String sendText;
	static JButton[][] stones = new JButton[19][19];
	static int[][] omokArr = new int[19][19];

	OmokClient(Socket socket, Thread sender) {
		this.socket = socket;
		int x = 0, y = 0, cnt = 0;
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				JButton stone = new JButton();
				stone.setName(i + "��" + j);
				stone.addActionListener(new EventHandler(stone, socket, sender));
				stone.setCursor(new Cursor(Cursor.HAND_CURSOR));
				stone.setBounds(x, y, 25, 25);
				stone.setBorderPainted(false);
				stone.setContentAreaFilled(false);
				stone.setFocusPainted(false);
				stone.setOpaque(false);
				stone.setVisible(true);
				omokBoard.add(stone);
				stones[i][j] = stone;
				x += 29;
				cnt++;
				if (cnt == 19) {
					y += 29;
					x = 0;
					cnt = 0;
				}
			}
		}
		inName.setBorder(new LineBorder(Color.gray));
		inName.addActionListener(new EventHandler(nameBtn, inName, socket, sender));
		inName.setBounds(45, 10, 180, 25);

		nameBtn.setMargin(null);
		nameBtn.setFont(new Font("Dialog", Font.BOLD, 12));
		nameBtn.setBounds(82, 45, 90, 30);
		nameBtn.setText("�����ϱ�");
		nameBtn.setFocusable(false);
		nameBtn.setBackground(Color.white);
		nameBtn.addActionListener(new EventHandler(nameBtn, inName, socket, sender));

		vsPerson.setBounds(52, 90, 150, 30);
		vsPerson.setText("1:1 ���");
		vsPerson.setFont(new Font("Dialog", Font.BOLD, 12));
		vsPerson.setBackground(Color.white);
		vsPerson.setFocusable(false);
		vsPerson.addActionListener(new EventHandler(vsPerson, socket, sender));
		vsPerson.setVisible(false);

		black.setBounds(30, 90, 90, 30);
		black.setText("����");
		black.setFont(new Font("Dialog", Font.BOLD, 12));
		black.setForeground(Color.red);
		black.setBackground(Color.white);
		black.setFocusable(false);
		black.addActionListener(new EventHandler(black, socket, sender));
		black.setVisible(false);

		white.setBounds(130, 90, 90, 30);
		white.setText("�İ�");
		white.setFont(new Font("Dialog", Font.BOLD, 12));
		white.setForeground(Color.blue);
		white.setBackground(Color.white);
		white.setFocusable(false);
		white.addActionListener(new EventHandler(white, socket, sender));
		white.setVisible(false);

		inChat.setBounds(10, 490, 184, 30);
		inChat.setBorder(new LineBorder(Color.gray));
		inChat.addActionListener(new EventHandler(sendChat, inChat, socket, sender));
		inChat.setVisible(false);

		sendChat.setBounds(194, 490, 50, 30);
		sendChat.setText("��");
		sendChat.setFont(new Font("Dialog", Font.BOLD, 12));
		sendChat.setBackground(Color.white);
		sendChat.addActionListener(new EventHandler(sendChat, inChat, socket, sender));
		sendChat.setFocusable(false);
		sendChat.setVisible(false);
		statusBoard();
		omokBoard.setBounds(0, 0, 550, 550);

		gameInfoDi.setSize(400, 200);
		gameInfoDi.setLayout(new FlowLayout());
		gameInfoL.setFont(new Font("Dialog", Font.BOLD, 13));
		gameInfoDi.add(gameInfoL);
		gameInfoDi.setLocationRelativeTo(null);

		producerInfoDi.setName("����������");
		producerInfoDi.setSize(200, 100);
		producerInfoDi.setLayout(new FlowLayout());
		producerInfoDi.add(producerInfoL);
		producerInfoDi.setLocationRelativeTo(null);

		gameInfo.addActionListener(new MenuHandler(gameInfo));
		info.add(gameInfo);
		producerInfo.addActionListener(new MenuHandler(producerInfo));
		info.add(producerInfo);
		restart.addActionListener(new MenuHandler(restart));
		setGame.add(restart);
		exit.addActionListener(new MenuHandler(exit));
		setGame.add(exit);
		menuBar.add(setGame);
		menuBar.add(info);
		gameFrm.setJMenuBar(menuBar);

		finDi.setSize(220, 120);
		finDi.setLayout(new FlowLayout());
		finDi.setLocationRelativeTo(null);
		finLabel.setFont(new Font("Dialog", Font.BOLD, 15));
		finDi.add(finLabel);
		finBtn1.setFont(new Font("Dialog", Font.BOLD, 12));
		finBtn1.setBackground(Color.white);
		finBtn1.addActionListener(new EventHandler(finBtn1));
		finBtn1.setFocusable(false);
		finBtn2.setFont(new Font("Dialog", Font.BOLD, 12));
		finBtn2.setBackground(Color.white);
		finBtn2.addActionListener(new EventHandler(finBtn2));
		finBtn2.setFocusable(false);
		finDi.add(finBtn1);
		finDi.add(finBtn2);
		finDi.setVisible(false);

		gameFrm.add(omokBoard);
		gameFrm.setLayout(null);
		gameFrm.setSize(820, 614);
		gameFrm.setLocationRelativeTo(null);
		gameFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrm.setVisible(true);
	}

	public void statusBoard() {
		nameL.setBounds(10, 10, 55, 25);
		nameL.setFont(new Font("Dialog", Font.BOLD, 15));
		inName.setBorder(new LineBorder(Color.gray));
		inName.setBounds(45, 10, 180, 25);

		vsCom.setBounds(52, 130, 150, 30);
		vsCom.setText("��ǻ�Ͷ� ���");
		vsCom.setFont(new Font("Dialog", Font.BOLD, 12));
		vsCom.setBackground(Color.white);
		vsCom.setFocusable(false);
		vsCom.addActionListener(new EventHandler(vsCom));
		vsCom.setVisible(false);

		enemyName.setBounds(60, 130, 174, 20);
		enemyName.setFont(new Font("Dialog", Font.BOLD, 12));
		enemyName.setHorizontalAlignment(JLabel.RIGHT);
		enemyName.setVisible(false);

		chat.setBounds(10, 130, 50, 20);
		chat.setFont(new Font("Dialog", Font.BOLD, 15));
		chat.setVisible(false);

		chatPane.setBounds(10, 150, 234, 340);
		chatPane.setVisible(false);
		chatPane.setBorder(new LineBorder(Color.gray));
		chatBoard.setLineWrap(true);

		choicing.setText("��밡 ���� �������Դϴ�...");
		choicing.setBounds(0, 95, 254, 30);
		choicing.setFont(new Font("Dialog", Font.BOLD, 15));
		choicing.setForeground(Color.black);
		choicing.setHorizontalAlignment(JLabel.CENTER);
		choicing.setVisible(false);

		Mytimer.setText("0��0��");
		Mytimer.setBounds(0, 95, 254, 30);
		Mytimer.setFont(new Font("Dialog", Font.BOLD, 15));
		Mytimer.setForeground(Color.black);
		Mytimer.setHorizontalAlignment(JLabel.CENTER);
		Mytimer.setVisible(false);

		statusBoard.add(enemyName);
		statusBoard.add(choicing);
		statusBoard.add(Mytimer);
		statusBoard.add(black);
		statusBoard.add(white);
		statusBoard.add(chat);
		statusBoard.add(inChat);
		statusBoard.add(vsPerson);
		statusBoard.add(vsCom);
		statusBoard.add(chatPane);
		statusBoard.add(nameL);
		statusBoard.add(inName);
		statusBoard.add(nameBtn);
		statusBoard.add(sendChat);
		statusBoard.setBounds(550, 0, 254, 550);
		statusBoard.setLayout(null);
		statusBoard.setBackground(Color.getHSBColor(44, 21, 100));
		gameFrm.add(statusBoard);
	}

	class MenuHandler implements ActionListener {
		JMenuItem JMI;

		MenuHandler(JMenuItem JMI) {
			this.JMI = JMI;
		}

		public void actionPerformed(ActionEvent e) {
			if (JMI.getText().equals("�����(��ǻ�� ����)") && vsComCheck) {
				black.setVisible(true);
				white.setVisible(true);
				chatPane.setVisible(false);
				turn = false;
				choicing.setVisible(false);
				for (int i = 0; i < 19; i++) {
					for (int j = 0; j < 19; j++) {
						stones[i][j].setIcon(null);
						stones[i][j].setCursor(new Cursor(Cursor.HAND_CURSOR));
						Com.comRealArr[i][j] = 0;
						omokArr[i][j] = 0;
					}
				}
			} else if (JMI.getText().equals("����")) {
				System.exit(0);
			} else if (JMI.getText().equals("���Ӽ���")) {
				gameInfoDi.setVisible(true);
			} else if (JMI.getText().equals("����������")) {
				producerInfoDi.setVisible(true);
			}
		}

	}

	class BPanel extends JPanel {
		private Image omokBoard = new ImageIcon(OmokClient.class.getResource("omokBoard.png")).getImage();

		BPanel() {
			this.setLayout(null);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.drawImage(omokBoard, 0, 0, this);
		}
	}

	class EventHandler implements ActionListener {
		private JTextField tf = new JTextField();
		private JButton b = new JButton();
		private int x, y;
		Socket socket;
		Thread sender = new Thread(new ClientSender(socket));

		EventHandler(JButton b) {
			this.b = b;
		}

		EventHandler(JButton b, Socket socket, Thread sender) {
			this.b = b;
			this.socket = socket;
			this.sender = sender;
		}

		EventHandler(JButton b, JTextField tf, Socket socket, Thread sender) {
			this.b = b;
			this.tf = tf;
			this.socket = socket;
			this.sender = sender;
		}

		public void actionPerformed(ActionEvent e) {
			if (b.getText().equals("�����ϱ�") && !tf.getText().equals("")) {
				name = tf.getText(); // �г��� ����
				nameL.setEnabled(false);
				tf.setEnabled(false);
				vsPerson.setVisible(true);
				vsCom.setVisible(true);
				b.setEnabled(false);
				b.setText("������");
				sendText = name;
				System.out.println("�����ϱ�");
				sender.run();
			} else if (b.getText().equals("1:1 ���")) {
				b.setVisible(false);
				vsCom.setVisible(false);
				chat.setVisible(true);
				enemyName.setVisible(true);
				chatPane.setVisible(true);
				inChat.setVisible(true);
				sendChat.setVisible(true);
				black.setVisible(true);
				white.setVisible(true);
				chatBoard.append("ä��â�� ���ӵǾ����ϴ�.\n");
			} else if (b.getText().equals("��ǻ�Ͷ� ���")) {
				vsComCheck = true;
				b.setVisible(false);
				black.setVisible(true);
				white.setVisible(true);
				vsPerson.setVisible(false);
			} else if (gameStart && turn && b.getText().equals("")) {
				x = Integer.valueOf(b.getName().split("��")[0]);
				y = Integer.valueOf(b.getName().split("��")[1]);
				choicing.setVisible(true);
				if (omokArr[x][y] == 0) {
					b.setIcon(new ImageIcon(selStone));
					b.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					omokArr[x][y] = selNum;
					if (vsComCheck) {
						Com.comRealArr[x][y] = nSelNum; // ��ǻ�� ������ üũ
						com.comChoiceStone();
						if (vsComWinCheck == 1) { // ��ǻ�� �¸�
							finLabel.setText("�¸��ϼ̽��ϴ�!!");
							finDi.setVisible(true);
							vsComWinCheck = 0;
						} else if (vsComWinCheck == 2) { // ����� �¸�
							finLabel.setText("�й��ϼ̽��ϴ�..");
							finDi.setVisible(true);
							vsComWinCheck = 0;
						}
					} else if (!vsComCheck) {
						Mytimer.setVisible(false);
						timer.stop();
						sendText = b.getName();
						sender.run();
						checkStones(socket, sender);
					}
					System.out.println(turn);
//					for (int i = 0; i < 19; i++) {
//						for (int j = 0; j < 19; j++) {
//							System.out.printf("%2d", omokArr[i][j]);
//						}
//						System.out.println();
//					}
				}
			} else if (b.getText().equals("����")) {
				turn = true;
				b.setVisible(false);
				white.setVisible(false);
				selStone = blackStone;
				nSelStone = whiteStone;
				chatPane.setVisible(true);
				if (vsComCheck) {
					gameStart = true;
					choicing.setText("�浹�Դϴ�.");
					choicing.setVisible(true);
				} else {
					sendText = "��";
					sender.run();
					if (gameStart) {
						Mytimer.setText("�����Դϴ� ���� �����ּ���");
					} else {
						Mytimer.setText("��븦 ��ٸ��� ���Դϴ�.");
					}
					Mytimer.setVisible(true);
				}
			} else if (b.getText().equals("�İ�")) {
				turn = false;
				b.setVisible(false);
				black.setVisible(false);
				selStone = whiteStone;
				nSelStone = blackStone;
				chatPane.setVisible(true);
				if (vsComCheck) {
					gameStart = true;
					omokArr[9][9] = nSelNum;
					Com.comRealArr[9][9] = selNum;
					stones[9][9].setIcon(new ImageIcon(nSelStone));
					turn = true;
					choicing.setText("�鵹�Դϴ�.");
					choicing.setVisible(true);
				} else {
					sendText = "��";
					sender.run();
					if (gameStart) {
						choicing.setText("��밡 ���� �������Դϴ�...");
					} else {
						choicing.setText("��븦 ��ٸ��� ���Դϴ�.");
					}
					choicing.setVisible(true);
				}
			} else if (!tf.getText().equals("") && b.getText().equals("��")) {
				sendText = tf.getText() + "��";
				sender.run();
				tf.setText("");
			} else if (b.getText().equals("��������")) {
				System.exit(0);
			} else if (b.getText().equals("����ϱ�")) {
				finDi.setVisible(false);
			}
		}
	}

	public void comSelect() {
		stones[12][12].setIcon(new ImageIcon(nSelStone));
	}

	public static void main(String[] args) {

		try {
			sendText = name;
			// ������ �����Ͽ� �����û
			Socket socket = new Socket(serverIp, 7777);
			Thread sender = new Thread(new ClientSender(socket));
			Thread receiver = new Thread(new ClientReceiver(socket));
			new OmokClient(socket, sender);
			receiver.start();
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ClientReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		String reText;
		private int x, y, cnt = 0;

		ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {

			}
		}

		// ���� ��
		public void run() {
			while (true) {
				try {
					reText = in.readUTF();
					if (reText.contains("��")) { // ���� �� ��ġ
						x = Integer.valueOf(reText.split("��")[0]);
						y = Integer.valueOf(reText.split("��")[1]);
						if (omokArr[x][y] == 0) {
							omokArr[x][y] = nSelNum;
							stones[x][y].setIcon(new ImageIcon(nSelStone));
							stones[x][y].setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							choicing.setVisible(false);
							sec = 0;
							timer.start();
							Mytimer.setVisible(true);
							turn = true;
						} else {
							turn = false;
						}
					} else if (reText.contains("��")) {
						reText = reText.split("��")[0];
						chatBoard.append(reText + "\n");
						chatBoard.setCaretPosition(chatBoard.getDocument().getLength());
						System.out.println(reText);
					} else if (reText.contains("��")) {
						Mytimer.setVisible(false);
						choicing.setVisible(false);
						if (reText.contains(name)) {
							finLabel.setText("�¸��ϼ̽��ϴ�.");
							finDi.setVisible(true);
							turn = false;
						} else {
							finLabel.setText("�й��ϼ̽��ϴ�.");
							finDi.setVisible(true);
							turn = false;
						}
					} else if (reText.contains("��")) {
						cnt++;
						if (!reText.split("��")[0].equals(name)) {
							enemyName.setText("��� : " + reText.split("��")[0]);
						}
						if (cnt == 2) {
							gameStart = true;
							timer.start();
							choicing.setText("��밡 ���� �������Դϴ�...");
						}
					}
				} catch (IOException e) {

				}
			}
		}
	}

	static class ClientSender extends Thread {
		Socket socket;
		DataOutputStream out;

		ClientSender(Socket socket) {
			this.socket = socket;
			try {
				out = new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) {
			}
		}

		public void run() {
			try {
				out.writeUTF(sendText);
				System.out.println(sendText + "�� ���½��ϴ�.");
			} catch (IOException ie) {
			}
		}
	}

	static void checkStones(Socket socket, Thread sender) {
		int cnt = 0, i = 0, j = 0;
		for (i = 0; i < 19; i++) {
			for (j = 0; j < 19; j++) {
				if (omokArr[i][j] == selNum) {
					if (i <= 14) { // �Ʒ��� üũ
						cnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) { // �Ʒ��� üũ
							if (omokArr[l++][h] == selNum) {
								cnt++;
								System.out.println("�Ʒ�");
							}
						}
					}
					if (cnt == 5) {
						break;
					}
					if (i > 3) { // ���� üũ
						cnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) { // �Ʒ��� üũ
							if (omokArr[l--][h] == selNum) {
								cnt++;
							}
						}
					}
					if (cnt == 5) {
						break;
					}
					if (j <= 14) { // ���������� üũ
						cnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) { // �Ʒ��� üũ
							if (omokArr[l][h++] == selNum) {
								cnt++;
							}
						}
					}
					if (cnt == 5) {
						break;
					}
					if (j > 3) { // �������� üũ
						cnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) { // �Ʒ��� üũ
							if (omokArr[l][h--] == selNum) {
								cnt++;
							}
						}
					}
					if (cnt == 5) {
						break;
					}
					if (i <= 14 && j <= 14) { // ������ �Ʒ� �밢�� üũ
						cnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) {
							if (omokArr[l++][h++] == selNum) {
								cnt++;
							}
						}
					}
					if (cnt == 5) {
						break;
					}
					if (i > 3 && j <= 14) { // ������ �� �밢�� üũ
						cnt = 0;
						int l = i, h = j;
						for (int k = 0; k < 5; k++) {
							if (omokArr[l--][h++] == selNum) {
								cnt++;
							}
						}
					}
					if (cnt == 5) {
						break;
					}
				}
			}
			if (cnt == 5) {
				sendText = "��";
				sender.run();
				break;
			}
			cnt = 0;
		}
	}
}
