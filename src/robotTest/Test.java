package robotTest;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class Test extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
	Robot r;
	JButton start, stop, test, capture;
	JDialog captureScreen;

	BufferedImage screen;
	boolean saveLocation = false, captureOn = false;
	boolean pressOn = false;

	int clickCnt = 0, x = 0, y = 0, width = 1360, height = 768;
	String[] clickRecord;

	public Test() {
		super("test");
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		start = new JButton("start");
		start.setBackground(Color.white);
		start.setFocusable(false);
		start.setFont(new Font("Dialog", Font.BOLD, 12));
		start.setPreferredSize(new Dimension(70, 30));
		start.setActionCommand("start");
		start.addActionListener(this);

		stop = new JButton("stop");
		stop.setBackground(Color.white);
		stop.setFocusable(false);
		stop.setFont(new Font("Dialog", Font.BOLD, 12));
		stop.setPreferredSize(new Dimension(70, 30));
		stop.setActionCommand("stop");
		stop.addActionListener(this);

		test = new JButton("test");
		test.setBackground(Color.white);
		test.setFocusable(false);
		test.setFont(new Font("Dialog", Font.BOLD, 12));
		test.setPreferredSize(new Dimension(70, 30));
		test.setActionCommand("test");
		test.addActionListener(this);

		capture = new JButton("capture");
		capture.setBackground(Color.white);
		capture.setFocusable(false);
		capture.setFont(new Font("Dialog", Font.BOLD, 12));
		capture.setPreferredSize(new Dimension(70, 30));
		capture.setActionCommand("capture");
		capture.addActionListener(this);

		captureScreen = new JDialog();
		captureScreen.setSize(1360, 768);

		add(start);
		add(stop);
		add(test);
		add(capture);
		addMouseListener(this);
		addMouseMotionListener(this);
		setLayout(new FlowLayout());
		setSize(200, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		new Test();
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("start")) {
			clickRecord = new String[10000];
			start();
		} else if (command.equals("stop")) {
			stop();
		} else if (command.equals("test")) {
			test();
		} else if (command.equals("capture")) {
			captureOn = true;
			capture(x, y, width, height);
		}
	}

	public void start() {
		saveLocation = true;
	}

	public void stop() {
		saveLocation = false;
	}

	public void test() {
		int cnt = 0;
		try {
			r.mouseMove(120, 190);
		} catch (Exception e) {

		}

	}

	public void capture(int x, int y, int width, int height) {
		screen = r.createScreenCapture(new Rectangle(x, y, width, height));
		captureScreen.setIconImage(screen);
		captureScreen.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {
		if (saveLocation) {
			System.out.println("Click X : " + e.getX() + " Y : " + e.getY());
		}
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		if (saveLocation) {
			clickRecord[clickCnt++] = e.getX() + ":" + e.getY();
			System.out.println("X : " + e.getX() + " Y : " + e.getY());
		}
	}
}
