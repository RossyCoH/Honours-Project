package userApplication;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class EquipmentOutReturnWindow {
	
	private ClientApplication logicLayer;
	private String inOutState;
	
	private JPanel operatorPanel = new JPanel();
	private JLabel idLbl = new JLabel("Select member to use equipment: ");
	private JComboBox<String> opCmb = new JComboBox<String>();
	{
	    operatorPanel.add(idLbl);
		operatorPanel.add(opCmb);
		operatorPanel.setVisible(true);
		
	}
	
	private JPanel notesPanel = new JPanel();
	private JLabel noteLbl = new JLabel("Notes: ");
	private JTextField noteTxt = new JTextField(25);
	{
		notesPanel.add(noteLbl);
		notesPanel.add(noteTxt);
		notesPanel.setVisible(true);
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
		window.setLayout(new GridLayout(3, 1));
		window.setPreferredSize(new Dimension(600,400));
		window.add(operatorPanel);
		window.add(notesPanel);
		window.add(buttonPanel);
		window.pack();
		window.setTitle("Equipment Managment App - Equipment Checkout/Return");
		try {
			window.setIconImage(ImageIO.read(new File("src/imgs/radio_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public EquipmentOutReturnWindow(ClientApplication app, String state) {
		logicLayer = app;
		
		DefaultComboBoxModel<String> cmbModel = new DefaultComboBoxModel<String>();
		ArrayList<String> opList = new ArrayList<String>();
		
		for (String r : logicLayer.getOperators()) {
			opList.add(r);
		}
		
		cmbModel.addAll(opList);
		opCmb.setModel(cmbModel);
		
		if(!state.equals("out") && !state.equals("in")) {
			JOptionPane.showMessageDialog(window, "Error in program! Contact Admin!");
			window.dispose();
		}
		else {
			inOutState = state;
		}
		
		if (state .equals("out")) {
			noteLbl.setVisible(false);
			noteTxt.setVisible(false);
		}
		else if (state.equals("in")) {
			idLbl.setVisible(false);
			opCmb.setVisible(false);
		}
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		
		submitButton.addActionListener(new SubmitButtonListener());
		backButton.addActionListener( new BackButtonListener());
		
		
	}
	
	// The action listener on the submit button
	private class SubmitButtonListener implements ActionListener
	{
		// Called when the submit button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			// If equipment available, therefore to be checked out
			if (inOutState.equals("out")) {
				//
				String selectedOpID = String.valueOf(opCmb.getSelectedItem()).split(",")[0];
				if (logicLayer.checkoutEquipment(selectedOpID) ) {
					JOptionPane.showMessageDialog(window, "Equipment checkout successfully");
					
				}
				else {
					JOptionPane.showMessageDialog(window, "Error! Try Again");
					return;
				}
				
				logicLayer.resetWindowDisplay(1);
				window.dispose();
			}
			else if (inOutState.equals("in")) {
				if ( logicLayer.returnEquipment(noteTxt.getText() ) ) {
					JOptionPane.showMessageDialog(window, "Equipment return successful");
				}
				else {
					JOptionPane.showMessageDialog(window, "Error! Try Again");
					return;
				}
				
				logicLayer.resetWindowDisplay(1);
				window.dispose();
			}
			
		}
	}
	
	private class BackButtonListener implements ActionListener
	{
		// Called when the submit button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			logicLayer.resetWindowDisplay(1);
			window.dispose();
		}
	}
	

}
