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

public class ClientAddWindow {
	
	private BusinessApplication logicLayer;
	
	private JPanel infoPanel = new JPanel();
	private JLabel nameLbl = new JLabel("Name: *");
	private JTextField nameTxt = new JTextField(15);
	private JLabel addressLbl = new JLabel("Address: *");
	private JTextField addressTxt = new JTextField(25);

	{
		JPanel namePanel = new JPanel();
	    namePanel.add(nameLbl);
	    namePanel.add(nameTxt);
	    JPanel addrPanel = new JPanel();
	    addrPanel.add(addressLbl);
	    addrPanel.add(addressTxt);
		
	    infoPanel.add(namePanel);
	    infoPanel.add(addrPanel);
	    infoPanel.setLayout(new GridLayout(3,1));
	    infoPanel.setVisible(true);
		
	}
	
	private JPanel contactPanel = new JPanel();
	private JLabel contactNameLbl = new JLabel("Contact Name: *");
	private JTextField contactNameTxt = new JTextField(30);
	private JLabel contactAddressLbl = new JLabel("Contact Address:");
	private JTextField contactAddressTxt = new JTextField(30);
	{
		JPanel cNamePanel = new JPanel();
		cNamePanel.add(contactNameLbl);
		cNamePanel.add(contactNameTxt);
		JPanel cAddrPanel = new JPanel();
		cAddrPanel.add(contactAddressLbl);
		cAddrPanel.add(contactAddressTxt);
		
		contactPanel.add(cNamePanel);
		contactPanel.add(cAddrPanel);
		contactPanel.setLayout(new GridLayout(2, 1));
		contactPanel.setVisible(true);
	}
	
	private JPanel passwordPanel = new JPanel();
	private JLabel passwordLbl = new JLabel("Password: *");
	private JPasswordField passwordFld = new JPasswordField(20);
	{
		passwordPanel.add(passwordLbl);
		passwordPanel.add(passwordFld);
		passwordPanel.setVisible(true);
	}
	
	private JPanel buttonPanel = new JPanel();
	private JButton backButton = new JButton("< Back");
	private JButton submitButton = new JButton("Submit");
	{
		
		buttonPanel.add(backButton);
		buttonPanel.add(submitButton);
		buttonPanel.setVisible(true);
	}
	
	private JFrame window = new JFrame();
	{
		window.setLayout(new GridLayout(5, 1));
		window.setPreferredSize(new Dimension(600,400));
		window.add(infoPanel);
		window.add(contactPanel);
		window.add(passwordPanel);
		window.add(buttonPanel);
		window.pack();
		window.setTitle("Equipment Managment App -Add Client");
		try {
			window.setIconImage(ImageIO.read(new File("src/imgs/radio_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ClientAddWindow( BusinessApplication app) {
		logicLayer = app;
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		
		submitButton.addActionListener(new SubmitButtonListener());
		backButton.addActionListener( new BackButtonListener());
		
		//updateTable();
	}
	
	// The action listener on the submit button
	private class SubmitButtonListener implements ActionListener
	{
		// Called when the submit button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			if (logicLayer.addClient(nameTxt.getText(), addressTxt.getText(), contactNameTxt.getText(), contactAddressTxt.getText(), passwordFld.getPassword()) ) {
				JOptionPane.showMessageDialog(window, "Client Added Successfully");
				
			}
			else {
				JOptionPane.showMessageDialog(window, "Error! Try Again");
				return;
			}
			logicLayer.resetWindowDisplay(3);
			window.dispose();
		}
	}
	
	private class BackButtonListener implements ActionListener
	{
		// Called when the submit button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			//logicLayer.resetWindowDisplay();
			window.dispose();
		}
	}
}
