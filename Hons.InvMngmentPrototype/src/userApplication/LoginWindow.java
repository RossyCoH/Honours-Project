package userApplication;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;

/***
 * Class for window for user to login 
 * @author Ross Hunter
 * @since
 */
public class LoginWindow {
	
	private UserApplicationShared logicLayer; // Logic class to
	
	private JPanel headPanel = new JPanel();
	{
		headPanel.setVisible(true);
		
	}
	
	private JPanel inputPanel = new JPanel();
	private JLabel usernameLbl = new JLabel("Name: ");
	private JTextField usernameTxt = new JTextField(20);
	private JLabel passLbl = new JLabel("Password: ");
	private JPasswordField passTxt = new JPasswordField(20);
	{
		JPanel userNamePanel  = new JPanel();
		userNamePanel.add(usernameLbl);
		userNamePanel.add(usernameTxt);
		
		JPanel passwordPanel  = new JPanel();
		passwordPanel.add(passLbl);
		passwordPanel.add(passTxt);
		
		inputPanel.add(userNamePanel);
		inputPanel.add(passwordPanel);
		inputPanel.setLayout(new GridLayout(2,1));;
		inputPanel.setVisible(true);
	}
	
	private JPanel buttonPanel = new JPanel();
	private JButton exitButton = new JButton("EXIT");
	private JButton loginButton = new JButton("Submit");
	{
		
		buttonPanel.add(exitButton);
		buttonPanel.add(loginButton);
		buttonPanel.setVisible(true);
	}
	
	private JFrame window = new JFrame();
	{
		window.setLayout(new GridLayout(3, 1));
		window.setPreferredSize(new Dimension(600,400));
		window.add(headPanel);
		window.add(inputPanel);
		window.add(buttonPanel);
		window.pack();
		window.setTitle("Equipment Managment App - Login");
		try {
			window.setIconImage(ImageIO.read(new File("src/imgs/radio_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LoginWindow( UserApplicationShared app) {
		logicLayer = app;
		/*
		 * try {
		 * UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
		 * ); } catch (ClassNotFoundException | InstantiationException |
		 * IllegalAccessException | UnsupportedLookAndFeelException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		
		loginButton.addActionListener(new LoginButtonListener());
		exitButton.addActionListener( new ExitButtonListener());
		
		//updateTable();
	}
	
	// The action listener on the submit button
		private class LoginButtonListener implements ActionListener
		{
			// Called when the submit button is clicked
			public void actionPerformed(ActionEvent arg0)
			{
				if (logicLayer.loginProcess(usernameTxt.getText(), passTxt.getPassword())) {
					JOptionPane.showMessageDialog(window, "Login Successful!");
					displose();
				}
				else {
					JOptionPane.showMessageDialog(window, "Login Not Vaild! Try Again!");
					return;
				}
				displose();
			}
		}
		
		// Class to handle action lister for exit button
		private class ExitButtonListener implements ActionListener
		{
			// Called when the submit button is clicked
			public void actionPerformed(ActionEvent arg0)
			{
				window.setVisible(false);
				logicLayer.endApp();
				displose();
			}
		}
	
		// Funtion to dispose of the window when done
		private void displose() {
			window.dispose();
		}
}
