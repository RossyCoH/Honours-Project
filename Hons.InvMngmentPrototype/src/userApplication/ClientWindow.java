package userApplication;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientWindow {
	
	private ClientApplication logicLayer;
	
	private JPanel topPanel = new JPanel();
	private JLabel userLbl = new JLabel();
	{
		topPanel.add(userLbl);
	}
	
	private JPanel feedbackPanel = new JPanel();
	private JTable recordTable = new JTable();
	private JLabel tblLbl = new JLabel("All Hires: ");
	DefaultTableModel model = new DefaultTableModel();
	JScrollPane scroll = new JScrollPane(recordTable);
	{
		
		recordTable.setDefaultEditor(Object.class, null);
		recordTable.setRowSelectionAllowed(true);
		recordTable.setCellSelectionEnabled(true);
	    ListSelectionModel cellSelectionModel = recordTable.getSelectionModel();
	    cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
	    recordTable.addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
	    		int row = recordTable.getSelectedRow();
	    		Object selectedHID;
				try {
					selectedHID = recordTable.getModel().getValueAt(row, 0);
					hireSelection(selectedHID);
				} catch (ClassCastException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		//System.out.println(selectedHID);
	    		
	    	}
	    });
		
		feedbackPanel.setLayout(new GridLayout(2,1));
		scroll.setPreferredSize(new Dimension(380,280));
		feedbackPanel.add(tblLbl);
		feedbackPanel.add(scroll);
		feedbackPanel.setVisible(true);
		
	}
	
	
	
	private JPanel buttonPanel = new JPanel();
	private JButton logoutButton = new JButton("Logout");
	private JButton addOpButton = new JButton("Operators");
	
	{
		buttonPanel.add(logoutButton);
		buttonPanel.add(addOpButton);
	}
	
	private JFrame window = new JFrame();
	{
		window.setLayout(new GridLayout(3, 1));
		window.setPreferredSize(new Dimension(800,600));
		window.add(topPanel);
		window.add(feedbackPanel);
		window.add(buttonPanel);
		window.pack();
		window.setTitle("Equipment Managment App - Client App");
		try {
			window.setIconImage(ImageIO.read(new File("src/imgs/radio_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ClientWindow( ClientApplication app, String uInfo) {
		logicLayer = app;
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		logoutButton.addActionListener(new LogoutButtonListener());
		addOpButton.addActionListener(new AddOpButtonListener());
		
		
		userLbl.setText("Client:  " + uInfo);
		updateTable();
	}
	
	// The action listener on the check button
	private class LogoutButtonListener implements ActionListener
	{
		// Called when the check button is clicked
		public void actionPerformed(ActionEvent arg0)
		{
			logicLayer.Logout();
			window.dispose();
		}
	}
	
	private class AddOpButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			logicLayer.diplayOperatorWindow();
		}
	}
	
	private void updateTable() {
		
		recordTable.repaint();
		model.setRowCount(0);
		model.setColumnCount(0);
		
		String column[] = {"HIRE ID", "START DATE", "END DATE", "COMPLETE", "VIEW"};
		for (String c : column) {
			model.addColumn(c);
		}
		
		ArrayList<String[]> records = logicLayer.clientHireRecords();
		for (String[] r : records) {
			
			model.addRow(r);
			//System.out.println(r[0]);
		}
		
		//feedbackPanel.add(new JScrollPane(jt));
		System.out.println();
		
		recordTable.setModel(model);
	}

	private void hireSelection(Object hID) {
		//System.out.println(hID);
		String hireS = (String) hID;
		logicLayer.setHire(Integer.parseInt(hireS));
		logicLayer.displayHireRecordWindow(false);
		window.setVisible(false); // Change to Close and clear resources!!
	}
}
