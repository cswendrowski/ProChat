package proc;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPException;

import steamWrapper.SteamEvent;
import steamWrapper.SteamListener;

/**
 * @author Cody
 * 
 */
public class LoginWindow implements ActionListener, KeyListener, SteamListener {
	JFrame frame;
	File saved;
	XmppManager connection;
	private String savedName;
	SavedInfoLoader savedInfo;

	public LoginWindow() {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					// javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		frame = new JFrame();

		String user = "";
		String pass = "";

		savedName = user;

		frame.addKeyListener(this);

		SavedInfoLoader.createInstance();

		savedInfo = SavedInfoLoader.getInstance();

		user = savedInfo.user;
		pass = savedInfo.pass;

		DisplayInputWindow(user, pass);
	}

	JTextField loginName;
	JPasswordField loginPass;

	// Variables declaration
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private JTextField serverIP;
	private JTextField serverPort;

	private void DisplayInputWindow(String user, String pass) {
		frame = new JFrame();
		frame.setSize(400, 600);
		frame.setTitle("ProChat");

		try {
			frame.setIconImage(ImageIO.read(this.getClass()
					.getResourceAsStream("logo.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		jLabel1 = new javax.swing.JLabel();
		loginName = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		loginPass = new javax.swing.JPasswordField();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();

		serverIP = new JTextField("ServerIP");
		serverPort = new JTextField("Port");

		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.setBackground(new java.awt.Color(255, 204, 0));

		jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
		jLabel1.setText("Username");

		loginName.setText(user);
		loginName.addKeyListener(this);

		jLabel2.setFont(new java.awt.Font("Trebuchet MS", 1, 18)); // NOI18N
		jLabel2.setText("Password");

		loginPass.setText(pass);
		loginPass.addKeyListener(this);

		jButton1.setText("Login");
		jButton1.setActionCommand("Login");
		jButton1.addActionListener(this);

		jButton2.setText("Register");
		jButton2.setActionCommand("Register");
		jButton2.addActionListener(this);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				frame.getContentPane());
		frame.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														loginPass,
														javax.swing.GroupLayout.Alignment.TRAILING)
												.addComponent(loginName)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						jLabel1)
																				.addComponent(
																						jLabel2)
																				.addComponent(
																						jButton1,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						191,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addComponent(
																		jButton2,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		183,
																		javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jLabel1)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(loginName,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										31,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(jLabel2)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(loginPass,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										28,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										23, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(
														jButton2,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														41, Short.MAX_VALUE)
												.addComponent(
														jButton1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addContainerGap()));

		frame.setLocation(500, 300);
		frame.pack();
		frame.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Login"))
			login();
		else if (e.getActionCommand().equals("Register"))
			register();
		else if (e.getActionCommand().equals("Ok"))
			submitRegistration();
		else if (e.getActionCommand().equals("Cancel"))
			regframe.dispose();

	}

	JFrame regframe;
	JTextField name;
	JPasswordField pass;

	/**
	 * 
	 */
	private void register() {

		regframe = new JFrame();
		regframe.setSize(300, 500);
		regframe.setTitle("Register");
		JPanel masterPanel = new JPanel();

		masterPanel.setLayout(new GridLayout(5, 1));

		JLabel userLabel = new JLabel("Username");
		JLabel passLabel = new JLabel("Password");

		if (savedName.equals(loginName.getText()))
			name = new JTextField("");
		else
			name = new JTextField(loginName.getText());

		pass = new JPasswordField("");

		name.addKeyListener(this);
		pass.addKeyListener(this);

		masterPanel.add(userLabel);
		masterPanel.add(name);
		masterPanel.add(passLabel);
		masterPanel.add(pass);

		JButton submit = new JButton("Ok");
		submit.addActionListener(this);

		JButton register = new JButton("Cancel");
		register.addActionListener(this);

		JPanel buttons = new JPanel(new GridLayout(0, 2));

		buttons.add(submit);
		buttons.add(register);

		regframe.add(masterPanel);
		regframe.add(buttons, BorderLayout.SOUTH);
		regframe.setIconImage(Toolkit.getDefaultToolkit().getImage("logo.png"));

		regframe.setVisible(true);

	}

	/**
	 * @param pass2
	 * @return
	 */
	private String bananaStrawberryEncryption(String in) {

		System.out.println("In is " + in);

		// Banana - Strawberry
		in = in.replace("b", "st");
		in = in.replace("a", "ra");
		in = in.replace("n", "wb");
		in = in.replace("B", "er");
		in = in.replace("A", "ry");
		in = in.replace("N", "na");

		// Kiwi - Orange
		in = in.replace("k", "or");
		in = in.replace("i", "an");
		in = in.replace("w", "ge");
		in = in.replace("K", "eg");
		in = in.replace("I", "na");
		in = in.replace("W", "pr");

		in = in.replace("1", "@n");
		in = in.replace("@", "6^");

		System.out.println("In is now " + in);

		in = String.format("%040x",
				new BigInteger(1, in.getBytes()).shiftLeft(4));

		System.out.println("In is now: " + in);

		return in;
	}

	private void submitRegistration() {
		AccountManager am = new AccountManager(connection.getConnection());
		try {
			String password = new String(pass.getPassword());
			password = bananaStrawberryEncryption(password);
			Log.l("Reg: " + name.getText() + "  " + password);
			am.createAccount(name.getText(), password);
			regframe.dispose();
			login();
			// Log.l("Registered " + loginName.getText());
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	boolean enterFix = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			enterFix = !enterFix; // Allows closing of invalid login window
									// without trying to log in again.
			if (enterFix)
				login();
		}

	}

	/**
	 * 
	 */
	private void login() {
		// Write info to .txt
		String serverIP = "54.200.92.207";
		int port = 5222;
		try {
			connection = new XmppManager(serverIP, port);
			connection.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			savedInfo.updateSavedLogin(loginName.getText(), new String(
					loginPass.getPassword()));

			User user = new User(loginName.getText(),
					bananaStrawberryEncryption(new String(
							loginPass.getPassword())));

			connection.getConnection().login(user.getName(), user.getPass());

			user.setEmail(connection.getConnection().getAccountManager()
					.getAccountAttribute("email"));

			Home home = new Home(user, connection);
			home.show();
			// JOptionPane.showMessageDialog(frame, "Successfully logged in as "
			// + loginName.getText());
			frame.dispose();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(frame, "Could not login as "
					+ loginName.getText() + "\nError: " + e1.getMessage());

			if (e1.getMessage().contains("savedInfo.txt")) {
				// Still login if we can't store info
				try {
					User user = new User(loginName.getText(),
							bananaStrawberryEncryption(new String(
									loginPass.getPassword())));

					connection.getConnection().login(user.getName(),
							user.getPass());

					user.setEmail(connection.getConnection()
							.getAccountManager().getAccountAttribute("email"));

					Home home = new Home(user, connection);
					home.show();
					JOptionPane.showMessageDialog(frame,
							"Successfully logged in as " + loginName.getText());
					frame.dispose();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame,
							"Could not login as " + loginName.getText()
									+ "\nError: " + e1.getMessage());
					e.printStackTrace();
				}
			}

			e1.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see steamWrapper.SteamListener#SteamUpdate(steamWrapper.SteamEvent)
	 */
	@Override
	public void SteamUpdate(SteamEvent e) {
		System.out.println("Steamupdate: " + e);

	}

}
