package remoteConnect;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RobotClient extends JFrame implements ActionListener, Runnable, Serializable {
	Robot r;
	ServerSocket server_socket;
	Socket socket_to_host;
	String host_address = "127.0.0.1";
	String s_local_address;
	// server와 통신할 port번호
	int port_to_host_number = 2222;
	final int screen_port = 3333;
	// client끼리 통신할 port번호
	int p2p_port_number = 1058;
	// 공유 키
	int shareKey = 0;
	boolean connectCheck = false, connectOn = false;

	Vector v_client_address;

	BufferedReader in = null;
	static PrintWriter out;

	// 화면 전송을 위한 bufferedInputStream, outputStream
	BufferedInputStream bin;
	BufferedOutputStream bout;

	Thread t_connection;
	String pathname, connectKey;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	final int SCREEN_WIDTH = screenSize.width; // 화면 가로 너비
	final int SCREEN_HEIGHT = screenSize.height; // 화면 세로 너비
	static Image img = null; // 생성자. UI 배치.

	int shareTime = 600;
	JPanel top_panel; // 상단 패널
	JButton connect; // 연결 버튼
	JButton makeShareKey; // 공유키 생성
	JTextField conTf; // 연결 textfield

	JPanel centerLeft_panel; // 중앙 패널
	JPanel centerRight_panel;
	JSplitPane centerSplitPane; // 중앙 splitpane
	JSplitPane mainSplitPane; // 메인화면 splitpane
	JDialog dialog;

	JMenuBar menubar;
	JMenu menu;
	JMenuItem server_ip;

	public RobotClient() {
		super("원격 연결");
		ConnectCreation();
		getContentPane().add(setUI()).setBackground(Color.white);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(484, 363);
		menuSet();
		setVisible(true);
	}

	public int ConnectCreation() {
		try {
			socket_to_host = new Socket(host_address, port_to_host_number);

			server_socket = new ServerSocket(p2p_port_number);
			getIpAddress();

			in = new BufferedReader(new InputStreamReader(socket_to_host.getInputStream()));
			out = new PrintWriter(socket_to_host.getOutputStream(), true);

			P2p_server robot_server = new P2p_server(server_socket);
			// p2p서버 클래스
		} catch (UnknownHostException e) {
			Alert("경고", "알수없는 호스트입니다.");
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			Alert("경고", "연결에 실패하였습니다.");
			return 0;
		} // try-catch
		return 1;
	}

	public void getIpAddress() {
		InetAddress local_ip = socket_to_host.getLocalAddress();
		s_local_address = local_ip.toString();
		// 위에서 구하면 /127.0.0.1 처럼 나오는데, 앞의 슬래쉬(/) 를 제거하기 위한 코드 부분
		for (int i = s_local_address.length() - 1; i >= 0; i--) {
			if (s_local_address.charAt(i) == '/') {
				s_local_address = s_local_address.substring(i + 1);
				break;
			} // if
		} // for
	}

	// Main화면 UI설정
	public Component setUI() {
		top_panel = new JPanel(new FlowLayout());
		centerLeft_panel = new JPanel();
		centerRight_panel = new JPanel();

		conTf = new JTextField();
		conTf.setPreferredSize(new Dimension(100, 30));

		// 연결 버튼
		connect = new JButton("연결");
		connect.setBackground(Color.white);
		connect.setFocusable(false);
		connect.setFont(new Font("Dialog", Font.BOLD, 12));
		connect.setPreferredSize(new Dimension(60, 30));
		connect.setActionCommand("connect");
		connect.addActionListener(this);

		// 공유키 생성 버튼
		makeShareKey = new JButton("공유키 생성");
		makeShareKey.setBackground(Color.white);
		makeShareKey.setFocusable(false);
		makeShareKey.setFont(new Font("Dialog", Font.BOLD, 12));
		makeShareKey.setPreferredSize(new Dimension(120, 30));
		makeShareKey.setActionCommand("makeShareKey");
		makeShareKey.addActionListener(this);

		centerLeft_panel.setPreferredSize(new Dimension(100, 300));
		top_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		top_panel.add(conTf);
		top_panel.add(connect);
		top_panel.add(makeShareKey);
		centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerLeft_panel, centerRight_panel);
		mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top_panel, centerSplitPane);
		return mainSplitPane;
	}

	public void menuSet() {
		menubar = new JMenuBar();
		menu = new JMenu("설정");
		server_ip = new JMenuItem("Server IP 설정");
		server_ip.addActionListener(this);
		server_ip.setActionCommand("server_ip");
		menu.add(server_ip);
		setJMenuBar(menubar);
	}

	public void Alert(String alert_title, String alert_message) {
		// alert 메소드
		dialog = new JDialog(this, alert_title, true);
		JLabel lll = new JLabel(alert_message);
		lll.setVerticalTextPosition(SwingConstants.CENTER);
		lll.setHorizontalTextPosition(SwingConstants.CENTER);
		JPanel ttt = new JPanel();
		ttt.add(lll);
		dialog.setLocation(180, 80);
		dialog.setSize(320, 100);
		dialog.setContentPane(ttt);
		dialog.show();
	}

	// 이벤트
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		Thread t, rt;
		if (command.equals("connect")) {
			connectKey = conTf.getText();
			out.println("#share#");
			try {
				int cnt = 0;
				while (!connectCheck) {
					Thread.sleep(1000);
					System.out.println("서버에 접속중입니다.");
					cnt++;
					if (cnt == 6)
						break;
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (connectCheck) {
				Alert("접속 성공", "접속에 성공했습니다.");
				out.println("#share#");
				out.println("#connectSuccess#");
				t = new Thread(new ReceiveScreen(socket_to_host));
				t.start();
			} else {
				Alert("접속 실패", "원격접속에 실패했습니다.");
			}
		} else if (command.equals("makeShareKey")) {
			out.println("공유키가 생성되었습니다.");
			shareKey = (int) (Math.random() * 100000);
			System.out.println(shareKey);
			makeShareKey.setVisible(false);
			connect.setVisible(false);
			conTf.setText("공유키 : " + shareKey);
			conTf.disable();
			Alert("공유", "공유키 : " + String.valueOf(shareKey));
		}
	}

	public void run() {
		String in_msg = null;
		Thread st;
		System.out.println("run");
		try {
			r = new Robot();
			while (true) {
				in_msg = in.readLine();
				System.out.println("넘어온 메시지" + in_msg);
				// 보통 #c#이 넘어오다가 검색하면 #s#sdfsdfwef 이 넘어옴
				// System.out.println(in_msg)
				if (in_msg != null) {
					if (in_msg.startsWith("#connect#")) {
						if (shareKey != 0)
							out.println("#shareKey#" + shareKey);
					}
					if (in_msg.startsWith("#press#") && shareKey != 0) {
						r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					} else if (in_msg.startsWith("#release#") && shareKey != 0) {
						r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
					} else if (in_msg.startsWith("#drag#") && shareKey != 0) {
						r.mouseMove(Integer.valueOf(in_msg.substring(6).split(":")[0]),
								Integer.valueOf(in_msg.substring(6).split(":")[1]));
					} else if (in_msg.startsWith("#move#") && shareKey != 0) {
						r.mouseMove(Integer.valueOf(in_msg.substring(6).split(":")[0]),
								Integer.valueOf(in_msg.substring(6).split(":")[1]));
					} else if (in_msg.startsWith("#shareKey#") && connectKey != null) {
						if (connectKey.equals(in_msg.substring(10))) {
							connectCheck = true;
						}
					} else if (in_msg.startsWith("#share#") && shareKey != 0) {
						// 화면 공유 시작
						SendScreen sc = new SendScreen(socket_to_host);
						sc.start();
					} else if (in_msg.startsWith("#send#") && shareKey == 0) {
						System.out.println("이미지 받음");
					} else if (in_msg.startsWith("#connectSuccess#")) {
						connectOn = true;
					}
				} else {
					break;
				}
			} // while
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// run

	class P2p_server extends Thread {
		ServerSocket p2p_server_socket;
		Socket p2p_client;
		PrintWriter requestor;

		public P2p_server(ServerSocket ss) {
			p2p_server_socket = ss;
			start();
		}

		public synchronized void setRequestor(PrintWriter requestor) {
			this.requestor = requestor;
		}

		public void run() {
			while (true) {
				try {
					p2p_client = p2p_server_socket.accept();
					System.out.println("p2p_server run");
				} catch (Exception ex) {

				}
				P2p_connection pc = new P2p_connection(p2p_client, this);
				pc.start();
			}
		}
	}

	class P2p_connection extends Thread {
		Socket socket;
		P2p_server p2p_server;
		BufferedReader in;
		FileInputStream fis;
		File file;
		String file_name;
		BufferedOutputStream out;

		public P2p_connection(Socket s, P2p_server p2p) {
			socket = s;
			p2p_server = p2p;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new BufferedOutputStream(socket.getOutputStream());
			} catch (Exception ex) {

			}
		}

		public void run() {
			String msg = "";
			System.out.println("p2p_connection run");
			try {
				while (true) {
					msg = in.readLine();
					if (msg != null) {
						System.out.println("incoming msg : " + msg);
						if (msg.startsWith("#r#")) {
							file_name = msg.substring(3);
							file = new File(pathname + file_name);
							fis = new FileInputStream(file);
							int c;
							while ((c = fis.read()) != -1) {
								out.write(c);
							}
							out.flush();
							break;
						}
					} else {
						break;
					}
				}
			} catch (Exception ex) {
				System.err.println("in the P2p_connection : " + ex);
			} finally {
				try {
					out.close();
					fis.close();
					in.close();
				} catch (Exception exc) {
					System.err.println("in the P2p_connection finally : " + exc);
				}
			}
		}

	}

	class RemoteScreen {
		Robot r;

		public RemoteScreen() {
			try {
				r = new Robot();
				while (true) {

				}
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	// 접속을 요청하는 컴퓨터에게 화면 전송
	class SendScreen extends Thread implements Serializable {
		Socket socket;
		ObjectOutputStream oos;

		public SendScreen(Socket s) {
			this.socket = s;
			try {
				oos = new ObjectOutputStream(s.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

			try {
				while (true) {
					Thread.sleep(10);
					if (!connectOn) {
						out.println("#shareKey#" + shareKey);
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public Image capture() {
			Robot robot;
			BufferedImage bufImage = null;
			try {
				robot = new Robot();
				Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				bufImage = robot.createScreenCapture(area); // Robot 클래스를 이용하여 스크린 캡쳐.
				// this.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bufImage;
		}
	}

	// 접속요청한 화면 받아오기
	class ReceiveScreen extends JFrame implements Runnable, MouseListener, MouseMotionListener {
		boolean onScreen = false;
		Socket socket;

		ObjectInputStream ois;

		public ReceiveScreen(Socket s) {
			super("접속화면");
			this.socket = s;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			onScreen = true;
			addMouseListener(this);
			addMouseMotionListener(this);
			setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
			setVisible(true);
		}

		public void run() {
			try {
				BufferedImage image = null;
				while (onScreen) {
					Thread.sleep(10);
					try {
						System.out.println("이미지 읽는중");
						image = (BufferedImage) ois.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					if (image != null) { // image가 null이 아닌 경우
						int w = this.getWidth();
						int h = this.getHeight();
						img = image.getScaledInstance(w, h, Image.SCALE_DEFAULT);
						// this.repaint();
						this.drawImage(img, w, h);
					} else {
						System.out.println("이미지 못받음");
					}
					if (!isVisible())
						onScreen = false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void drawImage(Image img, int x, int y) {
			Graphics g = this.getGraphics();
			g.drawImage(img, 0, 0, x, y, this);
			this.paint(g);
			this.repaint();
		}

		public void paint(Graphics g) {
			if (RobotClient.img != null) {
				g.drawImage(RobotClient.img, 0, 0, RobotClient.img.getWidth(this), RobotClient.img.getHeight(this),
						this);
			}
		}

		public void mouseClicked(MouseEvent e) {
			// mouse Click
		}

		public void mousePressed(MouseEvent e) {
			out.println("#press#" + e.getX() + ":" + e.getY());
			// 마우스 버튼 클릭
		}

		public void mouseReleased(MouseEvent e) {
			out.println("#release#" + e.getX() + ":" + e.getY());
		}

		public void mouseEntered(MouseEvent e) {
			// mouse Enter
		}

		public void mouseExited(MouseEvent e) {
			// mouse Exit
		}

		public void mouseDragged(MouseEvent e) {
			out.println("#drag#" + e.getX() + ":" + e.getY());
		}

		public void mouseMoved(MouseEvent e) {
			out.println("#move#" + e.getX() + ":" + e.getY());
			// move
		}
	}

	public static void main(String[] args) {
		RobotClient rc = new RobotClient();
		rc.run();
	}
}