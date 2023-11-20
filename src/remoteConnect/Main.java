package remoteConnect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel implements Runnable, ActionListener {

	JButton btn_capture;
	Image img = null; // 생성자. UI 배치.

	public Main() {
		this.btn_capture = new JButton("영상캡쳐");
		this.btn_capture.addActionListener(this);
		this.setLayout(new BorderLayout());
		this.add(this.btn_capture, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("영상캡쳐")) {
			System.out.println("영상을 캡쳐합니다..");
			while (true) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.capture(); // 영상캡처 버튼이 눌리면 캡쳐.
			}
		}
	}

	public void drawImage(Image img, int x, int y) {
		Graphics g = this.getGraphics();
		g.drawImage(img, 0, 0, x, y, this);
		this.paint(g);
		this.repaint();
	}

	public void paint(Graphics g) {
		if (this.img != null) {
			g.drawImage(this.img, 0, 0, this.img.getWidth(this), this.img.getHeight(this), this);
		}
	}

	public void capture() {
		BufferedImage bufImage = null;
		try {
			Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			bufImage = ImageIO.read(new File("C:/Users/Jongwon.JONG-PC/Desktop/send/capture.png")); // Robot 클래스를 이용하여
																									// // // 스크린 캡쳐.
			// Graphics2D g2d = bufImage.createGraphics();
			int w = this.getWidth();
			int h = this.getHeight();
			System.out.println(w + ", " + h);
			this.img = bufImage.getScaledInstance(w, h, Image.SCALE_DEFAULT);
			// this.repaint();
			this.drawImage(img, w, h);
			System.out.println(img.getHeight(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			this.setBackground(Color.RED);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
			this.setBackground(Color.GREEN);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}
	}

	public static void createFrame() {
		JFrame frame = new JFrame("Jv");
		JFrame.setDefaultLookAndFeelDecorated(true);
		Container cont = frame.getContentPane();
		cont.setLayout(new BorderLayout());
		Main mm = new Main();
		// new Thread(mm).start();
		cont.add(mm, BorderLayout.CENTER);
		frame.setSize(400, 400);
		frame.setVisible(true);
	}

	public static void main(String... v) {
		// new Main();
		JFrame.setDefaultLookAndFeelDecorated(true);
		createFrame();
	}
}
