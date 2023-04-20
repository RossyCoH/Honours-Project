package userApplication;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OperatorWindow {
	
	private ClientApplication logicLayer;
	
	private JPanel listPanel = new JPanel();
	private JLabel curLbl = new JLabel("Current Operators: ");
	private JList<String> opLst = new JList<String>();
	private DefaultListModel<String> opListModel = new DefaultListModel<String>();
	
	{
		listPanel.add(curLbl);
		listPanel.add(opLst);
		
		listPanel.setVisible(true);
		
	}
	
	private JPanel addPanel = new JPanel();
	private JLabel addLbl = new JLabel("Add operator (name): ");
	private JTextField opTxt = new JTextField(15);
	{
		addPanel.add(addLbl);
		addPanel.add(opTxt);
		addPanel.setVisible(true);
	}
	
	private JPanel buttonPanel = new JPanel();
	private JButton backButton = new JButton("< Back");
	private JButton addOpButton = new JButton("Add Operator");
	{
		
		buttonPanel.add(backButton);
		buttonPanel.add(addOpButton);
		buttonPanel.setVisible(true);
	}
	
	private JFrame window = new JFrame();
	{
		window.setLayout(new GridLayout(3, 1));
		window.setPreferredSize(new Dimension(600,400));
		window.add(listPanel);
		window.add(addPanel);
		window.add(buttonPanel);
		window.pack();
		window.setTitle("Equipment Managment App - Equipment Operators");
		try {
			window.setIconImage(ImageIO.read(new File("src/imgs/radio_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public OperatorWindow(ClientApplication app) {
		logicLayer = app;
		
		updateOpComboBox();
		
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		
		addOpButton.addActionListener(new AddOpButtonListener());
		backButton.addActionListener( new BackButtonListener());
		
		
	}
	
	/***
	 * Function to update list of operators
	 */
	private void updateOpComboBox() {
		opLst.repaint(); // Reset listbox
		opListModel.setSize(0); // reset model
		
		String[] records = logicLayer.getOperators(); // Get array of operators
		
		// Add operator(String) to model
		int i = 0;
		for (String r : records) {
			opListModel.add(i, r);
			i++;
		}
		
		opLst.setModel(opListModel); // Set model to list
	}
	
	// The action listener on the submit button
	private class AddOpButtonListener implements ActionListener
	{
		// Called when the submit button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			String opName = opTxt.getText();
			if (!opName.equals("") && logicLayer.addOperator(opName)) {
				JOptionPane.showMessageDialog(window, "Operator Added Successfully");
				updateOpComboBox();
				opTxt.setText("");
			}
			else {
				JOptionPane.showMessageDialog(window, "Error! Try Again");
			}
			
		}
	}
	
	private class BackButtonListener implements ActionListener
	{
		// Called when the submit button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			window.dispose();
			logicLayer.closeOperatorWindow();
		}
	}
}
